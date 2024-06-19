package com.gtimurchan.checkmycard.webpagescraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebPageScraper2 implements IWebPageScraper {
    WebClient client;
    String url;

    final String catalogSelector = "div#catalog";
    final String imgSelector = "div.product-card__img-wrap > img.j-thumbnail";

    public WebPageScraper2(String url) {
        this.url = url;
        this.client = new WebClient(BrowserVersion.CHROME);
        this.client.getOptions().setJavaScriptEnabled(true); // Enable JavaScript execution
//        this.client.getOptions().setCssEnabled(true); // Enable CSS
        this.client.getOptions().setThrowExceptionOnScriptError(false); // Ignore JavaScript errors
        this.client.setJavaScriptErrorListener(new JavaScriptErrorListener() {
            @Override
            public void scriptException(HtmlPage page, ScriptException scriptException) {
                // Handle script exceptions here (optional)
                System.err.println("Script Exception: " + scriptException.getMessage());
            }

            @Override
            public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {
                // Handle script timeout errors here (optional)
                System.err.println("Script Timeout Error: Page load timeout exceeded");
            }

            @Override
            public void malformedScriptURL(HtmlPage page, String url, MalformedURLException malformedURLException) {
                // Handle malformed script URL errors here (optional)
                System.err.println("Malformed Script URL Error: " + malformedURLException.getMessage());
            }

            @Override
            public void loadScriptError(HtmlPage page, java.net.URL scriptUrl, Exception exception) {
                // Handle general script loading errors here (optional)
                System.err.println("Script Loading Error: " + exception.getMessage());
            }

            @Override
            public void warn(String s, String s1, int i, String s2, int i1) {

            }
        });
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
                imageUrls.add(srcValue);
            }
        }

        return imageUrls;
    }


    // Wait until an element with the given CSS selector becomes visible
    public HtmlElement waitUntilElementVisible(String cssSelector, long timeoutMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        HtmlPage currentPage = null; //(HtmlPage) client.getCurrentWindow().getEnclosedPage();
        while (currentPage == null && System.currentTimeMillis() - startTime < timeoutMillis) {
            currentPage = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
            if (currentPage == null) {
                // Wait briefly before trying again
                Thread.sleep(500); // Adjust sleep duration as needed
                System.out.println("waited 500 ms !!");
            }
        }

        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            currentPage = (HtmlPage) client.getCurrentWindow().getEnclosedPage();

            String htmlContent = currentPage.asXml();

            HtmlElement element = currentPage.querySelector(cssSelector);

            if (element != null && element.isDisplayed()) {
                return element;
            }

            Thread.sleep(500); // Wait for a short interval before checking again
            System.out.println("waited 500 ms");
        }

        throw new RuntimeException("Timeout waiting for element with CSS selector: " + cssSelector);
    }

    public Collection<String> execute() {
        Collection<String> imageUrls = new ArrayList<>();

        try {
            HtmlPage page = client.getPage(url);
            // Wait for the catalog section to be visible (if necessary)
            client.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute
            System.out.println("after 1-st wait");
            waitUntilElementVisible(catalogSelector, 10000);
            System.out.println("after waitUntilElementVisible");
            // Fetch the web page

            page = client.getPage(url);


            // Get the updated HTML content
            String htmlContent = page.asXml();

            // Scrape image URLs
            imageUrls.addAll(scrapData(htmlContent));

            System.out.println("imageUrls size " + imageUrls.size());

            List<HtmlElement> elements = page.getByXPath(imgSelector);
//            List<WebElement> elements = driver.findElements(By.cssSelector(imgSelector));
            System.out.println("elements size " + elements.size());

            int imageAmount = elements.size();
            System.out.println("-----> image amount = " + imageAmount);

            System.out.println("-------------------------------------");


            // Simulate scrolling and fetching data multiple times (e.g., 5 pages)
            for (int i = 0; i < 20; i++) {


                System.out.println("Iteration # " + i);

                // Scroll down to trigger AJAX load
                scrollDown(page);
                System.out.println("Iteration # " + i + " after scrollDown()");

                Thread.sleep(10000); // Adjust as needed
                System.out.println("Iteration # " + i + " after timeout");

                final int imageAmountToCompare = imageAmount;

                page = client.getPage(url);

                // Get the updated HTML content
                htmlContent = page.asXml();

                // Scrape image URLs again
                imageUrls = scrapData(htmlContent);
                System.out.println("imageUrls size " + imageUrls.size());

                List<HtmlElement> elementsX = page.getByXPath(imgSelector);
//                List<WebElement> elementsX = driver.findElements(By.cssSelector(imgSelector));
                System.out.println("elementsX size " + elementsX.size());

                int prevImageAmount = imageAmount;
                imageAmount = elementsX.size();
                System.out.println("-----> prev + " + prevImageAmount + " ->  new image amount = " + imageAmount);

                if (imageAmount <= prevImageAmount) {
                    System.out.println("No more values. Stop cucle");
                    break;
                }


                System.out.println("-------------------------------------");


                System.out.println("Iteration # " + i);

                // Scroll down to trigger AJAX load (if needed, HtmlUnit may handle this internally)
                scrollDown(page);

                // Optionally wait for new content to load
                Thread.sleep(10000); // Adjust as needed

                // Re-fetch the page
                page = client.getPage(url);

                // Get the updated HTML content
                htmlContent = page.asXml();

                // Scrape image URLs again
                Collection<String> newImageUrls = scrapData(htmlContent);

                // Check if there are new image URLs
                if (newImageUrls.isEmpty()) {
                    System.out.println("No more values. Stop cycle");
                    break;
                }

                imageUrls.addAll(newImageUrls);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the WebClient
            client.close();
        }

        return imageUrls;
    }

    private void scrollDown(HtmlPage page) {
        // Scroll down to the bottom of the page using JavaScript
        page.executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
    }
}