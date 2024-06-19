package com.gtimurchan.checkmycard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Slf4j
@Controller
public class ExampleController {
    // Display the form at the root level
    @GetMapping("/")
    public String showInitialForm() {
        return "index"; // Ensure this matches the Thymeleaf template name (index.html)
    }

    // Handle form submission
    @PostMapping("/submit-request")
    public String handleRequest(@RequestParam("searchString") String searchString, Model model) {
        LOG.debug("Received request string: {}", searchString);

        // Process the request string as needed
        // For example, call a service or perform some logic
        Collection<String> imageUrls = new ImageExtractor().execute(searchString);

        // Add the image URLs to the model to pass to the view
        model.addAttribute("imageUrls", imageUrls);

        // Return the view name to display the results
        return "resultTableY"; // Ensure you have a result.html Thymeleaf template to display the results
    }

//
//    @GetMapping("/")
//    public String home(Model model) {
//        LOG.debug("ExampleController start start start");
//
//        Collection<String> imageUrls = ImageExtractor.execute();
////        String htmlPage = HtmlGenerator.generateHtmlPage(imageUrls);
////        System.out.println(htmlPage);
////        return htmlPage;
//
//        model.addAttribute("imageUrls", imageUrls);
//        return "example";
//    }
}