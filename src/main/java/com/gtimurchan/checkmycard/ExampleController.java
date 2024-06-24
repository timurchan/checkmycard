package com.gtimurchan.checkmycard;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class ExampleController {
    int counter = 1;
    String searchString;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ImageExtractor.class);

    // Display the form at the root level
    @GetMapping("/")
    public String showInitialForm() {
        return "index"; // Ensure this matches the Thymeleaf template name (index.html)
    }

    // Handle form submission
    @PostMapping("/submit-request")
    public String handleRequest(@RequestParam("searchString") String searchString, Model model) {
        LOG.debug("Received request string: {}", searchString);
        this.searchString = searchString;

//        Collection<String> imageUrls = new ImageExtractor().execute(searchString);
//        model.addAttribute("imageUrls", imageUrls);

        // Return the view name to display the results
        return "test2v2";
        //return "resultTableY";
    }

    @GetMapping("/load-more-images")
    @ResponseBody
    public List<String> loadMoreImages(@RequestParam int offset, @RequestParam int limit) {
        String updatedUrl = replaceLabelWithValue(Const.WEB_URL_WITH_PAGE, Const.LABEL_TO_CHANGE, counter);
        System.out.println("updatedUrl = " + updatedUrl);
        counter++;


        Collection<String> imageUrls = new ImageExtractor().execute(updatedUrl, searchString);
        List<String> listUrls = new ArrayList<>(imageUrls);
        return listUrls;
    }

    public static String replaceLabelWithValue(String url, String label, int value) {
        return url.replace(label, String.valueOf(value));
    }
}