import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CharSequence login;
        CharSequence password;

        Scanner in = new Scanner(System.in);
        System.out.println("Input login for gosuslugi.ru");
        login = in.nextLine();
        System.out.println("Input password for gosuslugi.ru");
        password = in.nextLine();


        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://login.school.mosreg.ru/");

        driver.findElement(By.xpath("/html/body/div/div/div/div[5]/a")).click();

        int countRefreshPage = 3;
        Retries.retrie(driver,countRefreshPage);


        driver.findElement(By.xpath("//*[@id=\"login\"]")).sendKeys(login);
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
        driver.findElement(By.xpath("/html/body/esia-root/div/esia-idp/div/div[1]/form/div[4]/button")).click();

        WebElement waitShcoolPortal = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div[2]/div[2]/ul/li[2]/a")));

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/ul/li[2]/a")).click();

        WebElement waitChildrenPage = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div[2]/div/div[2]/ul/li[2]/ul/li[2]/a")));
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/ul/li[2]/ul/li[2]/a")).click();

    }
}

class Retries {
    public static void retrie(WebDriver driver, int countRefreshPage) {
        RuntimeException exception;
        int countRetries = 10;
        WebElement waitEsia;

        do {
            try {
                exception = null;
                waitEsia = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(
                                By.xpath("/html/body/esia-root/div/esia-idp/div/div[1]/form/div[4]/button")));
                countRetries = 0;
                countRefreshPage = 0;
            } catch (TimeoutException e) {
                exception = e;
            }
        } while (countRetries-->0);

        if (countRefreshPage != 0 && exception != null) {
            driver.navigate().refresh();
            retrie(driver,--countRefreshPage);
        } else if (exception != null ) {
            driver.quit();
            throw exception;
        }
    }
}
