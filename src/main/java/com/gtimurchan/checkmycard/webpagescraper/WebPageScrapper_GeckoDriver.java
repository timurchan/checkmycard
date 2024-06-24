package com.gtimurchan.checkmycard.webpagescraper;

import com.gtimurchan.checkmycard.Const;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebPageScrapper_GeckoDriver implements IWebPageScraper {

    private WebDriver driver;
    final String imgSelector = "div.product-card__img-wrap > img.j-thumbnail";
    final String catalogSelector = "div#catalog";
    public WebPageScrapper_GeckoDriver()
    {
        System.setProperty("webdriver.gecko.driver", Const.PATH_TO_GECKO_DRIVER);
        // Initialize the Firefox driver
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        this.driver = new FirefoxDriver(options);

    }

    @Override
    public Collection<String> execute(String url) {
        // Open the web page
        driver.get(url);

        driver.manage().window().fullscreen();

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(30, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(catalogSelector)));

        List<WebElement> catalogElements = driver.findElements(By.cssSelector(catalogSelector));
        System.out.println("catalogElements size + " + catalogElements.size());

        List<String> imageUrls = scrapImages2();

        int imageAmount = imageUrls.size();
        System.out.println("-----> initial image amount = " + imageAmount);

        System.out.println("-------------------------------------");

        boolean stopCalculation = false;

        // Close the driver
        driver.quit();
        return imageUrls;
    }

    List<String> scrapImages2() {
        List<WebElement> elementsX = driver.findElements(By.cssSelector(imgSelector));
        System.out.println("elementsX size " + elementsX.size());

        List<String> imageUrls2 = new ArrayList<>();
        for (WebElement imgElement : elementsX) {
            String imageUrl = imgElement.getAttribute("src");
//            System.out.println("Image URL: " + imageUrl);
            imageUrls2.add(imageUrl);
        }
        return imageUrls2;
    }
}
