package RFT.Client;

import API.common.Environment;
import API.common.RestUtils;
import API.market.overviewAPI;
import API.token.tokenListAPI;
import RFT.Swap;
import com.jayway.jsonpath.DocumentContext;
import pages.SwapScreen;
import utils.Util;
import utils.logs.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwapClient {
    String chainName;
    long chainID;

    public SwapClient(long chainID) {
        this.chainID = chainID;
    }

    static String allRates_path ="/v2/swap/allRates?";
    //   ----------------------/{chain}/{version}/swap/allRates------------------------------------
    /**
     * @return All Rates URL
     */
    private static String setAllRatesURL(String src, String des, String srcAmount){
        int decimals = overviewAPI.getDecimals(src);
        String srcAmount_ = Util.fmt(Util.roundAvoidM(Double.parseDouble(srcAmount), decimals));
        String platform = "&platformWallet="+ Environment.platform;
        String userAddress = "&userAddress="+Environment.user_address;
        String path_ = Environment.uri_chain + allRates_path +"src="+src+"&srcAmount="+srcAmount_+"&dest="+des+platform+userAddress;
        Log.info("setAllRatesURL: " + path_);
        return path_;
    }

    private static String setAllRatesURL(String chain, String src, String des, String srcAmount){
        String srcAmount_ = srcAmount;
        String platform = "&platformWallet="+Environment.platform;
        String userAddress = "&userAddress="+Environment.user_address;
        String path_ = Environment.uri+ chain + allRates_path +"src="+src+"&srcAmount="+srcAmount_+"&dest="+des+platform+userAddress;
        Log.info("setAllRatesURL: " + path_);
        return path_;
    }
    /**
     * @param src source token (Ex: ETH, BNB...)
     * @param des dest token (Ex: ETH, BNB...)
     * @param srcAmount amount to swap (Ex: 1, 0.4...)
     * @return list of some items which get from API
     */
    public static Map<String, String> getAllRatesInfo(String src, String des, String srcAmount){
        String srcTokenAddress = tokenListAPI.getTokenAddress(src);
        String destTokenAddress = tokenListAPI.getTokenAddress(des);

        String url = setAllRatesURL(srcTokenAddress, destTokenAddress, srcAmount);
        DocumentContext ctx = RestUtils.getContext(url);

        Map<String, String> dictionary = new HashMap<String, String>();
//        int index = getTheBestPlatform(ctx, des, srcAmount);
        int index = theSelectedPlatform(ctx);

//      Get priceImpact transfer amount
        Object  out = ctx.read("$.rates["+index+"].priceImpact");
        dictionary.put("priceImpact", out.toString());
//      Get estimatedGas transfer amount
        out = ctx.read("$.rates["+index+"].estimatedGas");
        dictionary.put("estimatedGas", out.toString());
//      Get estGasConsumed transfer amount
        out = ctx.read("$.rates["+index+"].estGasConsumed");
        dictionary.put("estGasConsumed", out.toString());
//      Get rate transfer amount
        out = ctx.read("$.rates["+index+"].rate");
        dictionary.put("rate", out.toString());

        out = ctx.read("$.rates["+index+"].platformShort");
        Log.info("The best platform: "+ out);
        return dictionary;
    }

    public Swap.PlatformList getGasLimit(String chain, String src, String des, String srcAmount){
        Swap.PlatformList allRateDetails = new Swap.PlatformList();
        String API = setAllRatesURL(chain, src, des, srcAmount);
        DocumentContext ctx = RestUtils.getContext(API);
        int length = ctx.read("$.rates.length()");
        if(length ==0){
            allRateDetails.platform_NG.add("NO_RATE");
        }else {
            for (int i =0; i < length ;i++){
                int estimatedGas = ctx.read("$.rates["+i+"].estimatedGas");
                String platformShort = ctx.read("$.rates["+i+"].platformShort");
                if (estimatedGas == 500000){
                    allRateDetails.platform_NG.add(platformShort);
                }else {
                    allRateDetails.platform_OK.add(platformShort);
                }
            }
        }
        return allRateDetails;
    }

    private static int theSelectedPlatform(DocumentContext ctx){
        String selectedPlatform = SwapScreen.getTheSelectedPlatform();
        int index = 0;
        int length = ctx.read("$.rates.length()");
        for (int i =0; i<length;i++) {
            String platform = new String();
            try {
//                ComPage.wait(2);
                platform = ctx.read("$.rates[" + i + "].platformShort");
            } catch (Exception e) {

            }
            if (platform.equals(selectedPlatform))
            {
                index = i;
                break;
            }
        }
        return index;
    }
}
