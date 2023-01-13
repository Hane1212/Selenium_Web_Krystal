package API;

import com.jayway.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import utils.ScanPages;
import utils.Util;

import static com.jayway.restassured.RestAssured.*;


public class getAPI {
    public static String API;
    static Response response = null;
//# List of API
    static String API_URL =  "https://dev-krystal-api.knstats.com/";
    String API_OverView = "referralOverview";
    static String API_GASPRICE = "v1/gasPrice";
    static String API_BALANCE = "balances";
    static String TEST_CHAIN = Util.getPropertyValue("TEST_CHAIN");
//    ------------------------------------GENERAL-----------------------------------------
    public getAPI(){

    }

    public static void getAPIresponse(String chain, String APIname){
        String API_URL = set_swap_API_URL(chain, APIname);
        response = RestAssured.get(API_URL);
}

    public static String getJsonResponse(String chain, String APIname)  {
        getAPIresponse(chain, APIname);
        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        return responseBody;
    }

    public static void getResponseStatus(String chain, String APIname){
        int statusCode= given().when().get(set_swap_API_URL(chain, APIname)).getStatusCode();
        System.out.println("The response status is "+statusCode);
//        given().when().get(url).then().assertThat().statusCode(200);
    }

    public static void getResponsTime(String chain, String APIname)  {
//        getAPIresponse(APIname);
//        System.out.println(response.getTimeIn(TimeUnit.MILLISECONDS));
        System.out.println("The time taken to fetch the response "+get(set_swap_API_URL(chain, APIname))
                .timeIn(TimeUnit.MILLISECONDS) + " milliseconds");
//        System.out.println("The time taken to fetch the response "+get(url)
//                .timeIn(TimeUnit.MILLISECONDS) + " milliseconds");
    }

//    ------------------------------------SWAP-----------------------------------------
    public static String set_swap_API_URL(String chain, String APIname){
        String API_ = new String();
        API_ = API_URL + chain.toLowerCase()+ APIname;
        return API_;
    }

    public static String getJsonValueForGasFee(String API, String JsonObj, String val) {
        response = RestAssured.get(API);
        String responseBody = new String();
        String value = new String();
        try {
            responseBody = response.getBody().asString();
            JSONObject json = new JSONObject(responseBody);
            value = json.getJSONObject(JsonObj).getString(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static double getStandGasFee(String chain){
        String API_URL = set_swap_API_URL(chain, API_GASPRICE);
        String standard = getJsonValueForGasFee(API_URL, "gasPrice", "standard");
        double val = Double.parseDouble(standard);
        return val;
    }

    public static double getFastGasFee(String chain){
        String API_URL = set_swap_API_URL(chain, API_GASPRICE);
        String standard = getJsonValueForGasFee(API_URL, "gasPrice", "fast");
        double val = Double.parseDouble(standard);
        return val;
    }
    //    ------------------------------------ACCOUNT-----------------------------------------


}

