package pages;

import API.common.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Util;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class EarnScreen extends BasePage{
//  --------------------------------- Initial ----------------------------------
    String main_URL = Environment.base_url;
    String earn_URL = main_URL+"earn";
    By explore_btn = By.xpath("//*[@class='chakra-button css-1i93il9']");
// --------------------------------- Earn table ----------------------------------
    By token_list = By.xpath("//*[@class='css-1kw2fa0']");
    String list_tokens_str = "//*[@class='css-11l8nwy']";
// --------------------------------- Earn Screen ----------------------------------
    By src_token_opt = By.xpath("//*[@class='css-8ary9v']");
    By earn_eth_opt = By.xpath("//*[@class='css-70qvj9'] //*[text()='ETH']");
    By src_input = By.xpath("//*[@class='chakra-input css-nrf1kn']");
    By supply_btn = By.xpath("//*[@class='chakra-button css-16417xp']");
    By confirm_btn = By.xpath("//*[@class='chakra-button css-1dfq24b']");
    By swap_now_btn = By.xpath("//*[@class='chakra-text css-1wndfbg']");
    By goBack_btn = By.xpath("//*[@class='chakra-text css-1wndfbg']");
    //  --------------------------------- Operation ----------------------------------
    int wait = Integer.parseInt(Util.getPropertyValue("WAIT"));
    int waitP = Integer.parseInt(Util.getPropertyValue("WAIT_PROCESS"));
//    int

    public EarnScreen(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }

    /**
     * Author : HuongTT
     * Updated: 2022-03-09
     * @apiNote Open Earn page by get URL of Earn page
     */
    public void getEarnPageByURL(){
//        1. Access to Main page by URL
        driver.get(earn_URL);
//        2. Handle click on [Explore] button if button appear
        if(isElemExist(explore_btn)){
            click(explore_btn);
        }
    }

    /**
     * Author : HuongTT
     * Updated: 2022-03-22
     * @apiNote Open Earn page by click on [Earn] button from sidebar menu
     */
    public void getEarnPageByEarnButton(){
//        1. Access to Main page by URL
        driver.get(main_URL);
//        2. Click on [Earn] button
        click(ComPage.Earn_btn);
//        3. Handle click on [Explore] button if button appear
        if(isElemExist(explore_btn)){
            click(explore_btn);
        }
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-09
     * @apiNote Select token by name from Earn screen
     * @param token which will be used to earn
     */
    public void selectTokenByNameFromEarn(String token){
        List<WebElement> listTokens = driver.findElements(token_list);
        for (int i = 0; i<listTokens.size(); i++){
            String tokenName = listTokens.get(i).getText();
            if (tokenName.equals(token)){
                listTokens.get(i).click();
                break;
            }
        }
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-09
     * @apiNote Select token from list token at source
     * @param token
     */
    private void selectTokenByNameFromList(String token){
//        1. Get current token
        String currentTokenAtScr = readText(src_token_opt);
        if (!currentTokenAtScr.equals(token)){
//            If current token is not target token
//        2. Click option to select token
            click(src_token_opt);
//        3. Click on target token
            String selectToken = list_tokens_str + "[text()='"+token+"']";
            clickByAction(By.xpath(selectToken));
        }
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-24
     * @apiNote
     */
    public void performEarnToken(String token){
//        1. Select ETH from token list
        selectTokenByNameFromList(token);
//        2. Perform Earn
        performEarn();
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-23
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public void performEarn() {
//        1. Input amount to earn (Similar with Swap)
        ComPage.inputAmount();
//        2. Click on [Supply] button]
        click(supply_btn);
//        3. Click on [Confirm] button
        click(confirm_btn);
//        4. Handle confirm (with metamask)
        ImportMetaWallet.handleConfirm();
//        5. Wait to process done
        ComPage.waitProcessDone(false);
    }

    public void performSwapNow(String token){
        selectTokenByNameFromList(token);
        performSwapNowFromEarn();
    }
    /**
     * Author: HuongTT
     * Updated: 2022-03-24
     * @apiNote Perform swap now from Earn screen
     */
    private void performSwapNowFromEarn(){
//        1. From Earn screen, click on [Swap now] button
        driver.findElement(swap_now_btn).click();
//        2. Perform Swap
        SwapScreen.swapToken();
    }

    public void withdrawToken(String token){

    }

}
