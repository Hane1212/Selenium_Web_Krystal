package API.common;

import API.accounts.balancesAPI;
import API.token.tokenListAPI;
import utils.Util;

import java.util.*;

public class Environment {
    public static String env = Util.getPropertyValue("ENV");
    public static String test_chain = Util.getPropertyValue("TEST_CHAIN");
//    public static String test_chain = System.getProperty("TEST_CHAIN");
//    public static String env =  System.getProperty("ENV");
    public static String uri = get_uri("API_URL");
    public static String base_url = get_uri("BASE_URL");
    public static String uri_chain = uri + test_chain.toLowerCase();
//    public static String RPC_URL = get_detail(test_chain.toLowerCase(),"RPC_URL");
//    public static String version = Util.getPropertyValue("API_VERSION");
    public static String platform = Util.getPropertyValue("PLATFORM_WALLET");
    public static String user_address = Util.getPropertyValue("USER_ADDRESS");
    public static String krystal_title = "krystal";
    public static String recipientAddress = Util.getPropertyValue("RECIPIENT_ADDRESS");

    private static String get_uri(String item){
        String api_;
        String base_url;
        String output;
        switch (env){
            case "PRODUCT":
                api_ = Util.getPropertyValue("API_PRO");
                base_url = Util.getPropertyValue("KRYSTAL_PRO");
                break;
            default:
                api_ = Util.getPropertyValue("API_DEV");
                base_url = Util.getPropertyValue("KRYSTAL_DEV");
        }
        if (item.equals("BASE_URL")){
            output = base_url;
        }else output = api_;

        return output;
    }
    

    public static class Chain {
        public String chainName;
        public long chainId;
        public String swapSpender;
        public String multiSendSpender;
        public String l1GasContract;
        public String nativeToken;
        public String RPC_URL;

        public Chain(String chainName, long chainId, String nativeToken, String RPC_URL, String swapSpender, String multiSendSpender, String l1GasContract) {
            this.chainName = chainName;
            this.chainId = chainId;
            this.nativeToken = nativeToken;
            this.RPC_URL = RPC_URL;
            this.swapSpender = swapSpender;
            this.multiSendSpender = multiSendSpender;
            this.l1GasContract = l1GasContract;
        }

