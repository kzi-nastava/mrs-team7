package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PassengerRideHistoryPage {
    WebDriver driver;

    @FindBy(css = "h2")
    WebElement pageTitle;

    public PassengerRideHistoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void goToRideHistory() {
        driver.get("http://localhost:4200/user/ride-history");
    }

    public boolean isPageLoaded() {
        try {
            new WebDriverWait(driver, Duration.ofMillis(300)).
                    until(ExpectedConditions.visibilityOf(pageTitle));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public WebElement getFirstRateableRide() {
        // sacekaj da se voznje ucitaju
        try {
            new WebDriverWait(driver, Duration.ofMillis(300)).
                    until(ExpectedConditions.presenceOfElementLocated(new By.ByCssSelector(".divide-y > div")));
        } catch (TimeoutException e) {
            return null;
        }

        List<WebElement> rows = driver.findElements(new By.ByCssSelector(".divide-y > div"));

        for (WebElement row : rows) {
            List<WebElement> allButtons = row.findElements(new By.ByCssSelector(".rate-ride-button"));

            for (WebElement btn : allButtons) {
                if (btn.getText().trim().equals("Rate")) {
                    String disabled = btn.getAttribute("disabled");
                    String classes = btn.getAttribute("class");

                    boolean isDisabled = disabled != null;
                    boolean hasOpacity = classes != null && classes.contains("opacity-50");

                    if (!isDisabled && !hasOpacity) {
                        return row;
                    }
                    break;
                }
            }
        }
        return null;
    }

    public void clickRateButtonForRide(WebElement rideRow) {
        List<WebElement> allButtons = rideRow.findElements(new By.ByCssSelector(".rate-ride-button"));

        for (WebElement btn : allButtons) {
            if (btn.getText().trim().equals("Rate")) {
                WebElement rateButton = new WebDriverWait(driver, Duration.ofMillis(300))
                        .until(ExpectedConditions.elementToBeClickable(btn));
                rateButton.click();
                break;
            }
        }
    }

    public WebElement getFirstAlreadyRatedRide() {
        try {
            new WebDriverWait(driver, Duration.ofMillis(300))
                    .until(ExpectedConditions.presenceOfElementLocated(new By.ByCssSelector(".divide-y > div")));
        } catch (TimeoutException e) {
            return null;
        }

        List<WebElement> rows = driver.findElements(new By.ByCssSelector(".divide-y > div"));

        for (WebElement row : rows) {
            List<WebElement> allButtons = row.findElements(new By.ByCssSelector(".rate-ride-button"));

            for (WebElement btn : allButtons) {
                if (btn.getText().trim().equals("Rate")) {
                    String disabled = btn.getAttribute("disabled");
                    String classes = btn.getAttribute("class");

                    boolean isDisabled = disabled != null;
                    boolean hasOpacity = classes != null && classes.contains("opacity-50");

                    if (isDisabled || hasOpacity) {
                        return row;
                    }
                    break;
                }
            }
        }
        return null;
    }

    public void tryClickRateButtonForRide(WebElement rideRow) {
        List<WebElement> allButtons = rideRow.findElements(new By.ByCssSelector(".rate-ride-button"));

        for (WebElement btn : allButtons) {
            if (btn.getText().trim().equals("Rate")) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                }
                break;
            }
        }
    }

    public void highlightElement(WebElement element, String color) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script =
                "var element = arguments[0];" +
                        "var originalStyle = element.getAttribute('style');" +
                        "element.setAttribute('style', 'border: 3px solid " + color + ";');" +
                        "setTimeout(function() {" +
                        "  element.setAttribute('style', originalStyle);" +
                        "}, 500);";
        js.executeScript(script, element);
    }
}