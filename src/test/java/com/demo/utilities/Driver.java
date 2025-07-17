package com.demo.utilities;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class Driver {

    private Driver(){}



    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    public static WebDriver getDriver(){
        if(driverPool.get() == null){
            String browserType = System.getProperty("browser") !=null?System.getProperty("browser"): ConfigurationReader.getProperty("browser");
            String gridUrl = System.getProperty("GRID_URL")!=null?ConfigurationReader.getProperty("local.grid"): ConfigurationReader.getProperty("aws.grid.url");
            System.out.println(gridUrl + " ------------------ grid URL");
            // String gridUrl = ConfigurationReader.getProperty("grid.url");
            switch (browserType){
                case "chrome" -> {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    options.addArguments("--remote-allow-origins=*");
                    driverPool.set(new ChromeDriver(options));
                }
                case "chrome-headless" -> {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless");
                    driverPool.set(new ChromeDriver(options));
                }
                case "firefox" -> {
                    FirefoxOptions options = new FirefoxOptions();
                    WebDriver firefoxDriver = new FirefoxDriver(options);
                    firefoxDriver.manage().window().fullscreen();
                    driverPool.set(firefoxDriver);
                }
                case "firefox-hadless" -> {
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("--headless");
                    driverPool.set(new FirefoxDriver(options));
                }
                case "grid-chrome-local"->{
                    ChromeOptions remoteOptions = new ChromeOptions();
                    remoteOptions.addArguments("--headless=new");
                    try {
                   driverPool.set(new RemoteWebDriver(new URL(ConfigurationReader.getProperty("local.grid")+"/wd/hub"), remoteOptions));
                    } catch (MalformedURLException e) {

                    }

                }


                case "grid-firefox-local"->{
                    DesiredCapabilities firefox = new DesiredCapabilities();
                    firefox.setBrowserName("firefox");

                    try {
                        driverPool.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefox));
                    } catch (MalformedURLException e) {

                    }

                }



                case "remote-chrome-aws"->{
                    ChromeOptions remoteOptions = new ChromeOptions();
                    remoteOptions.addArguments("--start-maximized");
                   // remoteOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
                    try {
                        driverPool.set(new RemoteWebDriver(new URL(gridUrl), remoteOptions));
                    } catch (MalformedURLException e) {

                    }

                }


                case "remote-firefox-aws"->{
                    FirefoxOptions firefoxOptions1 = new FirefoxOptions();
                    firefoxOptions1.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
                    try {
                        driverPool.set( new RemoteWebDriver(new URL(gridUrl+"/wd/hub"), firefoxOptions1));
                    } catch (MalformedURLException e) {

                    }

                }


            }
        }
        return driverPool.get();

    }

    public static void closeDriver(){
        if (driverPool.get()!=null){
            driverPool.get().quit();
            driverPool.remove();
        }
    }

}
