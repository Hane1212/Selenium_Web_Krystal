package API.multisend;

import API.common.Environment;
import com.google.gson.Gson;
import com.jayway.restassured.path.json.JsonPath;
import okhttp3.*;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.http.HttpService;
import pages.ComAPI;
import utils.logs.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class buildTxAPI {
    static String buildMultisendTx_path = "/v1/transfer/buildMultisendTx";
    static OkHttpClient client = new OkHttpClient();
    Environment.Chain test_chain = Environment.getChainByName(Environment.test_chain);
    private static Web3j web3j;
    public static class SendInMultiSend {
        public String amount;
        public String toAddress;
        public String tokenAddress;

        public SendInMultiSend(String amount, String toAddress, String tokenAddress) {

            this.amount = ComAPI.getWeiValue(tokenAddress, amount).toString();
            this.toAddress = toAddress;
            this.tokenAddress = tokenAddress;
        }
    }
    public static class BuildMultiSendRequest{
        public String senderAddress;
        public List<SendInMultiSend> sends;

        public BuildMultiSendRequest( String senderAddress, List<SendInMultiSend> sends) {
            this.senderAddress = senderAddress;
            this.sends = sends;
        }
    }

    /**
     * @param items: amount, toAddress, tokenAddress
     * @return
     */
    public static String createJsonBody(String[][] items){
        List<SendInMultiSend> sends = new ArrayList<>();
        for (int i=0; i<items.length; i++){
            SendInMultiSend SendInMultiSend = new SendInMultiSend(items[i][0], items[i][1], items[i][2]);
            sends.add(SendInMultiSend);
        }
        BuildMultiSendRequest BuildMultiSendRequest = new BuildMultiSendRequest(Environment.user_address, sends);
        String jsonBody = new Gson().toJson(BuildMultiSendRequest);
        return jsonBody;
    }

    public static String buildMultisendTx(String[][] itemsList) throws IOException {
        String jsonBody = createJsonBody(itemsList);
        System.out.println("JsonBody: "+ jsonBody);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url(Environment.uri_chain + buildMultisendTx_path)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        String out = response.body().string();
        return out;
    }

    public static int getGasLimitByAPI(String[][] itemsList) {
        int out = 0;
        try {
            String test = buildMultisendTx(itemsList);
            JsonPath jp = new JsonPath(test);
            String gasLimit = jp.get("txObject.gasLimit");
            out = Integer.parseInt(gasLimit.replace("0x","").toUpperCase(),16);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    //---------------------------get Gas Limit By Call Node------------------------
    /**
     * @param itemsList : amount, toAddress, tokenAddress
     * @return
     */
    public String getGasLimit(String[][] itemsList){
//        String contract = "0xfb6bd0c00bd348125a1f6edc36e4b7ff5dbddfba";//BSC
        String contract = test_chain.l1GasContract;
        List<StaticStruct> inputs = new ArrayList<>();
        for (int i = 0; i<itemsList.length;i++){
            StaticStruct input = new StaticStruct(
                    new org.web3j.abi.datatypes.Address(itemsList[i][2]),//tokenAddress
                    new org.web3j.abi.datatypes.Address(itemsList[i][1]),//toAddress
                    new Uint256(new BigInteger(itemsList[i][0])) //amount
            );
            inputs.add(input);
        }
        Environment.Chain chain_ = Environment.getChainByName(Environment.test_chain);
        web3j = Web3j.build(new HttpService(chain_.RPC_URL));
        System.out.println("RPCLink: "+ chain_.RPC_URL);
        // Get function data
        Function function = new Function("multiSend",
                Arrays.asList(new DynamicArray<>(StaticStruct.class, inputs)),
                Collections.emptyList());

        String data_response = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = null;
        EthEstimateGas response = null;
        try {
            try {
                response = web3j.ethEstimateGas(new Transaction(Environment.user_address, null, null, null, contract, null, data_response)).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BigInteger estimatedGas;
        if(response.getResult() == null) {
            // Incase can not get gas limit we will fallback to default value
            Log.error("Error when buildtx for Transfer: " + response.getError().getMessage());
            estimatedGas = BigInteger.valueOf(100000);
        } else {
            estimatedGas = response.getAmountUsed();
        }
        return String.valueOf(estimatedGas);
    }

}
