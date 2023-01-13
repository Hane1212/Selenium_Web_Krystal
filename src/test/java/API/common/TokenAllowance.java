package API.common;

import java.math.BigInteger;

import API.earning.optionDetail;
import API.swap.gasPriceAPI;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;
import org.web3j.utils.Convert;
import utils.Util;
import utils.logs.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.Arrays;

public class TokenAllowance {
    private static Web3j web3j;
    public static org.web3j.protocol.http.HttpService httpService = new HttpService(Util.getPropertyValue("CONFIG_HTTP_SERVICE"));
    public static Web3j web3 = Web3j.build(httpService);


    public static BigInteger getAllowance_(String chain, String owner, String spender, String token_address) {
        Environment.Chain chain_ = Environment.getChainByName(chain);
        web3j = Web3j.build(new HttpService(chain_.RPC_URL));
        // Read function allowance from contract
        Function allowance = new Function("allowance",
                Arrays.asList(new Address(owner), new Address(spender)),
                Collections.emptyList());

        String allowance_function = FunctionEncoder.encode(allowance);
        org.web3j.protocol.core.methods.response.EthCall ethCall = null;
        try {
            ethCall = web3j.ethCall(
                            Transaction.createEthCallTransaction(owner, token_address, allowance_function),
                            DefaultBlockParameterName.LATEST)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String hex_value = ethCall.getValue().replace("0x","");
        Log.info("hex_value of Allowance: " + hex_value);
        if(hex_value.equals("")){
            return new BigInteger("0");
        }
        return new BigInteger(hex_value, 16);
    }

    public static BigInteger getAllowance(String chain, String spender, String tokenAddress){
        String owner = Environment.user_address;//0x4420f4efA831D9fa76109C6fc9378E8759D365DF
        BigInteger allowance_value = getAllowance_(chain, owner,spender,tokenAddress);
        return allowance_value;
    }

    /**
     * @apiNote Unapprove to transfer token
     * @param owner: CONFIG_SPENDER_MULTISEND of Krystal
     * @param spender: Address which use as source to transfer
     * @param privateKey: Private key of address
     * @param token_address: token which transfer
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static String unApproveToken(String owner, String spender, String privateKey, String token_address) throws IOException, InterruptedException, ExecutionException {
        String addr = Credentials.create(privateKey).getAddress();
        Credentials credentials = Credentials.create(privateKey);

        // Get nonce
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(addr, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce_in_BigInteger = ethGetTransactionCount.getTransactionCount();

        BigInteger amount_in_BigInteger = new BigInteger("0");

        // Get gasPrice
        String gasPrice = gasPriceAPI.getGasPriceFast();
        BigInteger gas_Price = Convert.toWei(gasPrice, Convert.Unit.GWEI).toBigInteger();

        // Get function data
        Function function = new Function("approve",
                Arrays.asList(new Address(spender), new Uint256(amount_in_BigInteger)),
                Collections.emptyList());
        String approve_data = FunctionEncoder.encode(function);

        // Get estimate Gas Limit
        Transaction transaction = new Transaction(addr, nonce_in_BigInteger, null, null, token_address, null, approve_data);
        EthEstimateGas response = web3.ethEstimateGas(transaction).send();

        BigInteger estimatedGas;
        if(response.getResult() == null) {
            // Incase can not get gas limit we will fallback to default value
            estimatedGas = BigInteger.valueOf(100000);
        } else {
            estimatedGas = response.getAmountUsed().add(BigInteger.valueOf(10000));
        }


        // Create rawtransaction
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce_in_BigInteger, gas_Price, estimatedGas, token_address, approve_data);

        // Sign transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        // Send transaction
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

        // Get transaction Hash
        String transactionHash = ethSendTransaction.getTransactionHash();
        System.out.println("Transaction Hash: " + transactionHash);
        String result;
        if(transactionHash == null){
            String errorTransferMesg = ethSendTransaction.getError().getMessage();
            System.out.println(errorTransferMesg);
            result = errorTransferMesg;

        }
        else {

            // Get result of transaction receipt
            while (true) {
                EthGetTransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(transactionHash).send();
                if (transactionReceipt.getResult() != null) {
                    System.out.println("Tx status: " + transactionReceipt.getResult().getStatus());
                    result = transactionReceipt.getResult().getStatus();
                    break;
                }
                Thread.sleep(10000);
            }
        }
        return result; //Expected 0x1
    }

}
