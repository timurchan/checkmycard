package com.gtimurchan.checkmycard;

import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.junit.Assert.assertTrue;

public class ChromeDriverTest2 {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Set path to ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", Const.PATH_TO_CHROME_DRIVER);

        // Initialize ChromeDriver
        driver = new ChromeDriver();
    }

    @Test
    public void testGoogleSearch() {
        // Navigate to the desired URL
        driver.get(Const.WEB_URL);

        // Wait for the dynamic content to load (adjust timeout as needed)
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#catalog")));

        // Get the updated HTML content
        String htmlContent = driver.getPageSource();

        // Parse the HTML content using Jsoup
        Document doc = Jsoup.parse(htmlContent);

        Elements elements = doc.select("div.product-card__img-wrap.img-plug.j-thumbnail-wrap");

        for (Element element : elements) {

            Elements thumbnailElements = element.select(".j-thumbnail");

            // Iterate over the found elements
            for (Element thumbnailElement : thumbnailElements) {
                // Extract the 'src' attribute value from each element
                String srcValue = thumbnailElement.attr("src");
                System.out.println("src value: " + srcValue);
            }

        }

        System.out.println("elements size {} + elements.size()");

    }

    @After
    public void tearDown() {
        // Quit the browser
        if (driver != null) {
            driver.quit();
        }
    }
}