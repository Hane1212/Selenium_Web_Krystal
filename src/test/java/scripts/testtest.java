package scripts;

import API.common.Environment;
import API.common.TokenAllowance;
import API.earning.buildStakeTxAPI;
import API.earning.optionDetail;
import API.token.tokenListAPI;
import RFT.Earn;
import RFT.Swap;
import com.google.gson.Gson;
import io.netty.resolver.AddressResolver;
import liquibase.pro.packaged.A;
import liquibase.pro.packaged.B;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.http.HttpService;

import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple3;
import pages.BasePage;
import pages.ComAPI;
import utils.Assertion;
import utils.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import org.web3j.abi.FunctionEncoder;

import utils.logs.Log;

import static pages.TransferScreen.getGasLimit;

public class testtest extends BasePage{
    String krystal_url = Util.getPropertyValue("KRYSTAL");
    String API_GASPRICE = Util.getPropertyValue("API_GASPRICE");
    String eth_chain = Util.getPropertyValue("CHAIN_ETH");
    String key = Util.getPropertyValue("ADDRESS");
    String ETHER_SCAN = "https://etherscan.io/";
    static String pathProject = System.getProperty("user.dir");
    static String pathToData = pathProject + "/data/";
    By balance_val = By.xpath("//*[@class='col-md-4 mb-1 mb-md-0'][text()='Balance:']/following-sibling::div");
    static WebDriver driver;
    static String Sfile = pathToData + "Swap.csv";
    Assertion objAssert;

    //Common
    static String srcToken;
    static String destToken;
    static String amount;
    //    Swap screen


    //    Swap screen
    static Object expectValue, actualValue;
    static Double rate, destIncome, slipPage, minReceive, refPrice, priceImpact;
    static String gasPrice, gasLimit;
    static Double gasFee, usd;

//    public static String RPC_URL = "https://matic-mainnet.chainstacklabs.com/";
    public static String RPC_URL = "https://ropsten.infura.io/v3/b14b5f54206846dcac9d091556a2a063";
//    public static String RPC_URL = "https://polygonscan.com/";

    private static String fromAddress = "0x4420f4efA831D9fa76109C6fc9378E8759D365DF";
    private static String toAddress = "0x8D61aB7571b117644A52240456DF66EF846cd999";


    static String tokenAddress1 = "0x8d11ec38a3eb5e956b052f67da8bdc9bef8abf3e";
    static String toAddress1 = "0x4420f4efA831D9fa76109C6fc9378E8759D365DF";
    static String amount1 = "1000000000000000000";
    static String tokenAddress2 = "0x049d68029688eabf473097a2fc38ef61633a3c7a";
    static String toAddress2 = "0x4420f4efA831D9fa76109C6fc9378E8759D365DF";
    static String amount2 = "1000000";
    static String owner = "0x4420f4efA831D9fa76109C6fc9378E8759D365DF";

    String contract = "0xfb6bd0c00bd348125a1f6edc36e4b7ff5dbddfba";

    public testtest(WebDriver driver, Environment.Chain test_env) {
        super(driver, test_env);
    }

    //    public testtest(WebDriver driver, Environment.Chain test_env){
//        super(driver, test_env);
//    }
    public static void main(String[] args) {
        List<String> ListPlatform = Util.getListPropertyValue("EARN_TYPE");
        System.out.println(ListPlatform.get(0));
    }


//    @Test
    public static void test(){
        Log.info("TEST");
        System.out.println(test_env.chainName);
    }



}
