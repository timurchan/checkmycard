package com.gtimurchan.checkmycard.webpagescraper;

import com.gtimurchan.checkmycard.Const;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebPageScraper_ChromeDriver extends WebPageScraper_AbstractChromeDriver {
    String url;

    public WebPageScraper_ChromeDriver(String url) {
        this.url = url;
    }

    public Collection<String> execute() {

        driver.get(url);
        driver.manage().window().fullscreen();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(catalogSelector)));

        List<WebElement> catalogElements = driver.findElements(By.cssSelector(catalogSelector));
        System.out.println("catalogElements size + " + catalogElements.size());

        String htmlContent = driver.getPageSource();
        List<String> imageUrls = scrapImages2();

        int imageAmount = imageUrls.size();
        System.out.println("-----> initial image amount = " + imageAmount);

        System.out.println("-------------------------------------");

        boolean stopCalculation = false;
        int i = 0;
        do {
            scrollUp((driver));
            System.out.println("executed scrollUp # " + i);
            scrollDown(driver);
            System.out.println("executed scrollDown # " + i);

            final int imageAmountToCompare = imageAmount;

            try {
                wait = new WebDriverWait(driver, 3);
                wait.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        // Find all elements matching the CSS selector
                        List<WebElement> elements = driver.findElements(By.cssSelector(imgSelector));
                        System.out.println("elements size inside apply():  " + elements.size());

                        // Check if the count of elements exceeds 100
                        return elements.size() > imageAmountToCompare;
                    }
                });
            } catch (Exception e) {
                stopCalculation = true;
            }

            htmlContent = driver.getPageSource();
            imageUrls = scrapImages2();

            int prevImageAmount = imageAmount;
            imageAmount = imageUrls.size();
            System.out.println("-----> prev + " + prevImageAmount + " ->  new image amount = " + imageAmount);

            if (imageAmount <= prevImageAmount) {
                System.out.println("No more values. Stop cycle");
                break;
            }

            System.out.println("-------------------------------------");
            i++;
        } while (!stopCalculation);


        // Close the browser
        driver.quit();

        return imageUrls;
    }

    private List<String> scrapImages2() {
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