package API.accounts;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Util;
import utils.logs.Log;

import java.util.List;

public class balancesAPI {
    static Response response = null;
    Response res = null; //Response object
    static JsonPath jp = null; //JsonPath object
    static String balances_path = "/v1/account/balances?address=";

    private static String set_accountBalances_URL(){
        String URL = new String();
        URL = Environment.uri_chain + balances_path + Environment.user_address;
        Log.info("AccountBalances_URL: "+ URL);
        return URL;
    }
    private static String set_accountBalances_URL(String chain){
        String URL = new String();
        URL = Environment.uri + chain.toLowerCase() + balances_path + Environment.user_address;
        Log.info("AccountBalances_URL: "+ URL);
        return URL;
    }

    /**
     * @apiNote get Max balance of token for account
     * @param token token name
     * @return max balance
     */
    public static String getMaxBalanceOfUserAddress(String token){
        String API_URL = set_accountBalances_URL();
        JSONObject json = getJsonObject(API_URL);
        String balance = getBalanceForAccount(json, token);
        balance = Util.parseDecimalsFormat(balance);
        balance.replaceAll(",",""); // does not work
        return balance;
    }

    /**
     * @param chain
     * @return list all token which balance >0
     */
    public static List<String> getListAccountBalance(String chain){
        String API = set_accountBalances_URL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.balances[*].token.address");
        return tokens;
    }

    public static String getBalance(String chain, String address){
        String API = set_accountBalances_URL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> balances = ctx.read("$.balances[?(@.token.address == '"+address+"')].balance");
        return balances.get(0);
    }

    public static String getTokenSymbol(String tokenAddress){
        String API = set_accountBalances_URL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.balances[?(@.token.address == '"+tokenAddress+"')].token.symbol");
//        if (tokens.size()>1){
////          TODO
//        }
        return tokens.get(0);
    }

    public static String getTokenAddress(String tokenSymbol){
        String API = set_accountBalances_URL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.balances[?(@.token.symbol == '"+tokenSymbol+"')].token.address");
//        if (tokens.size()>1){
////          TODO
//        }
        return tokens.get(0);
    }
    public static String getTokenAddress(String chainName, String tokenSymbol){
        String API = set_accountBalances_URL(chainName);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.balances[?(@.token.symbol == '"+tokenSymbol+"')].token.address");
        Log.info("Number of address of "+ tokenSymbol+ " :"+ tokens.size());
        return tokens.get(0);
    }

    /**
     * @apiNote : Get Rate of token based USD (Use API of Krystal)
     * @param token: current token
     * @return Rate of t/??oken based USD (Use API of Krystal)
     */
    public static Double getRateBasedUSDOfUserAddress(String token){
        Double rate = null;
        String API_URL = Environment.uri_chain + balances_path + Environment.user_address;
        Log.info("getRateBasedUSD API: "+ API_URL);
        JSONObject json = getJsonObject(API_URL);
        rate = getUSDRateForAccount(json, token);
        return rate;
    }

    /**
     * @apiNote : Get Json object from Expected API
     * @param API: Expected API
     * @return Json object from Expected API
     */
    public static JSONObject getJsonObject(String API){
        response = RestAssured.get(API);
        String responseBody = new String();
        JSONObject json = null;
        try {
            responseBody = response.getBody().asString();
            json = new JSONObject(responseBody);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * @apiNote : get balance of token from Address
     * @param json: Json file which get from API
     * @param token: Balance of token from address
     * @return Return balance of token from Address
     */
    public static String getBalanceForAccount(JSONObject json, String token) {
        String value = "0";
        try {
            JSONArray msg = (JSONArray) json.get("balances");
            for(int i = 0;i < msg.length();i++ ) {
                JSONObject tokenObj = msg.getJSONObject(i);
                JSONObject token_detail = tokenObj.getJSONObject("token");
                String symbol = token_detail.getString("symbol");
                for (int j=0; j<tokenObj.length(); j++){
                    //now get id & value
                    if (symbol.equals(token)){
                        String decimals = token_detail.getString("decimals");
                        System.out.println("Decimals: " + decimals);
                        String balance = tokenObj.getString("balance");
                        System.out.println("balance :"+ balance);
                        double dec = Math.pow(10, Double.parseDouble(decimals));
                        value = String.valueOf((Double.parseDouble(balance)/dec));
                        break;
                    }
                }
                if (!value.equals("0")){
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * @apiNote : Get USD rate for token
     * @param json : Json file from API
     * @param token: name of token
     * @return USD rate for token
     */
    public static Double getUSDRateForAccount(JSONObject json, String token) {
        Double value = 0.0;
        try {
            JSONArray msg = (JSONArray) json.get("balances");
            for(int i = 0;i < msg.length();i++ ) {
                JSONObject tokenObj = msg.getJSONObject(i);
                JSONObject token_detail = tokenObj.getJSONObject("token");
                String symbol = token_detail.getString("symbol");
                for (int j=0; j<tokenObj.length(); j++){
                    //now get rate to USD from token
                    if (symbol.equals(token)){
                        JSONObject quotes = tokenObj.getJSONObject("quotes");
                        JSONObject quotes_detail = quotes.getJSONObject("usd");
                        value = Double.parseDouble(quotes_detail.getString("rate"));
                        System.out.println("Value of Rate: " + value);
                        break;
                    }
                }
                if (!value.equals(0.0)){
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

}
