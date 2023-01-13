package API.market;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.restassured.path.json.JsonPath;
import utils.logs.Log;

public class refPriceAPI {
    static String refPrice_path ="/v1/market/refPrice?";


//   ----------------------/{chain}/v1/market/refPrice------------------------------------
    /**
     * @param src token address (Ex: 0x8076c74c5e3f5852037f31ff0093eeb8c8add8d3)
     * @param des token address (Ex: 0xbc81ea817b579ec0334bca8e65e436b7cb540147)
     * @return RefPrice between source and destination token in swap
     */
    public static String getRefPrice(String src, String des){
        String API = Environment.uri_chain + refPrice_path +"src="+src+"&dest="+des;
        Log.info("getRefPrice_API: "+API);
        JsonPath jp = null; //JsonPath object
        jp = RestUtils.getJsonPath(RestUtils.getResponse(API));
        return   jp.get("refPrice");
    }
}
