package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import pages.LoginPage;
import pages.RatingModalPage;
import pages.PassengerRideHistoryPage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RatingTest extends TestBase {

    static final String PASSENGER_EMAIL = "p2@uberplus.com";
    static final String PASSENGER_PASSWORD = "password";
    private PassengerRideHistoryPage passengerRideHistoryPage;
    private RatingModalPage ratingModal;

    @BeforeEach
    public void loginAndNavigateToRideHistoryAndRatingModal() {
        driver.get("http://localhost:4200/signIn");
        LoginPage loginPage = new LoginPage(driver);

        Assumptions.assumeTrue(loginPage.isOnLoginPage());

        loginPage.enterEmail(PASSENGER_EMAIL);
        loginPage.enterPassword(PASSENGER_PASSWORD);
        loginPage.login();

        passengerRideHistoryPage = new PassengerRideHistoryPage(driver);
        passengerRideHistoryPage.goToRideHistory();
        Assertions.assertTrue(passengerRideHistoryPage.isPageLoaded());
    }

    @Test
    @Order(1)
    public void testSuccessfulRideRating() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"green");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.setDriverRating(5);
        ratingModal.setVehicleRating(4);
        ratingModal.enterComment("Great ride!");
        ratingModal.clickSubmit();

        ratingModal.clickCloseButton();
        Assertions.assertFalse(ratingModal.isModalDisplayed());
    }

    @Test
    @Order(2)
    public void testSuccessfulRideRatingWithNoComment() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"green");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.setDriverRating(5);
        ratingModal.setVehicleRating(4);
        ratingModal.clickSubmit();

        ratingModal.clickCloseButton();
        Assertions.assertFalse(ratingModal.isModalDisplayed());
    }

    @Test
    @Order(3)
    public void testSubmitWithoutDriverRating() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"red");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.setVehicleRating(4);
        ratingModal.enterComment("Great ride!");

        Assertions.assertTrue(ratingModal.isSubmitButtonDisabled());
    }

    @Test
    @Order(4)
    public void testSubmitWithoutVehicleRating() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"red");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.setDriverRating(5);
        ratingModal.enterComment("Great ride!");

        Assertions.assertTrue(ratingModal.isSubmitButtonDisabled());
    }

    @Test
    @Order(5)
    public void testSubmitWithoutAnyRating() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"red");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.enterComment("Great ride!");

        Assertions.assertTrue(ratingModal.isSubmitButtonDisabled());
    }

    @Test
    @Order(6)
    public void testCommentWithMaxLength() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"green");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.setDriverRating(5);
        ratingModal.setVehicleRating(4);

        String maxLengthComment = "A".repeat(500);
        ratingModal.enterComment(maxLengthComment);

        String actualComment = ratingModal.getCommentValue();
        Assertions.assertEquals(500, actualComment.length());

        ratingModal.clickSubmit();
        ratingModal.clickCloseButton();
        Assertions.assertFalse(ratingModal.isModalDisplayed());
    }

    @Test
    @Order(7)
    public void testCancelRatingDoesNotSaveData() {
        WebElement rateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(rateableRide);
        passengerRideHistoryPage.highlightElement(rateableRide,"red");

        passengerRideHistoryPage.clickRateButtonForRide(rateableRide);

        ratingModal = new RatingModalPage(driver);
        Assertions.assertTrue(ratingModal.isModalDisplayed());

        ratingModal.setDriverRating(5);
        ratingModal.setVehicleRating(4);
        ratingModal.enterComment("This should not be saved");

        ratingModal.clickCloseButton();
        Assertions.assertFalse(ratingModal.isModalDisplayed());

        passengerRideHistoryPage.goToRideHistory();
        WebElement stillRateableRide = passengerRideHistoryPage.getFirstRateableRide();
        Assertions.assertNotNull(stillRateableRide, "Ride should still be rateable after canceling");
    }

    @Test
    @Order(8)
    public void testDisabledRateButtonDoesNotOpenModal() {
        passengerRideHistoryPage.goToRideHistory();
        WebElement alreadyRatedRide = passengerRideHistoryPage.getFirstAlreadyRatedRide();
        Assumptions.assumeTrue(alreadyRatedRide != null, "No already rated ride found");
        passengerRideHistoryPage.highlightElement(alreadyRatedRide,"red");

        passengerRideHistoryPage.tryClickRateButtonForRide(alreadyRatedRide);

        RatingModalPage ratingModal = new RatingModalPage(driver);
        Assertions.assertFalse(ratingModal.isModalDisplayed());
    }
}