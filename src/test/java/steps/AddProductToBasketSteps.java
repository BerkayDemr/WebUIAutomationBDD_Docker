package steps;

import config.TestConfig;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.springframework.test.context.ContextConfiguration;
import pages.*;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import utils.ScreenshotUtils;

@ContextConfiguration(classes = {TestConfig.class}) // Spring bağlamını tanımlıyoruz
public class AddProductToBasketSteps {

    private final MainPage mainPage;
    private final LoginPage loginPage;
    private final ProductPage productPage;
    private final ProductDetailPage productDetailPage;
    private final ShoppingCartPage shoppingCartPage;

    private final WebDriver driver;

    @Autowired
    public AddProductToBasketSteps(WebDriver driver) {
        this.driver = driver;
        System.out.println("Kullanılan Tarayıcı: " + driver);
        this.mainPage = new MainPage(driver);
        this.loginPage = new LoginPage(driver);
        this.productPage = new ProductPage(driver);
        this.productDetailPage = new ProductDetailPage(driver, productPage);
        this.shoppingCartPage = new ShoppingCartPage(driver);
    }
    // @AfterStep annotation is used to check if the scenario fails after each step.
    // When this method is used, the afterstep step is added between the cucumber test steps in the allure test report.
    // If this is not desired in the test report, the @AfterStep annotation should be removed and the method should be called manually at each step.
    // If the manual invocation method is used, the next code will not run when the test fails.
    // each step should be taken to the try-catch block. This can be organised according to the project expectation.
    @AfterStep
    public void takeScreenshotIfStepFail(Scenario scenario) {
        if (scenario.isFailed()) {
            String scenarioNameWithoutWhiteSpace = scenario.getName().replaceAll("\\s+", "_");
            ScreenshotUtils.takeScreenshot(driver, scenario, scenarioNameWithoutWhiteSpace);
        }
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        mainPage.openUserLoginPage();
    }

    @When("the user logs in")
    public void theUserLogsIn() {
        loginPage.userLogin();
    }

    @And("the user searches for {string}")
    public void theUserSearchesFor(String productName) {
        mainPage.searchProduct(productName);
    }

    @And("the user filters products with price range {string} to {string}")
    public void theUserFiltersProducts(String minPrice, String maxPrice) {
        productPage.filterProduct(minPrice, maxPrice);
    }

    @And("the user selects a random product")
    public void theUserSelectsARandomProduct() {
        productPage.selectRandomProduct();
    }

    @And("the user adds the product of the lowest-rated seller to the basket")
    public void theUserAddsTheProductOfTheLowestRatedSellerToTheBasket() {
        productDetailPage.selectLowerRatingSellerAndAddToShoppingCart();
    }

    @Then("the basket should contain the selected product")
    public void theBasketShouldContainTheSelectedProduct() {
        boolean isProductValid = shoppingCartPage.getProductSalesName().equals(productDetailPage.getProductNameWithoutBrand())
                || shoppingCartPage.getProductSalesName().equals(productDetailPage.getProductFullName());
        assertTrue(isProductValid, "Selected product is not found in the basket!");
    }

    @And("the user clears the basket")
    public void theUserClearsTheBasket() {
        shoppingCartPage.clearToBasket();
    }
}
