package com.gtimurchan.checkmycard.tmp;

import java.util.Collection;

public class HtmlGenerator {

    public static String generateHtmlPage(Collection<String> imageUrls) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Image URLs Table</title>");
        html.append("<style>");
        html.append("table { width: 100%; border-collapse: collapse; }");
        html.append("th, td { border: 1px solid black; padding: 8px; text-align: left; }");
        html.append("img { max-width: 100px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>Image URLs Table</h1>");
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Image</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        for (String imageUrl : imageUrls) {
            html.append("<tr>");
            html.append("<td><img src=\"" + imageUrl + "\" alt=\"Image\"></td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}