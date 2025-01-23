package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShoppingCartPage extends BaseMethods {

    private static final By SHOPPING_CART_PAGE_PRODUCT_NAME_LOCATOR = By.cssSelector(".product_name_2Klj3 > a");
    private static final By DELETE_PRODUCT_LOCATOR = By.cssSelector(".delete_button_1lHhf");
    private static final By DELETE_BUTTON_POPUP_LOCATOR = By.xpath("//button[text()='Sil']");

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    private void isDisplayedDeletePopUp(){
        if(!isDisplayed(DELETE_BUTTON_POPUP_LOCATOR).isEmpty()){
            click(DELETE_BUTTON_POPUP_LOCATOR,5);
            System.out.println("Silme aksiyonu sonrası emin misiniz sorgusu çıktı.");
        }else{
            System.out.println("Silme aksiyonu sonrası emin misiniz sorgusu çıkmadı.");
        }
    }

    public String getProductSalesName(){
        waitElementToBeClickable(SHOPPING_CART_PAGE_PRODUCT_NAME_LOCATOR,20);
        return driver.findElement(SHOPPING_CART_PAGE_PRODUCT_NAME_LOCATOR).getText();
    }

    public void clearToBasket(){
        click(DELETE_PRODUCT_LOCATOR,5);
        isDisplayedDeletePopUp();
        WebElement emptyBasketWebElement = driver.findElement(By.className("content_Z9h8v")).findElement((By.tagName("h1")));
        waitElementToBeClickable(emptyBasketWebElement,20);
        System.out.println("Sepet durumu = " + emptyBasketWebElement.getText());
    }
}
