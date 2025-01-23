package utils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotUtils {

    public static void takeScreenshot(WebDriver driver, Scenario scenario, String testName) {
        if (!(driver instanceof TakesScreenshot)) {
            System.err.println("Driver ekran görüntüsü almayı desteklemiyor.");
            return;
        }

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            // Saving a screenshot to a file
            String timestamp = DateTimeUtils.getCurrentTimestamp();
            Path destination = Path.of("screenshots", testName + "_" + timestamp + ".png");
            Files.createDirectories(destination.getParent());
            Files.copy(screenshot.toPath(), destination);
            System.out.println("Ekran görüntüsü kaydedildi: " + destination.toAbsolutePath());

            // Convert screenshot to byte array
            byte[] screenshotBytes = Files.readAllBytes(screenshot.toPath());

            // Add to Allure report
            attachToAllure(testName, screenshotBytes);


        } catch (IOException e) {
            System.err.println("Ekran görüntüsü kaydedilirken hata oluştu.");
            e.printStackTrace();
        }
    }

    private static void attachToAllure(String testName, byte[] screenshotBytes) {
        Allure.getLifecycle().addAttachment(
                "Screenshot - " + testName,
                "image/png",
                "png",
                screenshotBytes
        );
    }
}
