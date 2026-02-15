package tests;

import org.junit.jupiter.api.Test;
import pages.LoginPage;

public class FavoriteRouteOrderTest extends TestBase {
    static final String email = "p2@uberplus.com";
    static final String password = "password";

    @Test
    public void favoriteRouteOrderTest() {
        driver.get("http://localhost:4200/signIn");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
    }
}
