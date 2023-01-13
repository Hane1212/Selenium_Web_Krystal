package API.swap;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.restassured.path.json.JsonPath;
import utils.logs.Log;

public class gasPriceAPI {
    static String gasPrice_path ="/v2/gasPrice";
    //   ----------------------/{chain}/{version}/gasPrice----------------------------------------
    public static String getGasPriceSupperFast(){
//        super fast = Max(20, 2*fast)
//        TODO
        return getGasPrice("fast");
    }

    public static String getGasPriceFast() { return getGasPrice("fast"); }

    public static Double getGasPriceStandard(){
        return Double.valueOf(getGasPrice("standard"));
    }

    public static Double getBaseFee(){
        JsonPath jp = gasPrice();; //JsonPath object
        return   Double.valueOf(jp.get("baseFee"));
    }

    public static Double getPriorityFee(){
        JsonPath jp = gasPrice();; //JsonPath object
        return   Double.valueOf(jp.get("priorityFee.standard"));
    }

    public static String getGasPriceLow(){
        return getGasPrice("standard");
    }

    private static String getGasPrice(String option){
        JsonPath jp = gasPrice();
        return   jp.get("gasPrice."+option+"");
    }

    private static JsonPath gasPrice(){
        String API = Environment.uri_chain + gasPrice_path;
        Log.info("API_getGasPrice: "+ API);
        JsonPath jp = null; //JsonPath object
        jp = RestUtils.getJsonPath(RestUtils.getResponse(API));
        return   jp;
    }
}
