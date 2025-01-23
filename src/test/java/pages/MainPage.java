package pages;

import org.openqa.selenium.*;

public class MainPage extends BaseMethods {

    private static final By MY_ACCOUNT_BUTTON_LOCATOR    = By.id("myAccount");
    private static final By LOGIN_BUTTON_LOCATOR         = By.id("login");
    private static final By SEARCH_TYPE_LOCATOR          = By.cssSelector(".initialComponent-hk7c_9tvgJ8ELzRuGJwC");
    private static final By COOKIE_ACCEPT_BUTTON_LOCATOR = By.id("onetrust-accept-btn-handler");
    private static final By TYPING_SEARCH_BUTTON_LOCATOR = By.cssSelector("[placeholder='Ürün, kategori veya marka ara']");
    private static final By TRENDING_SEARCH_LOCATOR = By.cssSelector(".trendingTerms-jDeLg38IzFbnmV_tVWsg");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void openUserLoginPage(){
        acceptCookieIfExist();
        moveToMyAccountButton();
        clickLoginButton();

    }

    public void searchProduct(String searchTerm){
        if(!isDisplayed(SEARCH_TYPE_LOCATOR).isEmpty()){
            waitElementToBeClickable(SEARCH_TYPE_LOCATOR,20);
            System.out.println("Arama elementi sayfada görünür durumda");
            click(SEARCH_TYPE_LOCATOR,10);
            waitElementToBeClickable(TRENDING_SEARCH_LOCATOR,20);
            type(TYPING_SEARCH_BUTTON_LOCATOR,searchTerm + Keys.ENTER);
        }else{
            System.out.println("Arama elementi sayfada görünür durumda değil");
        }
    }

    private void moveToMyAccountButton(){
        waitElementToBeClickable(MY_ACCOUNT_BUTTON_LOCATOR,20);
        moveToElement(MY_ACCOUNT_BUTTON_LOCATOR);
    }

    private void clickLoginButton(){
        click(LOGIN_BUTTON_LOCATOR,5);
    }

    private void acceptCookieIfExist(){
        if(!isDisplayed(COOKIE_ACCEPT_BUTTON_LOCATOR).isEmpty()){
            click(COOKIE_ACCEPT_BUTTON_LOCATOR,5);
        }else{
            System.out.println("Cookie sorgusu çıkmadı!");
        }
    }
}
