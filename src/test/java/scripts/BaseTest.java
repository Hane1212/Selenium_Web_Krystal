/*
Create date:
Author:
Description:
 */
package scripts;
import API.common.Environment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pages.*;
import utils.Assertion;
import utils.Util;
import utils.logs.Log;

import java.util.concurrent.TimeUnit;

import static pages.BasePage.wait;
import java.io.File;


public class BaseTest {
    String MacOS = "Mac";
    String WinOS = "Windows";
    String LinuxOS = "Linux";
    public String IEBrowser = "IEDriver";
    public String ChromeBrowser = "Chrome";
    String EdgeBrowser = "Edge";
    String metaMask = "nkbihfbeogaeaoehlefnkodbefgpgknn-10.3.0-Crx4Chrome.com.crx";
    String metaMask_URL = "chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/notification.html#unlock";
    public static String Krystal_URL = Util.getPropertyValue("KRYSTAL");
    String pathProject = System.getProperty("user.dir");

    public static WebDriver driver;
    public static Environment.Chain test_env;
    public static ImportMetaWallet MetaWallet;
    public static MultiSendScreen MultiSendPage;
    public static SwapScreen SwapPage;
    public static TransferScreen TransPage;
    public static EarnScreen EarnPage;
    public static SummaryScreen SumPage;
    public static ComPage ComPage;
    public static Assertion AssertPage;
    public static MarketScreen MarketPage;
    public static BridgeScreen BrigePage;
    public static SummaryNoConnectedWallet InitPage;

    public BaseTest() {
    }

    public static WebDriver getDriver() {
        return driver;
    }
    public static Environment.Chain getTestEnvironment(){return test_env;}

    @BeforeTest(alwaysRun=true)
    @Parameters("browser")
    public void setup(String browser) {
        Log.info("Tests is starting!");
        test_env = Environment.getChainByName(Environment.test_chain);
        openChromeProfile(browser);
        pagesSetup();
        InitPage.getMainPage();
        BasePage.getPageInNewTab(metaMask_URL);
        MetaWallet.Unlock();
        MetaWallet.ConnectWallet();
        InitPage.getMainPage();
    }

    public void openChromeProfile(String browser){
        String pathProject = System.getProperty("user.dir");
        String os = System.getProperty("os.name");
        String chrome_path = new String();
        switch (browser) {
            case "Chrome":
                setSystemPropertyChrome();
                ChromeOptions options = new ChromeOptions();
                if(os.contains(MacOS)){
                    options.addArguments("--user-data-dir=/Users/"+System.getProperty("user.name")+"/Library/Application Support/Google/Chrome/");
                    options.addArguments("--profile-directory=Profile 4");
                } else if (os.contains(LinuxOS)) {
//                    chrome_path = "--user-data-dir=/home/hane/.config/chrome-remote-desktop/chrome-config/google-chrome/";
                    chrome_path = "--user-data-dir=/home/"+System.getProperty("user.name")+"/.config/chrome-remote-desktop/chrome-config/google-chrome/";
                    options.addArguments(chrome_path);
                    options.addArguments("--profile-directory=Profile 1");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--disable-gpu");
                    // options.addArguments("--disable-extensions");
                    options.addArguments("--start-maximized");
                    options.addArguments("--headless=chrome");
                    File extension = new File(pathProject +"/libs/MetaMask.crx");
                    options.addExtensions(extension);
                }
                Log.info("Current OS: "+ os);
                Log.info("chrome_path: "+ chrome_path);
            //    options.setHeadless(true);
//                options.addArguments("--disable-extensions");
                driver = new ChromeDriver(options);
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

    private void setSystemPropertyChrome(){
        String os = System.getProperty("os.name");
        if(os.contains(WinOS)){
            System.setProperty("webdriver.chrome.driver", pathProject + "/libs/chromedriver.exe");
        }else if(os.contains(MacOS)){
            System.setProperty("webdriver.chrome.driver", pathProject + "/libs/chromedriver");
        }else if(os.contains(LinuxOS)){
            System.setProperty("webdriver.chrome.driver", pathProject + "/libs/chromedriver_linux");
        }
    }

    public void pagesSetup() {
        MetaWallet = new ImportMetaWallet(driver, test_env);
        MultiSendPage = new MultiSendScreen(driver, test_env);
        SwapPage = new SwapScreen(driver, test_env);
        TransPage = new TransferScreen(driver, test_env);
        EarnPage = new EarnScreen(driver, test_env);
        SumPage = new SummaryScreen(driver, test_env);
        ComPage = new ComPage(driver, test_env);
        AssertPage = new Assertion(driver, test_env);
//        MarketPage = new MarketScreen(driver, test_env);
        InitPage = new SummaryNoConnectedWallet(driver, test_env);
        SumPage = new SummaryScreen(driver, test_env);
        BrigePage = new BridgeScreen(driver, test_env);
    }

    @AfterTest(alwaysRun=true)
    public void tearDown() {
        Log.info("Tests are ending!");
        driver.quit();
    }
}
