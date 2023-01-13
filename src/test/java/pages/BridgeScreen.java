package pages;

import API.common.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Util;

public class BridgeScreen extends BasePage{
    String bridge_URL = main_url +"bridge";


    String note = "//*[@role='list']";
    By trans_fee = By.xpath("("+note+")[1]");
    By amount = By.xpath("("+note+")[2]");
    By estimated = By.xpath("("+note+")[3]");


    public BridgeScreen(WebDriver driver, Environment.Chain test_env) {
        super(driver, test_env);
    }

    /**
     * Author : HuongTT
     * Updated: 2022-03-09
     * @apiNote Open Earn page by get URL of Earn page
     */
    public void getBridgePageByURL(){
//        1. Access to Main page by URL
        driver.get(bridge_URL);
    }

    /**
     * Transaction fee is 0.00 %, gas fee 0.9 USDT & it is paid to nodes facilitating token transfer.
     * Transaction fee is 0.1 %, gas fee 1.113 KNC (max 222.72 KNC) & it is paid to nodes facilitating token transfer.
     * @return
     */
    public String getTransactionFee(){
        String transaction_fee =  readText(trans_fee);
        transaction_fee.replace(",","");
        return transaction_fee;
    }

    public String getGasFee(){
        String gas_fee =  readText(trans_fee);
        gas_fee.replace(",","");
        return gas_fee;
    }

}
