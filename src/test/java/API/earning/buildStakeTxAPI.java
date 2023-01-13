package API.earning;

import API.common.Environment;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.restassured.path.json.JsonPath;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.logs.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class buildStakeTxAPI {
    static String buildStakeTx_path = "all/v1/earning/buildStakeTx";
    static OkHttpClient client = new OkHttpClient();

    public static class BuildStakeRequest{
        public long chainID;
        public String platform;
        public String earningType;
        public String tokenAddress;
        public String tokenAmount;
        public String userAddress;

        public BuildStakeRequest(long chainID,  String platform, String earningType, String tokenAddress, String tokenAmount, String userAddress) {
            this.chainID = chainID;
            this.platform = platform;
            this.earningType = earningType;
            this.tokenAddress = tokenAddress;
            this.tokenAmount = tokenAmount;
            this.userAddress = userAddress;
        }
    }

    public static String buildStakeTx(long chainID, String platform, String type,
                                      String tokenAddress, String amount) throws IOException, JSONException {
        BuildStakeRequest stakeRequest = new BuildStakeRequest(
                chainID, platform, type, tokenAddress, amount, Environment.user_address);

        String jsonBody = new Gson().toJson(stakeRequest);
        System.out.println("jsonBodyWhenBuildTx:");
        System.out.println(jsonBody);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url(Environment.uri + buildStakeTx_path)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        String out = response.body().string();
        return out;
    }

    public static int getGasLimit(long chainID, String platform, String type,
                                  String tokenAddress, String amount) {
        int out = 0;
        try {
            JsonPath jp = new JsonPath(buildStakeTx(chainID, platform, type , tokenAddress, amount));
            String gasLimit = jp.get("txObject.gasLimit");
            out = Integer.parseInt(gasLimit.replace("0x","").toUpperCase(),16);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

}
