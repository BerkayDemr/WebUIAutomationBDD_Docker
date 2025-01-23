package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BaseMethods {

private static final By USER_NAME_LOCATOR = By.id("txtUserName");
private static final By USER_PASSWORD_LOCATOR = By.id("txtPassword");
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    public void userLogin(){
        typeMailInfo();
        typePassword();
    }

    private void typeMailInfo(){
        type(USER_NAME_LOCATOR,"type_your_mail");
    }

    private void typePassword(){
        type(USER_PASSWORD_LOCATOR,"type_your_password"+ Keys.ENTER);
    }
}
