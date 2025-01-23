package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductPage extends BaseMethods{
    private static final By MINIMUM_PRICE_LOCATOR                    = By.cssSelector("[placeholder='En az']");
    private static final By MAXIMUM_PRICE_LOCATOR                    = By.cssSelector("[placeholder='En çok']");
    private static final By SEARCH_BUTTON_LOCATOR                    = By.cssSelector(".moria-Button-jKYKyx");
    private static final By FILTER_RESULT_COUNT_LOCATOR              = By.xpath("//b[@class='searchResultSummaryBar-AVnHBWRNB0_veFy34hco']");
    private static final By FILTER_PRICE_DISTANCE_AREA_CLEAR_LOCATOR = By.cssSelector(".FacetList-KWeEo5_VbjJlDbQbWZgv");
    private static final By LOAD_MORE_PRODUCT_BUTTON_LOCATOR         = By.cssSelector(".paginatorStyle-zIpoq1X_Q3UzNyjPnUX1");
    private static final By MOBILE_PHONES_FILTER_BUTTON_LOCATOR      = By.cssSelector(".tree-MKK_VYGEQxGO236zUzZw > div:nth-of-type(2) .seoAnchorLink-nCW0yP4qoVI_AhEjVAY_");
    private static final By PRODUCT_COUNT_LOCATOR                    = By.cssSelector(".paginatorStyle-OQbSBb_oQve3e_k9LEg5");
    private static final By PRICE_FILTER_TITLE_LOCATOR               = By.id("fiyat");
    private static final By PRICE_FILTER_COLLAPSE_ICON_LOCATOR       = By.cssSelector("#fiyat [data-test-id='collapse-icon-closed']");
    private static final double PRODUCT_COUNT_IN_ONE_PAGE = 36;
    private String originalWindow;
    private int searchResultPageCount;
    private int lastProductID;
    private int searchResultCount;
    
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getOriginalWindow() {
        return originalWindow;
    }

    private int extractProductCount(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);

        int lastNumber = -1;
        while (matcher.find()) {
            lastNumber = Integer.parseInt(matcher.group());
        }
        if (lastNumber == -1) throw new IllegalArgumentException("Metinde sayı bulunamadı!");
        return lastNumber;
    }

    private void goToLastProductsLineInMultiplePages(double searchResultPageCount){
        for (int i=1; i<searchResultPageCount;i++){
            scrollToElement(By.cssSelector(".productListContent-pXUkO4iHa51o_17CBibU > ul:nth-of-type("+(i)+") > li:nth-of-type(36)"));
            try{
                //Wait for the Show more products button
                waitElementToBeClickable(LOAD_MORE_PRODUCT_BUTTON_LOCATOR,10);
                click(LOAD_MORE_PRODUCT_BUTTON_LOCATOR,5);
                System.out.println("Daha fazla ürün göster butonu çıktı. "+i+".deneme");
            }catch (TimeoutException e){
                System.out.println("Daha fazla ürün göster butonu çıkmadı. Lazy loading'e devam edilecek! "+i+".deneme");
            }
            waitElementToBeClickable(By.cssSelector(".productListContent-pXUkO4iHa51o_17CBibU > ul:nth-of-type("+(i+1)+") > li:nth-of-type(1)"),20);
        }
    }

    private void checkProductPagesCount(){
        double searchResultPageCountDouble = searchResultCount / PRODUCT_COUNT_IN_ONE_PAGE;

        if(searchResultPageCountDouble != Math.floor(searchResultPageCountDouble)){
            searchResultPageCount = (int)(Math.ceil(searchResultPageCountDouble));
        }

        lastProductID = searchResultCount - 1;
    }
    private void selectRandomProductFromLastLine(){
        List<Integer> lastRowProductsIDs =  new ArrayList<>();
        int lastRowProductsCount;
        int randomSelectedIndex;

        System.out.println("Son satırdaki ürünler bulunup, rastgele bir tanesi seçilecek");
        lastRowProductsCount = searchResultCount % 4;

        if(lastRowProductsCount == 0){
            lastRowProductsCount = 4;
        }

        for(int i=0;i<lastRowProductsCount;i++){
            lastRowProductsIDs.add(lastProductID);
            lastProductID--;
        }

        Random random = new Random();
        randomSelectedIndex = random.nextInt(lastRowProductsIDs.size());
        System.out.println("Son satırda seçilen ürün ID'si = " + lastRowProductsIDs.get(randomSelectedIndex));
        String selectedProductID = lastRowProductsIDs.get(randomSelectedIndex).toString();

        scrollToElement(By.id("i" + selectedProductID));
        click(By.id("i" + selectedProductID),5);
    }
    // Expanding the price filtering area.
    private void expandPriceFilterArea(){
        scrollToElement(PRICE_FILTER_TITLE_LOCATOR);
        click(PRICE_FILTER_COLLAPSE_ICON_LOCATOR,5);
    }

    public void filterProduct(String minPrice, String maxPrice){
        if(!isDisplayed(MOBILE_PHONES_FILTER_BUTTON_LOCATOR).isEmpty()){
            click(MOBILE_PHONES_FILTER_BUTTON_LOCATOR,5);
        }

        // Check if the price filtering field is closed.
        if(!isDisplayed(PRICE_FILTER_COLLAPSE_ICON_LOCATOR).isEmpty()){
            expandPriceFilterArea();
        }

        waitElementToBeClickable(MINIMUM_PRICE_LOCATOR,20);
        scrollToElement(MINIMUM_PRICE_LOCATOR);
        type(MINIMUM_PRICE_LOCATOR,minPrice);
        type(MAXIMUM_PRICE_LOCATOR,maxPrice);
        click(SEARCH_BUTTON_LOCATOR,5);

    }

    public void selectRandomProduct(){
        originalWindow = driver.getWindowHandle();

        // Check if the price filtering field is closed.
        if(!isDisplayed(PRICE_FILTER_COLLAPSE_ICON_LOCATOR).isEmpty()){
            expandPriceFilterArea();
        }

        waitElementToBeClickable(FILTER_PRICE_DISTANCE_AREA_CLEAR_LOCATOR,20);
        String text = driver.findElement(PRODUCT_COUNT_LOCATOR).getText();
        searchResultCount = extractProductCount(text);
        // Number of products written on the page as a result of filtering.
        WebElement filterResultCountElement = driver.findElement(FILTER_RESULT_COUNT_LOCATOR);
        String searchResultCountAfterFilter = filterResultCountElement.getText();

        if(searchResultCount == 0 ){
            System.out.println("Filtreleme sonucu hiç bir kayıt bulunamadı!");
            return;
        }
        checkProductPagesCount();

        if(searchResultPageCount > 1){
            // In some cases, the number of pieces shown at the bottom of the page and the number of pieces shown at the top of the page may be different.
            // This is an output added to observe this situation.
            System.out.println("Filtreleme sonucu bulunan ürün adetleri --> Sayfa altında gösterilen = " + searchResultCount + " - Filtreleme sonucunda gösterilen = " + searchResultCountAfterFilter);

            goToLastProductsLineInMultiplePages(searchResultPageCount);
        }
       selectRandomProductFromLastLine();
    }
}
