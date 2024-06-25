package com.gtimurchan.checkmycard.webpagescraper;

import com.gtimurchan.checkmycard.Const;
import com.gtimurchan.checkmycard.GeneralHelper;
import com.gtimurchan.checkmycard.ImageInfo;
import com.gtimurchan.checkmycard.ImageQueueManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebPageScraper_ChromeDriverAsync  extends WebPageScraper_AbstractChromeDriver {
    ImageQueueManager imageQueueManager;
    int counter = 1;
    String searchString;
    Collection<ImageInfo> totalImageInfos = new ArrayList<>();

    public WebPageScraper_ChromeDriverAsync(ImageQueueManager imageQueueManager, String searchString) {
        this.searchString = searchString;
        this.imageQueueManager = imageQueueManager;
    }

    private String getNextPageUrl() {
        String updatedUrl = GeneralHelper.replaceLabelWithValue(Const.WEB_URL_WITH_PAGE, Const.LABEL_TO_CHANGE, counter);
        updatedUrl += searchString;
        System.out.println("current searchString = " + updatedUrl);
        counter++;
        return updatedUrl;
    }


    @Override
    public Collection<String> execute() {
        for(int i = 0; i < 10; i ++) {
            executeOneStep();
        }

        // Close the browser
        driver.quit();

        return null; // TODO
    }

    public Collection<String> executeOneStep() {
        driver.get(getNextPageUrl());
        driver.manage().window().fullscreen();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(catalogSelector)));

        List<WebElement> catalogElements = driver.findElements(By.cssSelector(catalogSelector));
//        System.out.println("catalogElements size + " + catalogElements.size());

        Collection<ImageInfo> imageInfos = scrapImages();
        int imageAmount = imageInfos.size();
        System.out.println("-----> initial image amount = " + imageAmount);

//        System.out.println("-------------------------------------");

        boolean stopCalculation = false;
        int i = 0;
        do {
            scrollUp((driver));
//            System.out.println("executed scrollUp # " + i);
            scrollDown(driver);
//            System.out.println("executed scrollDown # " + i);

            final int imageAmountToCompare = imageAmount;

            try {
                wait = new WebDriverWait(driver, 2);
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

            imageInfos = scrapImages();

            int prevImageAmount = imageAmount;
            imageAmount = imageInfos.size();
//            System.out.println("-----> prev + " + prevImageAmount + " ->  new image amount = " + imageAmount);

            if (imageAmount <= prevImageAmount) {
                System.out.println("No more values. Stop cycle");
                break;
            }

//            System.out.println("-------------------------------------");
            i++;
        } while (!stopCalculation);

        return null; // TODO
    }

    protected Collection<ImageInfo> scrapImages() {
        String htmlContent = driver.getPageSource();

        List<ImageInfo> allImageInfosOnThePage = scrapImages3();
        System.out.println("allImageInfosOnThePage.sz = " + allImageInfosOnThePage.size());

        Collection<ImageInfo> deltaImageInfos = new ArrayList<>(allImageInfosOnThePage);
        deltaImageInfos.removeAll(totalImageInfos);
        System.out.println("deltaImageInfos.sz = " + deltaImageInfos.size());

        imageQueueManager.addElements(deltaImageInfos);

        totalImageInfos = allImageInfosOnThePage;
        return totalImageInfos;
    }

    protected List<ImageInfo> scrapImages3() {
        List<WebElement> elementsX = driver.findElements(By.cssSelector(imgSelector));
//        System.out.println("elementsX size " + elementsX.size());

        List<ImageInfo> imageInfos = new ArrayList<>();
        for (WebElement imgElement : elementsX) {
            String imageUrl = imgElement.getAttribute("src");
//            System.out.println("Image URL: " + imageUrl);
            imageInfos.add(new ImageInfo(imageUrl));
        }
        return imageInfos;
    }
}
