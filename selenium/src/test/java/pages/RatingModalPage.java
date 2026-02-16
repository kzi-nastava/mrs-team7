package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RatingModalPage {
    WebDriver driver;

    @FindBy(css = ".fixed.inset-0")
    WebElement modalBackdrop;

    @FindBy(css = ".rating-modal-title")
    WebElement modalTitle;

    @FindBy(css = "textarea")
    WebElement commentTextarea;

    @FindBy(css = ".submit-rating-button")
    WebElement submitButton;

    @FindBy(css = ".driver-rating-label")
    WebElement driverRatingLabel;

    @FindBy(css = ".vehicle-rating-label")
    WebElement vehicleRatingLabel;

    public RatingModalPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isModalDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofMillis(300)).
                    until(ExpectedConditions.visibilityOf(modalBackdrop));
            new WebDriverWait(driver, Duration.ofMillis(300)).
                    until(ExpectedConditions.visibilityOf(modalTitle));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public void setDriverRating(int stars) {
        WebElement parentDiv = driverRatingLabel.findElement(By.xpath("./following-sibling::div"));
        List<WebElement> starButtons = parentDiv.findElements(new By.ByCssSelector(".driver-star-button"));
        WebElement starButton = new WebDriverWait(driver, Duration.ofMillis(300))
                .until(ExpectedConditions.elementToBeClickable(starButtons.get(stars - 1)));
        starButton.click();
    }

    public void setVehicleRating(int stars) {
        WebElement parentDiv = vehicleRatingLabel.findElement(By.xpath("./following-sibling::div"));
        List<WebElement> starButtons = parentDiv.findElements(new By.ByCssSelector(".vehicle-star-button"));
        WebElement starButton = new WebDriverWait(driver, Duration.ofMillis(300))
                .until(ExpectedConditions.elementToBeClickable(starButtons.get(stars - 1)));
        starButton.click();
    }

    public void enterComment(String comment) {
        WebElement textarea = new WebDriverWait(driver, Duration.ofMillis(300))
                .until(ExpectedConditions.visibilityOf(commentTextarea));
        assert textarea != null;
        textarea.sendKeys(comment);
    }

    public void clickSubmit() {
        WebElement button = new WebDriverWait(driver, Duration.ofMillis(300))
                .until(ExpectedConditions.elementToBeClickable(submitButton));
        button.click();
    }

    public void clickCloseButton() {
        WebElement xButton = driver.findElement(new By.ByCssSelector("h2 button"));
        WebElement button = new WebDriverWait(driver, Duration.ofMillis(300))
                .until(ExpectedConditions.elementToBeClickable(xButton));
        button.click();
    }

    public boolean isSubmitButtonDisabled() {
        try {
            String disabled = submitButton.getAttribute("disabled");
            return disabled != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCommentValue() {
        return commentTextarea.getAttribute("value");
    }
}