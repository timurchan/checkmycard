package com.gtimurchan.checkmycard.webpagescraper;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;

import java.util.Collection;

public class WebPageScraper2_1 implements IWebPageScraper {
    private String URL;// = Const.WEB_URL;
    private static final String IMG_SELECTOR = "div.product-card__img-wrap > img.j-thumbnail";
    private static final int MAX_PAGES = 10;
    private static final int SCROLL_PAUSE_TIME = 5000; // milliseconds

    public WebPageScraper2_1(String url) {
        URL = url;
    }

    private void f() {

        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(true);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.setAjaxController(new NicelyResynchronizingAjaxController());

        try {
            HtmlPage page = client.getPage(URL);
            client.waitForBackgroundJavaScript(SCROLL_PAUSE_TIME);

            String pageXml = page.asXml();

            int pageCount = 0;
            while (pageCount < MAX_PAGES) {
               // List<HtmlElement> imageElements = page.getByXPath("//" + IMG_SELECTOR.replace(" ", "//").replace(">", "/"));
                //List<HtmlElement> imageElements = page.getByXPath("//div[@class='product- card__img-wrap']//img[contains(@class, 'j-thumbnail')]");
//
//                List<HtmlElement> imageElements = page.getByXPath("//div[contains(@class, 'product-card__img-wrap')]//img[contains(@class, 'j-thumbnail')]");
//
//                // Получаем URL-адреса изображений
//                List<String> imageUrls = imageElements.stream()
//                        .map(e -> e.getAttribute("src"))
//                        .collect(Collectors.toList());



                // JavaScript код для выполнения
                String jsCode = "var cssSelector = 'div.product-card__img-wrap > img.j-thumbnail';" +
                        "var elements = document.querySelectorAll(cssSelector);" +
                        "var imageSources = [];" +
                        "elements.forEach(function(element) {" +
                        "    imageSources.push(element.getAttribute('src'));" +
                        "});" +
                        "return imageSources;"; // Возвращаем массив imageSources

//                // Выполняем JavaScript код на странице
//                ScriptResult result = page.executeJavaScript(jsCode);
//                // Получаем результат выполнения JavaScript в виде строки
//                String jsResult = result.getJavaScriptResult().toString();
//                // Преобразуем результат в список строк
//                List<String> imageUrls = new ArrayList<>();
//                String[] srcArray = jsResult.replaceAll("\\[|\\]", "").split(",\\s*");
//                for (String src : srcArray) {
//                    imageUrls.add(src.trim());
//                }
//

                String script = "var elements = document.querySelectorAll('" + IMG_SELECTOR + "'); elements;";
                Object result = page.executeJavaScript(script).getJavaScriptResult();

                // Check the type of result
                if (result instanceof NodeList) {
                    NodeList nodeList = (NodeList) result;
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        DomElement element = (DomElement) nodeList.item(i);
                        // Process each element as needed
                        System.out.println("Element found: " + element.asXml());
                        System.out.println("Src attribute: " + element.getAttribute("src"));
                    }
                } else {
                    System.out.println("No elements found for the given CSS selector.");
                }

//                System.out.println("Page " + (pageCount + 1) + " - Found " + imageUrls.size() + " images");
//                imageUrls.forEach(System.out::println);

                // Scroll down
                page.executeJavaScript("window.scrollBy(0, document.body.scrollHeight)");
                client.waitForBackgroundJavaScript(SCROLL_PAUSE_TIME);
                pageCount++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    @Override
    public Collection<String> execute(String url) {
        f();
        return null;
    }
}