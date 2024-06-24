package com.gtimurchan.checkmycard.webpagescraper;

import com.gtimurchan.checkmycard.ImageExtractor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.devicefarm.DeviceFarmClient;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlRequest;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;

public class AwsWebPageScrapper implements IWebPageScraper {

    RemoteWebDriver driver;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ImageExtractor.class);

    public AwsWebPageScrapper() throws MalformedURLException {


        //TODO: Move to resources and configuration
        //ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("checkmycard");
        DeviceFarmClient client = DeviceFarmClient.builder()
                //.credentialsProvider (credentialsProvider)
                .build();
        CreateTestGridUrlResponse response = client.createTestGridUrl(CreateTestGridUrlRequest.builder()
                        .projectArn("arn:aws:devicefarm:us-west-2:767397770324:testgrid-project:ba58c7a7-f020-4bea-9710-13be3cd3e0c8")
                .expiresInSeconds(300)
                .build());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");


        driver = new RemoteWebDriver(new URL(response.url()), capabilities);
    }

    @Override
    public Collection<String> execute(String url) {
        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
            String htmlContent = driver.getPageSource();

        } catch (Exception e) {
            LOG.debug("---> Error {}", e.getMessage());
        }
        return new ArrayList<String>();
    }
}
