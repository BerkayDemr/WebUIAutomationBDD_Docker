package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

// A class using common methods.
public class BaseMethods {
    WebDriver driver;

    public BaseMethods(WebDriver driver) {
        this.driver = driver;
    }

    // Method that scrolls the related element.
    public void scrollToElement(By locator){
        //The scroll continues until the desired web element is displayed.
        WebElement webelement_locator = driver.findElement(locator);
        scrollToElement(webelement_locator);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Moves to the relevant element.
    public void moveToElement(By locator){
        WebElement element = driver.findElement(locator);
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    // Method that finds the web element.
    public WebElement find(By locator){
        return driver.findElement(locator);
    }

    // The method that performs the click operation.
    public void click(By locator, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void click(WebElement webElement, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.elementToBeClickable(webElement)).click();
    }

    // Text input method.
    public void type(By locator, String text){
        find(locator).sendKeys(text);
    }

    // Controls whether an element is displayed on the screen.
    public List<WebElement> isDisplayed(By locator){
        return driver.findElements(locator);
    }

    public void waitElementToBeClickable(By locator,int timeOut){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitElementToBeClickable(WebElement webElement,int timeOut){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public void waitElementToInvisible(By locator,int timeOut){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}