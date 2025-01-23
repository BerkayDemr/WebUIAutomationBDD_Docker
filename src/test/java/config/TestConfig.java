package config;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TestConfig {

    private WebDriver driver;

    @Bean
    @Scope("prototype")
    public WebDriver webDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        String gridUrl = "http://172.19.0.2:4444/wd/hub"; // Selenium grid hub URL

        try {
            URI gridURI = new URI(gridUrl);

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();

                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    // Popup blocking
                    chromeOptions.addArguments("--disable-popup-blocking");
                    chromeOptions.addArguments("--disable-infobars");
                    chromeOptions.addArguments("--disable-notifications");
                    // Blocking certificate security errors
                    chromeOptions.addArguments("--ignore-certificate-errors");
                    chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    // Accept invalid certificates
                    chromeOptions.setAcceptInsecureCerts(true);
                    // Saving password and switching off automatic logins
                    chromeOptions.setExperimentalOption("prefs", Map.of(
                            "credentials_enable_service", false,
                            "profile.password_manager_enabled", false
                    ));

                    if (System.getProperty("grid") != null && System.getProperty("grid").equals("true")) {
                        driver = new RemoteWebDriver(gridURI.toURL(), chromeOptions);
                    } else {
                        driver = new ChromeDriver(chromeOptions);
                    }
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();

                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    // Accept invalid certificates
                    firefoxOptions.setAcceptInsecureCerts(true);

                    FirefoxProfile firefoxProfile = new FirefoxProfile();
                    // Popup blocking
                    firefoxProfile.setPreference("dom.disable_open_during_load", true);
                    // Saving password and switching off automatic logins
                    firefoxProfile.setPreference("signon.rememberSignons", false);
                    firefoxProfile.setPreference("signon.autologin.proxy", false);
                    // Blocking SSL security errors
                    firefoxProfile.setPreference("security.cert_pinning.enforcement_level", 0);
                    firefoxProfile.setPreference("security.ssl.enable_ocsp_stapling", false);
                    firefoxProfile.setPreference("browser.startup.homepage_override.mstone", "ignore");
                    firefoxProfile.setPreference("security.enterprise_roots.enabled", true);
                    firefoxProfile.setPreference("privacy.trackingprotection.enabled", false);

                    firefoxOptions.setProfile(firefoxProfile);

                    if (System.getProperty("grid") != null && System.getProperty("grid").equals("true")) {
                        driver = new RemoteWebDriver(gridURI.toURL(), firefoxOptions);
                    } else {
                        driver = new FirefoxDriver(firefoxOptions);
                    }
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();

                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    // Popup blocking
                    edgeOptions.addArguments("--disable-popup-blocking");
                    edgeOptions.addArguments("--disable-notifications");
                    edgeOptions.addArguments("--disable-infobars");
                    // Blocking certificate security errors
                    edgeOptions.addArguments("--ignore-certificate-errors");
                    edgeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    // Accept invalid certificates
                    edgeOptions.setAcceptInsecureCerts(true);

                    // Saving password and switching off automatic logins
                    Map<String, Object> edgePrefs = new HashMap<>();
                    edgePrefs.put("credentials_enable_service", false);
                    edgePrefs.put("profile.password_manager_enabled", false);
                    edgeOptions.setExperimentalOption("prefs", edgePrefs);

                    if (System.getProperty("grid") != null && System.getProperty("grid").equals("true")) {
                        driver = new RemoteWebDriver(gridURI.toURL(), edgeOptions);
                    } else {
                        driver = new EdgeDriver(edgeOptions);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Geçersiz tarayıcı seçimi: " + browser);
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            driver.manage().window().maximize();
            driver.get("https://www.hepsiburada.com/");
        } catch (Exception e) {
            throw new RuntimeException("WebDriver başlatılırken bir hata oluştu: " + e.getMessage(), e);
        }

        return driver;
    }

    @PreDestroy
    public void tearDown() {
        if (driver != null) {
            System.out.println("Driver kapatılıyor...");
            driver.quit();
        }
    }
}
