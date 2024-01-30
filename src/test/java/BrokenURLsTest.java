import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrokenURLsTest {

    public static WebDriver driver;

    @BeforeTest
    public void launch(){
        // Setting the Chrome browser version
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("121");

        //launching the Chrome browser with URL
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.get("https://www.icicibank.com/");
    }

    @Test
    public void test(){

        // getting all links by filtering empty, null and invalid URLs
        List<String> links = driver.findElements(By.tagName("a"))
                .stream()
                .map(link -> link.getAttribute("href"))
                .filter(url -> url != null && url.contains("http"))
                .collect(java.util.stream.Collectors.toList());

        System.out.println("Total Number of links: "+links.size());

        // checking for broken link using url response code
        links.parallelStream().forEach(url -> {
            try {

                URL linkUrl = new URL(url);

                // Open a connection for the URL
                HttpURLConnection connection = (HttpURLConnection) linkUrl.openConnection();
                connection.setRequestMethod("GET");

                // Get response code of the URL
                int responseCode = connection.getResponseCode();

                // Checking for the broken link
                if (responseCode >= 400) {
                    System.out.println("Broken Link: " + url + " - Response Code: " + responseCode);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @AfterTest
    public void close(){
        driver.close();
    }

}
