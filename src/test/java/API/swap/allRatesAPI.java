package API.swap;

import API.common.Environment;
import API.common.RestUtils;
import API.market.overviewAPI;
import API.token.tokenDetailsAPI;
import API.token.tokenListAPI;
import com.jayway.jsonpath.DocumentContext;
import pages.ComPage;
import pages.SwapScreen;
import scala.util.parsing.combinator.testing.Str;
import utils.Util;
import utils.logs.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class allRatesAPI {
    static String allRates_path ="/v2/swap/allRates?";
    //   ----------------------/{chain}/{version}/swap/allRates------------------------------------
    /**
     * @return All Rates URL
     */
    private static String setAllRatesURL(String src, String des, String srcAmount){
        int decimals = overviewAPI.getDecimals(src);
        String srcAmount_ = Util.fmt(Util.roundAvoidM(Double.parseDouble(srcAmount), decimals));
        String platform = "&platformWallet="+Environment.platform;
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
     * @param src token address (Ex: 0x8076c74c5e3f5852037f31ff0093eeb8c8add8d3)
     * @param des token address (Ex: 0xbc81ea817b579ec0334bca8e65e436b7cb540147)
     * @param srcAmount amount to swap (Ex: 0.1)
     * @return Rate between src and dest token
     */
    public static String getRate(String src, String des, String srcAmount){
        String url = setAllRatesURL(src, des, srcAmount);
        String rate = (String) getAllRates(url, "rate");
        return rate;
    }

    private static Object getAllRates(String API, String option){
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> rates = ctx.read("$.rates");
        if (rates.size()>0){
//          TODO
        }
        Object out = ctx.read("$.rates[0]."+option+"");
//        List<String> emails = ctx.read("$.rates[?(@.status == 'VERIFIED')].email");
//        if (!rates.isEmpty()) {
//            return rates.get(0);
//        }
        return out;
    }

    private static int getTheBestPlatform(DocumentContext ctx, String destToken, String amount){
        double usd_rate = tokenDetailsAPI.get_usd_value_of_native_token();
        //        (amount * rate) * rate_usd - net fee est
        int best =0;
        double best_val = 0;
        int length = ctx.read("$.rates.length()");
        for (int i =0; i<length;i++) {
            String rate = new String() ;
            try {
//                ComPage.wait(2);
                rate = ctx.read("$.rates["+i+"].rate");
            }catch (Exception e){

            }
            Object estGasConsumed = ctx.read("$.rates["+i+"].estGasConsumed");
            Log.info("estGasConsumed: "+i+" "+ estGasConsumed);
            double rate_ = Util.roundAvoidD(Double.valueOf(rate), 18, 6);
            double output = Double.valueOf(amount) * rate_* tokenDetailsAPI.get_usd_rate(destToken);
            Log.info("Dest Amount of platform before net fee: "+output);
                  output = output - getNetworkFee(Double.valueOf(estGasConsumed.toString()), usd_rate);
            Log.info("Current best rate: "+ best_val);
            Log.info("Dest Amount of platform: "+ output);
            if(i==0) best_val = output;
            if (output > best_val) {
                best_val = output;
                best = i;
                }
        }
        return best;
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

    private static Double getNetworkFee(double estGasConsumed, double usd_rate){
        double network_fee;
        double gasPrice = gasPriceAPI.getGasPriceStandard();
        //      network_fee
        if (Environment.test_chain.equals("Ethereum") || Environment.test_chain.equals("Polygon") || Environment.test_chain.equals("Avalanche")) {
            Double baseFee = gasPriceAPI.getBaseFee();
            Double priorityFee = gasPriceAPI.getPriorityFee();
            //       # Network fee est	= Est.Gas consumed * (base_fee + priority_fee)
            network_fee =  estGasConsumed * (baseFee + priorityFee);
        }else {
            //       # network_fee = Est.Gas consumed * selected gas price
            network_fee = estGasConsumed * gasPrice;
        }
        network_fee = Util.roundAvoidD(network_fee * usd_rate, 9, 4);
        Log.info("network_fee: "+ network_fee);
        return network_fee;
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

    public static  List<String> getGasLimit500000(String chain, String src, String des, String srcAmount){
        String API = setAllRatesURL(chain, src, des, srcAmount);
        List<String> platform = new ArrayList<String>();
        List<String> platform_OK = new ArrayList<>();
        DocumentContext ctx = RestUtils.getContext(API);
        int length = ctx.read("$.rates.length()");
        if(length ==0){
            platform.add("NO_RATE");
        }else {
            for (int i =0; i < length ;i++){
                int estimatedGas = ctx.read("$.rates["+i+"].estimatedGas");
                if (estimatedGas == 500000){
                    String platformShort = ctx.read("$.rates["+i+"].platformShort");
                    platform.add(platformShort);
                }else {
                    return platform_OK;
                }
            }
        }
        return platform;
    }

}
