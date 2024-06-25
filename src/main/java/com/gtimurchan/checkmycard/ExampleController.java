package com.gtimurchan.checkmycard;

import com.gtimurchan.checkmycard.imageextractor.ImageExtractor;
import com.gtimurchan.checkmycard.imageextractor.ImageExtractorAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ExampleController {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

    int counter = 1;
    String searchString;
    //    ImageQueueManager imageQueueManager;
    ImageExtractorAsync imageExtractorAsync;

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

        if(imageExtractorAsync != null) {
            imageExtractorAsync.shutdown();
            imageExtractorAsync = null;
        }

        return "test2v2";
    }

    @GetMapping("/load-more-images")
    @ResponseBody
    public List<String> loadMoreImages(@RequestParam int offset, @RequestParam int limit) {
        String updatedUrl = GeneralHelper.replaceLabelWithValue(Const.WEB_URL_WITH_PAGE, Const.LABEL_TO_CHANGE, counter);
        System.out.println("updatedUrl = " + updatedUrl);
        counter++;


        Collection<String> imageUrls = new ImageExtractor().execute(updatedUrl, searchString);
        List<String> listUrls = new ArrayList<>(imageUrls);
        return listUrls;
    }

    @GetMapping("/load-more-images-async")
    @ResponseBody
    public List<String> loadMoreImagesAsync(@RequestParam int offset, @RequestParam int limit) {

        if (imageExtractorAsync == null) {
            System.out.println("create ImageExtractorAsync...");
            imageExtractorAsync = new ImageExtractorAsync();
            imageExtractorAsync.execute(searchString);
        } else {
            System.out.println("ImageExtractorAsync is already created. get images");
        }

        return imageExtractorAsync.getImageUrls();
    }
}