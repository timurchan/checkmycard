package com.gtimurchan.checkmycard.imageextractor;

import com.gtimurchan.checkmycard.webpagescraper.IWebPageScraper;
import com.gtimurchan.checkmycard.webpagescraper.WebPageScraper_ChromeDriver;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageExtractor extends AbstractImageExtractor {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ImageExtractor.class);

    public Collection<String> extractImageUrls(String url) throws IOException {
        Collection<String> imageUrls = new ArrayList<>();

//        IWebPageScraper pageScraper = new WebPageScraper_HtmlUnitDriver(url);
        IWebPageScraper pageScraper = new WebPageScraper_ChromeDriver(url);
        imageUrls = pageScraper.execute();


        LOG.debug("imageUrls {}", imageUrls);

        return imageUrls;
    }

    private String extractImageUrl(String html) {
        // Define regular expression pattern to extract image URLs
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public Collection<String> execute(String inputUrl, String searchString) {
        try {
            String url = inputUrl + searchString;
            LOG.debug("Processed url: {}", url);
            System.out.println("Processed url: " + url);

//            Collection<String> imageUrls = extractImageUrls(url);
            Collection<String> imageUrls = emulateImageExtracting();
            System.out.println("imageUrls: " + imageUrls);

            imageUrls = enrichWithCustomImage(imageUrls);
            System.out.println("imageUrls: " + imageUrls);

            return imageUrls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}