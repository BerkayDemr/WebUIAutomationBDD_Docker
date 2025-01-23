package pages;

import org.openqa.selenium.*;
import java.util.List;
import java.util.Objects;

public class ProductDetailPage extends BaseMethods {

    private static final By OTHER_SELLER_LOCATOR = By.cssSelector(".dWqPJgByuxvdJND4rSPT");
    private static final By SEE_ALL_SELLER_LOCATOR = By.cssSelector(".M6iJLUpgHKlEPzGcOggE");
    private static final By ADD_SHOPPING_CART_BUTTON_LOCATOR = By.cssSelector("button[data-test-id='addToCart']");
    private static final By SHOPPING_CART_LOCATOR = By.id("shoppingCart");
    private static final By GO_TO_SHOPPING_CART_LOCATOR = By.xpath("//button[.='Sepete git']");
    private static final By CONTINUE_SHOPPING_POPUP_LOCATOR = By.className("checkoutui-SalesFrontCash-UqBFhChdjCX02lFgPVLK");
    private static final By ALL_SELLER_LIST_LOCATOR = By.cssSelector(".hb-AxgMb");
    private final ProductPage productPage;
    private By goToSelectedProductButtonLocator;
    private String fullProductName;
    private String productDetailPageProductName;
    private WebElement productDetailPageProductNameWebElement;
    private WebElement ratingWebElement;
    private String ratingValueString;
    private float lowerRating = 10f;

    public ProductDetailPage(WebDriver driver, ProductPage productPage) {
        super(driver);
        this.productPage = productPage;
    }

    public void selectLowerRatingSellerAndAddToShoppingCart(){
        By sellerLocator;
        boolean isOtherSeller = false;

        switchToNewWindow();

        if (!isDisplayed(OTHER_SELLER_LOCATOR).isEmpty()){
            if(!isDisplayed(SEE_ALL_SELLER_LOCATOR).isEmpty()){
                isOtherSeller = true;
                System.out.println("Diğer satıcı ve tümünü göster butonu var.");
                click(SEE_ALL_SELLER_LOCATOR,5);
                goToSelectedProductButtonLocator = By.cssSelector("button[kind='primary']");
                sellerLocator = By.cssSelector(".VwUAvtsSpdiwukfc0VGp.IsAfBKbg4xH3kdMRzVZO.mnWNji9_P_vYbkjHXtoH");
                selectSellerInAllSeller(isOtherSeller, sellerLocator);
               }else{
                goToSelectedProductButtonLocator = By.cssSelector("button[kind='ghost']");
                sellerLocator = By.className("Xds3Q1iuhSmvOfzWbsbM");
                selectSellerInAllSeller(isOtherSeller, sellerLocator);
            }
        }else{
            System.out.println("Başka satıcı bulunamadı");
        }
        findProductName();
        addToBasket();
    }

    public void addToBasket(){
        waitElementToBeClickable(ADD_SHOPPING_CART_BUTTON_LOCATOR,20);
        scrollToElement(ADD_SHOPPING_CART_BUTTON_LOCATOR);
        click(ADD_SHOPPING_CART_BUTTON_LOCATOR,5);

        try{
            // The ‘Continue shopping’ element does not always appear;
            // if it does not, the exception is caught so that the test can continue.
            waitElementToBeClickable(CONTINUE_SHOPPING_POPUP_LOCATOR,20);
        } catch (TimeoutException e) {
            System.out.println("'Alışverişe devam et' elementi bulunamadı!");
        }

       if(!isDisplayed(CONTINUE_SHOPPING_POPUP_LOCATOR).isEmpty()){
           System.out.println("Alışverişe devam etmek istiyor musunuz sorgusu çıktı!");
           click(GO_TO_SHOPPING_CART_LOCATOR,5);
       }else{
           System.out.println("Alışverişe devam etmek istiyor musunuz sorgusu çıkmadı!");
           scrollToElement(SHOPPING_CART_LOCATOR);
           click(SHOPPING_CART_LOCATOR,5);
       }
    }

    private void switchToNewWindow() {
        String originalWindowHandle = productPage.getOriginalWindow();
        System.out.println("Original Window Handle: " + originalWindowHandle);

        // Detecting the newly opened tab and switching to that tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindowHandle.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    private void selectSellerInAllSeller(boolean isOtherSeller,By sellerLocator){
        float ratingValue;
        WebElement lowerRatingProductWebElement = null;

        if(!isOtherSeller){
            checkSelectedProductRating();
        }

        List<WebElement> sellerWebElementList = driver.findElements(sellerLocator);
            for(WebElement saleWebElement : sellerWebElementList){
                if(!saleWebElement.findElements(By.cssSelector("span[data-test-id='merchant-rating']")).isEmpty()) {
                    ratingWebElement = saleWebElement.findElement(By.cssSelector("span[data-test-id='merchant-rating']"));
                    ratingValueString = ratingWebElement.getText().replace(",",".");
                    ratingValue = Float.parseFloat(ratingValueString);

                    if(ratingValue < lowerRating){
                        lowerRating = ratingValue;
                        lowerRatingProductWebElement = saleWebElement.findElement(goToSelectedProductButtonLocator);
                    }
                }else{
                    System.out.println("Satıcı puanı bulunamadı!" + " - Satıcı adı = " + saleWebElement.findElement(By.cssSelector("a[data-test-id='merchant-name']")).getText());
                }
            }
            if (Objects.nonNull(lowerRatingProductWebElement)){
                scrollToElement(lowerRatingProductWebElement);
                click(lowerRatingProductWebElement,5);
                if(!isDisplayed(ALL_SELLER_LIST_LOCATOR).isEmpty()){
                    waitElementToInvisible(ALL_SELLER_LIST_LOCATOR,20);
                }
            }
    }

    private void checkSelectedProductRating(){
        WebElement selectedProductRatingWebElement = driver.findElement(By.className("xFmIt5Nn_nUpqAwhUTv9"));
        if(!selectedProductRatingWebElement.findElements(By.cssSelector("span[data-test-id='merchant-rating']")).isEmpty()){
            ratingWebElement = selectedProductRatingWebElement.findElement(By.cssSelector("span[data-test-id='merchant-rating']"));
            ratingValueString = ratingWebElement.getText().replace(",",".");
            lowerRating = Float.parseFloat(ratingValueString);
        }else{
                System.out.println("Ürün sayfasında seçilmiş ürünün satıcı puanı bulunamadı!" + " - Satıcı adı = " + selectedProductRatingWebElement.findElement(By.cssSelector("W5OUPzvBGtzo9IdLz4Li")).getDomAttribute("Title"));
        }
    }

    private void findProductName(){
        getProductFullName();
        getProductNameWithoutBrand();
    }

    public String getProductFullName(){
        if(Objects.isNull(fullProductName)){
            productDetailPageProductNameWebElement = driver.findElement(By.cssSelector(".xeL9CQ3JILmYoQPCgDcl"));
            fullProductName = productDetailPageProductNameWebElement.getText();
        }
        return fullProductName;
    }

    public String getProductNameWithoutBrand(){
        if(Objects.isNull(productDetailPageProductName)){
            WebElement productBrandNameWebElement = productDetailPageProductNameWebElement.findElement(By.tagName("a"));
            String productBrandName = productBrandNameWebElement.getText();
            productDetailPageProductName = fullProductName.replaceFirst(productBrandName, "").trim();
        }
        return productDetailPageProductName;
    }

}
