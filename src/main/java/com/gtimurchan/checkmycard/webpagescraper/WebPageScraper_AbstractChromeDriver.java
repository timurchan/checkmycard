package com.gtimurchan.checkmycard.webpagescraper;

import com.gtimurchan.checkmycard.Const;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

abstract public class WebPageScraper_AbstractChromeDriver implements IWebPageScraper {
    WebDriver driver;

    final String catalogSelector = "div#catalog";
    final String imgSelector = "div.product-card__img-wrap > img.j-thumbnail";

    public WebPageScraper_AbstractChromeDriver() {
        System.setProperty("webdriver.chrome.driver", Const.PATH_TO_CHROME_DRIVER);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        driver = new ChromeDriver(options);
    }


    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, document.body.scrollHeight)");
    }

    public static void scrollUp(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -document.body.scrollHeight)");
    }

}
