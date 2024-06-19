package com.gtimurchan.checkmycard.webpagescraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebPageScraper_HtmlUnitDriver implements IWebPageScraper {

    String url;
    final String catalogSelector = "#mainContainer";
    final String imgSelector = "div.product-card__img-wrap > img.j-thumbnail";


    public WebPageScraper_HtmlUnitDriver(String url) {
        this.url = url;
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



    @Override
    public Collection<String> execute() {
        // Инициализация HtmlUnitDriver
        WebDriver driver = new HtmlUnitDriver();

        // Открытие веб-страницы
        driver.get(url);

        // Явное ожидание загрузки элемента
        WebDriverWait wait = new WebDriverWait(driver, 30);
        String str = catalogSelector;

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(catalogSelector)));

        List<WebElement> catalogElements = driver.findElements(By.cssSelector(catalogSelector));
        System.out.println("catalogElements size + " + catalogElements.size());

        String htmlContent = driver.getPageSource();
    //        List<String> imageUrls = scrapData(htmlContent);
    //        System.out.println("imageUrls size " + imageUrls.size());
    //
    //        List<WebElement> elements = driver.findElements(By.cssSelector(imgSelector));
    //        System.out.println("elements size " + elements.size());
        // Получение заголовка страницы

        List<String> imageUrls = scrapData(htmlContent);
        System.out.println("imageUrls size " + imageUrls.size());

        List<WebElement> elements = driver.findElements(By.cssSelector(imgSelector));
        System.out.println("elements size " + elements.size());

        int imageAmount = elements.size();
        System.out.println("-----> image amount = " + imageAmount);


        driver.quit();

        return null;
    }

}