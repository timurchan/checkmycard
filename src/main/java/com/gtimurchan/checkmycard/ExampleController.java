package com.gtimurchan.checkmycard;

import com.gtimurchan.checkmycard.imageextractor.ImageExtractor;
import com.gtimurchan.checkmycard.imageextractor.ImageExtractorAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
public class ExampleController {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

    @Value("${upload.directory}")
    private String uploadDirectory;

    int counter = 1;
    String theLastUploadedImageFilePath;
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

        if (imageExtractorAsync != null) {
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
            imageExtractorAsync.setTheLastUploadedImageFilePath(theLastUploadedImageFilePath);
            imageExtractorAsync.execute(searchString);
        } else {
            System.out.println("ImageExtractorAsync is already created. get images");
        }

        return imageExtractorAsync.getImageUrls();
    }


    @PostMapping("/upload-image")
    @ResponseBody
    public ImageResponse uploadImage(@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        // Ensure the upload directory exists
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the uploaded file
        String fileName = imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);
        Files.write(filePath, imageFile.getBytes());
        System.out.println("file was uploaded : " + filePath.toString());
        System.out.println("return fileName : " + fileName);

        // Return the URL of the uploaded image
//        String imageUrl = "/img/" + fileName;
//        return new ImageResponse(imageUrl);

        theLastUploadedImageFilePath = fileName;//filePath.toString();
        return new ImageResponse(fileName);
    }

    public static class ImageResponse {
        private String imageUrl;

        public ImageResponse(String imageData) {
            this.imageUrl = imageData;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}