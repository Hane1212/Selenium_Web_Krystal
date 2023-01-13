package utils;

import API.common.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class ScanPages extends BasePage {
//   List of chain scan pages
    String address = Util.getPropertyValue("ADDRESS");
    String ETHER_SCAN = "https://etherscan.io/";
    By eth_native_bal_val = By.xpath("//*[@class='col-md-4 mb-1 mb-md-0'][text()='Balance:']/following-sibling::div");
    By eth_sup_bal_val = By.id("ContentPlaceHolder1_divFilteredHolderBalance");
    String FTM_SCAN = "https://ftmscan.com/address/";
    String ROS_SCAN = "https://ropsten.etherscan.io/address/";
    String BSC_SCAN = "https://bscscan.com/address/";
    String BSC_TEST_SCAN = "https://testnet.bscscan.com/address/";
    String AVA_SCAN = "https://avascan.info/blockchain/c/address/";
    String POLY_SCAN = "https://polygonscan.com/address/";
    String CRO_SCAN = "https://cronoscan.com/address/";
    String CHAIN = Util.getPropertyValue("TEST_CHAIN");

    public ScanPages(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }

    public String getBalanceFromEther(String token, String address){
        String tokenAddress = Util.getLineOfKey("TokensList_"+CHAIN+".csv", token)[1];
        String URL = ETHER_SCAN;
        String balance = new String();
        if(isNativeToken(token)){
            URL = ETHER_SCAN + "address/" +address;
            getPageInNewTab(URL);
            balance = readText(eth_native_bal_val).trim();
        }else {
            URL = ETHER_SCAN + "token/"+tokenAddress.trim()+"?a="+ address;
            getPageInNewTab(URL);
            balance = readText(eth_native_bal_val).trim();
            if (balance.contains("\\r?\\n")){
                balance = balance.split("\\r?\\n")[1];
            }
        }
        balance = balance.split(" ")[0];
        balance = Util.parseDecimalsFormat(balance);
        driver.close();
        switchToWindowByURLName("krystal");
        return  balance;
    }

    public boolean isNativeToken(String chain){
        String nativeToken = test_env.nativeToken;
        boolean flag = false;
        if (chain.equals(nativeToken)){
            flag = true;
        }
        return flag;
    }
}
