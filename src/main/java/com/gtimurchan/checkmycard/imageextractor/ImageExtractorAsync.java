package com.gtimurchan.checkmycard.imageextractor;

import com.gtimurchan.checkmycard.ImageInfo;
import com.gtimurchan.checkmycard.ImageQueueManager;
import com.gtimurchan.checkmycard.webpagescraper.IWebPageScraper;
import com.gtimurchan.checkmycard.webpagescraper.WebPageScraper_ChromeDriverAsync;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ImageExtractorAsync extends AbstractImageExtractor {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ImageExtractor.class);

    ImageQueueManager imageQueueManager;
    ExecutorService executor;

    public ImageExtractorAsync() {
        imageQueueManager = new ImageQueueManager();
    }

    public void setTheLastUploadedImageFilePath(String theLastUploadedImageFilePath) {
        this.theLastUploadedImageFilePath = theLastUploadedImageFilePath;
    }

    public void execute(String searchString) {
        boolean emaulate = true;

        if (emaulate) {
            try {
                Collection<String> imageUrls = emulateImageExtracting();
                Collection<ImageInfo> images = imageUrls.stream()
                        .map(ImageInfo::new)
                        .collect(Collectors.toList());
                imageQueueManager.addElements(images);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Create an ExecutorService with a fixed thread pool size
            executor = Executors.newFixedThreadPool(1);

            // Submit a task to the executor
            executor.submit(() -> {
                // Define the task you want to execute
                collectImages(searchString);
            });

            // Shutdown the executor once all tasks are completed
            executor.shutdown();
        }
    }

    private void collectImages(String searchString) {
        IWebPageScraper pageScraper = new WebPageScraper_ChromeDriverAsync(imageQueueManager, searchString);
        pageScraper.execute();
    }

    public List<String> getImageUrls() {
        List<String> listUrls = new ArrayList<>();
        try {
            Collection<ImageInfo> imageUrl = imageQueueManager.getElements(30);
            listUrls = imageUrl.stream()
                    .map(ImageInfo::getImageUrl) // Map ImageInfo objects to their Url fields
                    .collect(Collectors.toList()); // Collect Urls into a List<String>
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> result = new ArrayList<>(enrichWithCustomImage(listUrls));
        return result;
    }

    public void shutdown() {
        if (executor != null) {
            System.out.printf("Start ImageExtractorAsync::shutdownNow(): " + executor.isShutdown());
            executor.shutdownNow();
            System.out.printf("End ImageExtractorAsync::shutdownNow(): " + executor.isShutdown());
        } else {
            System.out.println("executor is null. Do nothing");
        }
    }
}