/*
Create date:
Author:
Description:
 */
package scripts;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pages.*;
import utils.Assertion;
import utils.Util;
import utils.logs.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static pages.BasePage.wait;


public class BaseTest_NonExtension {
    public String baseURL = "https://dev-krystal.knstats.com/summary";
    String MacOS = "Mac";
    String WinOS = "Windows";
    public String IEBrowser = "IEDriver";
    public String ChromeBrowser = "Chrome";
    String EdgeBrowser = "Edge";
    String metaMask = "nkbihfbeogaeaoehlefnkodbefgpgknn-10.3.0-Crx4Chrome.com.crx";
    public static String Krystal_URL = Util.getPropertyValue("KRYSTAL");
    public static WebDriver driver;
    public static ImportMetaWallet MetaWallet;
    public static MultiSendScreen MultiSendPage;
    public static SwapScreen SwapPage;
    public static TransferScreen TransPage;
    public static EarnScreen EarnPage;
    public static SummaryScreen MainPage;
    public static ComPage ComPage;
    public static Assertion AssertPage;
    public static MarketScreen MarketPage;
    public static SummaryNoConnectedWallet InitPage;

    public BaseTest_NonExtension() {

    }

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) {
        Log.info("Tests is starting!");
        setEnvironment(browser);
        pagesSetup();
        MetaWallet.HandleExtension();
        InitPage.getMainPage();
        MetaWallet.ConnectWallet();
        MetaWallet.Unlock_NonExtension();
    }

    public  void setEnvironment(String browser){
        String pathProject = System.getProperty("user.dir");
        String os = System.getProperty("os.name");
        switch (browser) {
            case "Chrome":
                if(os.contains(WinOS)){
                    System.setProperty("webdriver.chrome.driver", pathProject + "/libs/chromedriver.exe");
                }
                ChromeOptions options = new ChromeOptions();
                String pathToExtensionFileChrome = pathProject + "/libs/"+metaMask;
                options.addExtensions(new File(pathToExtensionFileChrome));
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                driver = new ChromeDriver(capabilities);
                break;
            case "Firefox"://just add for extend after
                System.setProperty("webdriver.gecko.driver", pathProject + "/libs/geckodriver.exe");
                System.setProperty("webdriver.firefox.bin", "C:/Program Files/Mozilla Firefox/firefox.exe");
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + browser);
        }
//        wait = new WebDriverWait(driver,30);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
//        driver.manage().window().setPosition(new Point(- 1000, 0));
//        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    public void pagesSetup() {
//        MetaWallet = new ImportMetaWallet(driver);
//        MultiSendPage = new MultiSendScreen(driver);
//        SwapPage = new SwapScreen(driver);
//        TransPage = new TransferScreen(driver);
//        EarnPage = new EarnScreen(driver);
//        MainPage = new SummaryScreen(driver);
//        ComPage = new ComPage(driver);
//        AssertPage = new Assertion(driver);
//        MarketPage = new MarketScreen(driver);
//        InitPage = new SummaryNoConnectedWallet(driver);
    }

    //    @AfterTest
    public void tearDown() {
        Log.info("Tests are ending!");
        driver.quit();
    }
}
