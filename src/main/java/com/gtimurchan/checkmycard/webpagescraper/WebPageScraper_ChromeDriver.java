package com.gtimurchan.checkmycard.webpagescraper;

import com.gtimurchan.checkmycard.Const;
import com.gtimurchan.checkmycard.ImageExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebPageScraper_ChromeDriver implements IWebPageScraper {
    WebDriver driver;
    String url;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ImageExtractor.class);

    final String catalogSelector = "div#catalog";
    final String imgSelector = "div.product-card__img-wrap > img.j-thumbnail";

    @Value("${chrome_driver.path}")
    private String chromeDriverPath;

    @Autowired
    private Environment environment;

    public WebPageScraper_ChromeDriver() {
        //this.url = url;
        System.setProperty("webdriver.chrome.driver", Const.PATH_TO_CHROME_DRIVER);
        String allowedIps;

        try {
            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingAnyFreePort()
                    .withWhitelistedIps("192.168.1.2")
                    .build();

            InetAddress address = InetAddress.getLocalHost();
            LOG.debug("My IP-address: " + address.getHostAddress());
            allowedIps = "--allowed-ips=" + address.getHostAddress();
            LOG.debug("Allowed IPs option: {}", allowedIps);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Run Chrome in headless mode
            options.addArguments("--disable-gpu"); // applicable to windows os only
            options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
            options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
            //options.addArguments(allowedIps);
            driver = new ChromeDriver(service, options);
        } catch (Exception e) {
            LOG.debug("Error ---> {}", e.getMessage());
        }
    }

    private List<String> scrapData(String htmlContent) {
        List<String> imageUrls = new ArrayList<>();

        // Parse the HTML content using Jsoup
        Document doc = Jsoup.parse(htmlContent);

        Elements elements = doc.select(imgSelector);

        for (Element element : elements) {

            Elements thumbnailElements = element.select(".j-thumbnail");

            // Iterate over the found elements
            for (Element thumbnailElement : thumbnailElements) {
                // Extract the 'src' attribute value from each element
                String srcValue = thumbnailElement.attr("src");
//                System.out.println("src value: " + srcValue);
                imageUrls.add(srcValue);
            }
        }

        return imageUrls;
    }

    private static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, document.body.scrollHeight)");
    }

    public static void scrollUp(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -document.body.scrollHeight)");
    }

    public Collection<String> execute(String url) {

        driver.get(url);
        driver.manage().window().fullscreen();

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(30, ChronoUnit.SECONDS));
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
                wait = new WebDriverWait(driver, Duration.of(3, ChronoUnit.SECONDS));
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