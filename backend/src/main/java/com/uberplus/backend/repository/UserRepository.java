package com.uberplus.backend.repository;

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
       SELECT u.*,
         CASE
           WHEN lower(u.name) = lower(:q) OR lower(u.last_name) = lower(:q) OR lower(u.email) = lower(:q) THEN 0
           WHEN lower(u.name) LIKE lower(:q) || '%' OR lower(u.last_name) LIKE lower(:q) || '%' OR lower(u.email) LIKE lower(:q) || '%' THEN 1
           WHEN lower(u.name) LIKE '%' || lower(:q) || '%' OR lower(u.last_name) LIKE '%' || lower(:q) || '%' OR lower(u.email) LIKE '%' || lower(:q) || '%' THEN 2
           ELSE 3
         END as score
       FROM users u
       WHERE lower(u.name) LIKE '%' || lower(:q) || '%'
          OR lower(u.last_name) LIKE '%' || lower(:q) || '%'
          OR lower(u.email) LIKE '%' || lower(:q) || '%'
       ORDER BY score ASC, lower(u.name) ASC
       LIMIT :limit
       """, nativeQuery = true)
    List<User> searchUsers(@Param("q") String q, @Param("limit") int limit);
}
