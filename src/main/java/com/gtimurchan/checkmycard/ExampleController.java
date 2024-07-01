package com.gtimurchan.checkmycard;

import com.gtimurchan.checkmycard.imageextractor.ImageExtractor;
import com.gtimurchan.checkmycard.imageextractor.ImageExtractorAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


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
    public String showInitialForm(Model model) {
//        model.addAttribute("imageUrl", theLastUploadedImageFilePath);
//        return "index";
        return "indexV4";
    }

    @GetMapping("/get-the-last-image-url")
    public ResponseEntity<Map<String, String>> getImageUrl() {
        // Логика для получения URL изображения
//        String imageUrl = "http://127.0.0.1:8091/img/2.png"; // Пример URL изображения

        String imageUrl = theLastUploadedImageFilePath;
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        System.out.println("method /get-the-last-image-url: imageUrl = " + imageUrl);

        return ResponseEntity.ok(response);
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
    public ImageResponse uploadImage(@RequestParam("imageFile") MultipartFile imageFile, HttpSession session) throws IOException {
        // Получаем sessionId
        String sessionId = session.getId();

        // Создаем уникальный каталог для пользователя
        File userUploadDir = new File(uploadDirectory + File.separator + sessionId);
        if (!userUploadDir.exists()) {
            userUploadDir.mkdirs();
        }

        // Save the uploaded file
        String fileName = imageFile.getOriginalFilename();
        Path filePath = Paths.get(userUploadDir.getAbsolutePath(), fileName);
        Files.write(filePath, imageFile.getBytes());

        System.out.println("file was uploaded : " + filePath.toString());

        String fileNameOnServer = "/" + sessionId + "/" + fileName;
        System.out.println("return fileName : " + fileNameOnServer);

        theLastUploadedImageFilePath = fileNameOnServer;
        return new ImageResponse(theLastUploadedImageFilePath);
    }

    @PostMapping("/uploadV3")
    @ResponseBody
    public ResponseEntity<?> handleFileUploadV3(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String sessionId = session.getId();

        // Create a unique directory for the user
        File userUploadDir = new File(uploadDirectory + File.separator + sessionId);
        if (!userUploadDir.exists()) {
            userUploadDir.mkdirs();
        }

        // Save the uploaded file
        String originalFileName = file.getOriginalFilename();
        String tempFilePrefix = "upload_";
        String tempFileSuffix = "_" + originalFileName;
        Path tempFilePath = Files.createTempFile(tempFilePrefix, tempFileSuffix);
        file.transferTo(tempFilePath.toFile());

        // Read the image from the temporary file
        BufferedImage image = ImageIO.read(tempFilePath.toFile());
        if (image == null) {
            Files.delete(tempFilePath);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Ошибка: неверный формат изображения.");
            return ResponseEntity.ok(response);
        }

        // Define the final file path
        String finalFileName = "processed_" + originalFileName;
        Path finalFilePath = Paths.get(userUploadDir.getAbsolutePath(), finalFileName);

        String fileNameOnServer = "/" + sessionId + "/" + finalFileName;
        System.out.println("return fileNameOnServer : " + fileNameOnServer);

        theLastUploadedImageFilePath = fileNameOnServer;

        // Save the processed image to the final file
        File outputFile = new File(finalFilePath.toString());
        ImageIO.write(image, "jpg", outputFile);

        // Delete the temporary file
        Files.delete(tempFilePath);

        // Возвращаем URL в JSON формате
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", fileNameOnServer);
        response.put("message", "Файл загружен успешно");

        return ResponseEntity.ok(response);
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