        public boolean isNative(String token){
            return (token.equals("0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"));
//            return (token.equals(getTokenAddress(nativeToken)));
        }
        public String getTokenAddress(String tokenSymbol){return balancesAPI.getTokenAddress(chainName, tokenSymbol);}

    }
    public static Chain bscChain = new Chain("bsc", 56,
            "BNB",
//            "https://rpc.ankr.com/bsc",
            "https://bsc-dataseed3.binance.org/",
            "0x051DC16b2ECB366984d1074dCC07c342a9463999",
            "0xFB6BD0C00Bd348125A1F6eDc36e4B7ff5DbdDFBa",
            null);
    public static Chain etherChain = new Chain("ethereum", 1,
            "ETH",
            "https://rpc.ankr.com/eth",
            "0x70270C228c5B4279d1578799926873aa72446CcD",
            "0xFB6BD0C00Bd348125A1F6eDc36e4B7ff5DbdDFBa",
            null);
    public static Chain polygonChain = new Chain("polygon", 137,
            "MATIC",
            "https://rpc.ankr.com/polygon",
//            "https://matic-mainnet.chainstacklabs.com/",
            "0x70270c228c5b4279d1578799926873aa72446ccd",
            "0x864F01c5E46b0712643B956BcA607bF883e0dbC5",
            null);
    public static Chain cronosChain = new Chain("cronos", 25,
            "CRO",
            "https://evm-cronos.crypto.org",
            "0xf351Dd5EC89e5ac6c9125262853c74E714C1d56a",
            "0xbf8Af96D0De8AC9cdd3a70b9b7c68A467e59433B",
            null);
    public static Chain fantomChain = new Chain("fantom", 250,
            "FTM",
            "https://rpc.ankr.com/fantom",
//            "https://rpc.ftm.tools/",
            "0xf351Dd5EC89e5ac6c9125262853c74E714C1d56a",
            "0xbf8Af96D0De8AC9cdd3a70b9b7c68A467e59433B",
            null);
    public static Chain avalancheChain = new Chain("avalanche", 43114,
            "AVAX",
                "https://rpc.ankr.com/avalanche",
//            "https://api.avax.network/ext/bc/C/rpc",
            "0x8C27aBf05DE1d4847c3924566C3cBAFec6eFb42A",
            "0x864F01c5E46b0712643B956BcA607bF883e0dbC5",
            null);
    public static Chain arbitrumChain = new Chain("arbitrum", 42161,
            "ETH",
            "https://rpc.ankr.com/arbitrum",
//            "https://arb1.arbitrum.io/rpc",
            "0x864F01c5E46b0712643B956BcA607bF883e0dbC5",
            "0xfA5d8aAF312c96b42Ac4355B13D9D28fB10b305c",
            null);
    public static Chain auroraChain = new Chain("aurora", 1313161554,
            "ETH",
            "https://mainnet.aurora.dev",
            "0x864F01c5E46b0712643B956BcA607bF883e0dbC5",
            "0x55abE1518005a297d3cF76C99cB94cCA572e5c99",
            null);
    public static Chain klaytnChain = new Chain("klaytn", 8217,
            "KLAY",
            "https://rpc.ankr.com/klaytn",
//            "https://public-node-api.klaytnapi.com/v1/cypress",
            "0x864F01c5E46b0712643B956BcA607bF883e0dbC5",
            "0xB95FBBC6cA74bb9BBBFE0f881CFB6AA95B16bB93",
            null);
    public static Chain optimismChain = new Chain("optimism", 10,
            "ETH",
            "https://rpc.ankr.com/optimism",
            "0xf6f2dafa542FefAae22187632Ef30D2dAa252b4e",
            "0xDD29dDAc5E6AdA1359fC20b8DEbad2b98963e0DD",
            "0x420000000000000000000000000000000000000F");
    public static Chain getChainById(int chainId) {
        switch (chainId) {
            case 56:
                return bscChain;
            case 1:
                return etherChain;
            case 137:
                return polygonChain;
            default:
                return null;
        }
    }
    public static Chain getChainByName(String chainName) {
        switch (chainName.toLowerCase()) {
            case "bsc":
                return bscChain;
            case "ethereum":
                return etherChain;
            case "polygon":
                return polygonChain;
            case "cronos":
                return cronosChain;
            case "fantom":
                return fantomChain;
            case "avalanche":
                return avalancheChain;
            case "arbitrum":
                return arbitrumChain;
            case "aurora":
                return auroraChain;
            case "klaytn":
                return klaytnChain;
            case "optimism":
                return optimismChain;
            default:
                return null;
        }
    }

//    To get L1 fee
    public static Map<String, String> contractList;
    static {
        contractList = new HashMap<>();
        contractList.put("optimism", "0x420000000000000000000000000000000000000F");
    }

    public static List<String> ListRecipientAddress;
    static {
        ListRecipientAddress = new ArrayList<>();
        ListRecipientAddress.add("0x4420f4efa831d9fa76109c6fc9378e8759d365df");
        ListRecipientAddress.add("0x8d61ab7571b117644a52240456df66ef846cd999");
        ListRecipientAddress.add("0x7464c9a9bef3d49cf8c0dfaeb44ff5c129c34396");
        ListRecipientAddress.add("0x076567024aa84d766803ef0128dc7c58c13a6359");
       /* ListRecipientAddress.add("4420f4efa831d9fa76109c6fc9378e8759d365df");
        ListRecipientAddress.add("8d61ab7571b117644a52240456df66ef846cd999");
        ListRecipientAddress.add("7464c9a9bef3d49cf8c0dfaeb44ff5c129c34396");
        ListRecipientAddress.add("076567024aa84d766803ef0128dc7c58c13a6359");*/
    }

    public static List<String> ListInvalidRecipientAddress;
    static {
        ListInvalidRecipientAddress = new ArrayList<>();
        ListInvalidRecipientAddress.add("0x64c9a9bef3d49cf8c0dfaeb44ff5c129c34396");
        ListInvalidRecipientAddress.add("61ab7571b117644a52240456df66ef846cd999");
        ListInvalidRecipientAddress.add("0xaaa4420f4efa831d9fa76109c6fc9378e8759d365df");
        ListInvalidRecipientAddress.add("cc076567024aa84d766803ef0128dc7c58c13a6359");
    }

}
