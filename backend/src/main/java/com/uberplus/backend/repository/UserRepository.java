package com.uberplus.backend.repository;

import com.uberplus.backend.dto.user.UserProfileDTO;
import com.uberplus.backend.dto.user.UserSearchResultDTO;
import com.uberplus.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationToken(String token);
    Optional<User> findByPasswordResetToken(String token);

    @Query(value = """
        SELECT u.id as id,\s
          u.email as email,\s
          u.first_name as firstName,\s
          u.last_name as lastName,
          u.blocked as blocked
       FROM users u
       WHERE lower(u.first_name) LIKE '%' || lower(:q) || '%'
          OR lower(u.last_name) LIKE '%' || lower(:q) || '%'
          OR lower(u.email) LIKE '%' || lower(:q) || '%'
       ORDER BY 
         CASE
           WHEN lower(u.first_name) = lower(:q) OR lower(u.last_name) = lower(:q) OR lower(u.email) = lower(:q) THEN 0
           WHEN lower(u.first_name) LIKE lower(:q) || '%' OR lower(u.last_name) LIKE lower(:q) || '%' OR lower(u.email) LIKE lower(:q) || '%' THEN 1
           ELSE 2
         END ASC, 
         lower(u.first_name) ASC
       LIMIT :limit
   """, nativeQuery = true)
    List<UserSearchResultDTO> searchUsers(@Param("q") String q, @Param("limit") int limit);
}
