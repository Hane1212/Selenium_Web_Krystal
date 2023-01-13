package API;

import API.common.Environment;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.http.HttpService;
import pages.ComAPI;
import utils.logs.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class transferAPI {
    private static Web3j web3j;

    public static int getGasLimit(String srcToken, String toAddress, double amount){
        int out=0;
        try {
            out = getGasLimit_(srcToken, toAddress, amount);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return out;
    }


    private static int getGasLimit_(String srcToken, String toAddress, double amount_in_BigInteger) throws ExecutionException, InterruptedException {
        Environment.Chain chain_ = Environment.getChainByName(Environment.test_chain);
        web3j = Web3j.build(new HttpService(chain_.RPC_URL));
        String fromAddr = Environment.user_address;

        // Get function data
        Function function = new Function("transfer",
                Arrays.asList(new Address(toAddress), new Uint256((long) amount_in_BigInteger)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);

        Transaction transaction;
        if (ComAPI.isNative(srcToken)) {
            transaction = new Transaction(fromAddr, null, null, null, toAddress, null, "");
        } else {
            transaction = new Transaction(fromAddr, null, null, null, srcToken, null, data);
        }

        EthEstimateGas response = null;
        try {
            response = web3j.ethEstimateGas(transaction).send();
        } catch (IOException e) {
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
        return (int) (estimatedGas.intValue()*1.2);
    }

    private static int estimateGas(String tokenAddress,
                                   String toAddress, double amount, boolean isNative) throws ExecutionException, InterruptedException {
        Environment.Chain chain_ = Environment.getChainByName(Environment.test_chain);
        web3j = Web3j.build(new HttpService(chain_.RPC_URL));
        String fromAddr = Environment.user_address;

        // Get function data
        Function function = new Function("transfer",
                Arrays.asList(new Address(toAddress), new Uint256((long) amount)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);

        // Get estimate Gas Limit
        Transaction transaction;
        if (isNative) {
            transaction = new Transaction(fromAddr, null, null, null, toAddress, null, "");
        } else {
            transaction = new Transaction(fromAddr, null, null, null, tokenAddress, null, data);
        }
        EthEstimateGas response = null;
        try {
            response = web3j.ethEstimateGas(transaction).send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BigInteger estimatedGas;
        if (response.getResult() == null) {
            // Incase can not get gas limit we will fallback to default value
            Log.info("Error when getting gas limit for Transfer: " + response.getError().getMessage());
            estimatedGas = BigInteger.valueOf(100000);
        } else {
            estimatedGas = response.getAmountUsed();
        }
        return (int) (estimatedGas.intValue() * 1.2);
    }

}
