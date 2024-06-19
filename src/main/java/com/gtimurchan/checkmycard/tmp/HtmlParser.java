package com.gtimurchan.checkmycard.tmp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public List<String> extractImageUrls(Document document) {
        List<String> imageUrls = new ArrayList<>();

        // Select all <img> elements with the class "j-thumbnail" inside <div> elements with the class "product-card__wrapper"
        Elements elements = document.select(".product-card__wrapper .j-thumbnail");

        // Extract image URLs from the selected elements
        for (Element element : elements) {
            String imageUrl = element.attr("src");
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    public static void execute() {
//        String htmlContent = "<div class=\"product-card__wrapper\">" +
//                "    <div class=\"product-card__top-wrap\">" +
//                "        <div class=\"product-card__img-wrap img-plug j-thumbnail-wrap\">" +
//                "            <img class=\"j-thumbnail\" width=\"516\" height=\"688\" src=\"https://basket-10.wbbasket.ru/vol1450/part145073/145073152/images/c516x688/1.webp\" alt=\"Серьги длинные медицинское золото SLOVALIO\">" +
//                "        </div>" +
//                "    </div>" +
//                "</div>";
//
//        HtmlParser parser = new HtmlParser();
//        List<String> imageUrls = parser.extractImageUrls(htmlContent);
//        for (String imageUrl : imageUrls) {
//            System.out.println(imageUrl);
//        }
    }
}