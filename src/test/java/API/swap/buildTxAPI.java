package API.swap;

import API.common.Environment;
import API.common.RestUtils;
import API.market.overviewAPI;
import API.token.tokenListAPI;
import com.jayway.restassured.path.json.JsonPath;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import utils.Util;
import utils.logs.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static oracle.jrockit.jfr.events.Bits.doubleValue;

public class buildTxAPI {
    static String buildTX_path ="/v2/swap/buildTx?";
    private static Web3j web3j;
    //   ----------------------/{chain}/{version}/swap/buildTx------------------------------------

    /**
     *
     * @param src token address (Ex: 0x8076c74c5e3f5852037f31ff0093eeb8c8add8d3)
     * @param des token address (Ex: 0xbc81ea817b579ec0334bca8e65e436b7cb540147)
     * @param srcAmount amount to swap
     * @param destAmount amount which received
     * @return
     */
    private static String setBuildTxURL(String src, String des, String srcAmount, String destAmount){
//        https://api-dev.krystal.team/optimism/v2/swap/buildTx?
//        userAddress=0x7464c9a9bef3d49cf8c0dfaeb44ff5c129c34396
//        &dest=0x7f5c764cbc14f9669b88837ca1490cca17c31607
//        &src=0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
//        &platformWallet=0x168E4c3AC8d89B00958B6bE6400B066f0347DDc9
//        &srcAmount=100000000000000
//        &minDestAmount=127191
//        &gasPrice=0
//        &hint=0x5b7b226964223a224b72797374616c20536d61727453776170222c2273706c697456616c7565223a31303030307d5d
//        &nonce=1

        int decimals = overviewAPI.getDecimals(src);
        String srcAmount_ = Util.fmt(Util.roundAvoidM(Double.parseDouble(srcAmount), decimals));
        decimals = tokenListAPI.getDecimals(des);
        String minDestAmount_ =Util.fmt(Util.roundAvoidM(Double.parseDouble(destAmount), decimals)) ;
//      -------------------------
        String userAddress = "userAddress="+Environment.user_address;
        des = "&dest="+des;
        src = "&src="+src;
        String platformWallet = "&platformWallet="+ Environment.platform;
        srcAmount_ = "&srcAmount="+srcAmount_;
        minDestAmount_ = "&minDestAmount="+minDestAmount_; //Minimum received
        String gasPrice = "&gasPrice=0";
        String nonce = "&nonce=1";

        String path_ = Environment.uri_chain + buildTX_path + userAddress + src + des + platformWallet + srcAmount_ + minDestAmount_ + gasPrice + nonce;
        Log.info("setBuildTxURL: " + path_);
        return path_;
    }
    /**
     * @param src token address (Ex: 0x8076c74c5e3f5852037f31ff0093eeb8c8add8d3)
     * @param des token address (Ex: 0xbc81ea817b579ec0334bca8e65e436b7cb540147)
     * @param srcAmount amount to swap
     * @param destAmount amount which received
     * @return gas Limit
     */
    public static String getGasLimit(String src, String des,String srcAmount, String destAmount){
        String API = setBuildTxURL(src, des, srcAmount, destAmount);
        JsonPath jp = null; //JsonPath object
        jp = RestUtils.getJsonPath(RestUtils.getResponse(API));
        String hex = jp.get("txObject.gasLimit");
        int out = Integer.parseInt(hex.replace("0x","").toUpperCase(),16);
        return String.valueOf(out);
    }

    public static String getData(String src, String des, String srcAmount, String destAmount){
        String API = setBuildTxURL(src, des, srcAmount, destAmount);
        JsonPath jp = null; //JsonPath object
        jp = RestUtils.getJsonPath(RestUtils.getResponse(API));
        String data = jp.get("txObject.data");
        return data;
    }

    public static Double getL1GasLimit(String src, String des, String srcAmount, String destAmount) {
        String data = getData(src, des, srcAmount, destAmount);
        data = data.replace("0x", "");
        DynamicBytes data_ = null;
        BigInteger L1Fee = null;
        try {
            data_ = new DynamicBytes(Hex.decodeHex(data.toCharArray()));
            L1Fee = getGasLimitL1_(data_, Environment.user_address, Environment.contractList.get("optimism"));
        } catch (DecoderException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(L1Fee);
//        L1Fee.divide(10^18)
        Double L1Fee_ = Util.roundAvoidD(doubleValue(L1Fee),18);
        System.out.println(doubleValue(L1Fee));
        return L1Fee_;
    }


    /**
     *
     * @param data
     * @param owner : my wallet
     * @param token_address : contract- 0x420000000000000000000000000000000000000F
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static BigInteger getGasLimitL1_(DynamicBytes data, String owner, String token_address) throws ExecutionException, InterruptedException {
        Environment.Chain chain_ = Environment.getChainByName("optimism") ;
        web3j = Web3j.build(new HttpService(chain_.RPC_URL));
        System.out.println("RPCLink: "+ chain_.RPC_URL);
        // Get function data
        Function function = new Function("getL1Fee",
                Arrays.asList(data),
                Collections.emptyList());

        String data_response = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = null;
        try {
            ethCall = web3j.ethCall(
                            Transaction.createEthCallTransaction(owner, token_address, data_response),
                            DefaultBlockParameterName.LATEST)
                    .send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hex_value = ethCall.getValue().replace("0x", "");
        BigInteger L1Fee_value = new BigInteger(hex_value, 16);
        return L1Fee_value;
    }
}
