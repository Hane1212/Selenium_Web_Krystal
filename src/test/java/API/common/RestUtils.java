package API.common;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Option;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import java.util.Map;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

public class RestUtils {
    /*
    ***Sets ContentType***
    We should set content type as JSON or XML before starting the test
    */
    public static void setContentType (ContentType Type){
        given().contentType(Type);
    }


    /**
     * @param: strURL
     * @param: mapParams that includes: key is param of API, value is value of API
     * @return: Response
     */
    public static Response getResponse(String strUrl, Map<String, String> mapParams) {
        String params = "?";
        int i = 0;
        for (Map.Entry<String, String> pair : mapParams.entrySet()) {
            if(i<(mapParams.size()-1)){
                params = params + pair.getKey() + "=" +  pair.getValue() + "&";
                i = i+1;
            }
            else{
                params = params + pair.getKey() + "=" +  pair.getValue();
            }
        }
        String url = strUrl + params;
        return get(url);
    }

    public static Response getResponse(String strUrl) {
        return get(strUrl);
    }

    /**
     * @param API
     * @return DocumentContext of json file (incase parse Json array)
     */
    public static DocumentContext getContext(String API){
        String jp = get(API).asString();
        Configuration cf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        DocumentContext ctx = com.jayway.jsonpath.JsonPath.using(cf).parse(jp);
        return ctx;
    }

    /**
     * @param: res Response object
     * @return: JsonPath object
     */
    public static JsonPath getJsonPath (Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }
}
