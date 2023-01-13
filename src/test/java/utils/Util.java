package utils;

import API.common.Environment;
import com.opencsv.CSVReader;
import com.csvreader.CsvReader;
//import liquibase.util.csv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jnr.ffi.annotations.In;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.SkipException;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//import au.com.bytecode.opencsv.CSVReader;

public class Util {
    static String testID="NotSet";
    static String resultFolder = "./test-output";

    static int toSeconds = 1;

    public static String pathProject = System.getProperty("user.dir");
    static String pathToData = pathProject + "/data/";

    String screenshotFolder = "./screenshots/";
    protected String imagesPath;
    static String EdgeBrowser = "Edge";

    private static CSVReader csvReader;
    static WebDriver driver;
    public Util(WebDriver driver) {
        this.driver = driver;
    }

    public static String getPropertyValue(String propertyKey) {
        String value = "";
        try {
            Properties prop = new Properties();
            File configFile = new File("config.properties");
            InputStream stream = new FileInputStream(configFile);
            prop.load(stream);
            value = prop.getProperty(propertyKey);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static List<String> getListPropertyValue(String propertyKey){
        String list = getPropertyValue(propertyKey);
        List<String> lists = Arrays.asList(list.split(","));
        return lists;
    }

    public void killChrome() {
        String cmd = "killall -9 'Google Chrome'";
        try {
            Runtime.getRuntime().exec(cmd);
            wait(toSeconds);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void killBrowser(String process) {
        // Default browser is IE
        String cmd = "taskkill /F /IM "  + process  + " /T";
        try {
            Runtime.getRuntime().exec(cmd);
            wait(toSeconds);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String removeString(String elem, String target){
        return elem.replace(target,"");
    }

    /**
     * @apiNote Return format of balance which use to display on Krystal app
     * The balance format include 4 number after the first 0 (after .)
     * @param val
     * @return
     */
    public static String parseDecimalsFormat(String val){
        String result = val;
        if (val.contains(".")){
            result = val.split("\\.")[0]+".";
            String after = val.split("\\.")[1];
            if(!after.equals("0")){
                ArrayList<String> chars = new ArrayList<String> (Arrays.asList(after.split("(?!^)")));
                int i = 0;
                while (chars.get(i).equals("0")){
                    result = result + "0";
                    i++;
                }
                int count = 1;
                for (int j = i; j<chars.size(); j++){
                    if (count==5){
                        break;
                    }
                    if(count<4 ||(count == 4 && Integer.parseInt(chars.get(j)) > 0)){
                        result = result + chars.get(j);
                        count++;
                    }else count++;
                }
            }else {
                result.replace(".","");
            }
        }
        return result;
    }

    /**
     * @param value
     * @param places
     * @return value*10^places
     */
    public static double roundAvoidM(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale);
    }
    /**
     * @param value
     * @return value/10^decimals keep places after ","
     */
    public static double roundAvoidD(Double value, int decimals, int place) {
        Double scale = Math.pow(10, decimals);
        Double p = Math.pow(10, place);
        if (place>decimals){
            p = Math.pow(10, decimals-(place-decimals));
        }
        return Math.round((value/scale)*p)/p;
    }

    /**
     * @param value
     * @return value/10^decimals keep places after ","
     */
    public static double roundAvoidD(Double value, int place) {
        double scale = Math.pow(10, place);
        return value/scale;
    }

    /**
     * @param value
     * @return round place
     */
    public static double roundPlace(double value, int places){
        double scale = Math.pow(10, places);
        return Math.round(value*scale)/scale;
    }

    /**
     * @param d double number
     * @return String display of double number, Ex: 1.0E18 -> 1000000000000000000
     */
    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    public static void captureScreen(String testcase){
        File scrFile = null;
        if(driver instanceof WebDriver)
            scrFile = ((TakesScreenshot)((WebDriver)driver)).getScreenshotAs(OutputType.FILE);
        else if(driver instanceof RemoteWebDriver)
            scrFile = ((TakesScreenshot)((RemoteWebDriver)driver)).getScreenshotAs(OutputType.FILE);
        try {
            String date = String.valueOf(java.time.LocalDateTime.now());
            FileUtils.copyFile(scrFile, new File(resultFolder + "/img/" + testcase +"_"+date+ ".jpg"));
        } catch (IOException e) {

        }
    }

    /**
     * Random click on element in table
     * @
     * @param table
     * @return
     */
    public static WebElement selectItemOnTable(String table) {
        // If there's no item, test case is skippedc
        List<WebElement> allItems = driver.findElements(By.xpath(table));
        if(allItems.size() == 0)
            throw new SkipException("No items in list");
        Random randomItem = new Random();
        WebElement selectedItem = null;
        while(selectedItem == null) {
            selectedItem = allItems.get(randomItem.nextInt(allItems.size()));
        }
//		WebElement selectedItem = allItems.get(randomItem.nextInt(allItems.size()));
//		Util.sleep(2);
        return selectedItem;
    }

    /**
     * @implSpec: select item on table by name
     * @param name: expected value of row
     * @return
     */
    public static WebElement selectItemOnTableByName(By table, String name) {
        // If there's no item, test case is skippedc
        List<WebElement> allItems = driver.findElements(table);
        if(allItems.size() == 0)
            throw new SkipException("No items in list");
        WebElement selectedItem;
        Random randomItem;
        while(true) {
            randomItem = new Random();
            int num = randomItem.nextInt(allItems.size());
            selectedItem = allItems.get(num);
            String selectedItemText = selectedItem.getText();
            if(selectedItemText.contains(name)) {
                break;
            }
        }
        return selectedItem;
    }

    /**
     * Author: Hane
     * Updated: 2022-04-14
     * @return Column which contain key
     * @throws FileNotFoundException
     *
     */
    public static List<String> getColumnOfKey(String file, String key) {
        String[] csvCell = new String[] {};
        List<String> output = new ArrayList<>();
        int index = 0;
        try {
            index = getIndex(file, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            CSVReader reader = new CSVReader(new FileReader(pathToData+file));
            while ((csvCell = reader.readNext()) != null) {
                output.add(csvCell[index]);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Set<String> hashSet = new LinkedHashSet(output);
        ArrayList<String> removedDuplicates = new ArrayList(hashSet);
        return removedDuplicates;
    }

    /**
     * @return line which contain key
     * @throws FileNotFoundException
     *
     */
    public static String[] getLineOfKey(String file, String key) {
        String[] csvCell = new String[] {};
        String[] output = new String[] {};
        Boolean isExist = false;
            try {
                CSVReader reader = new CSVReader(new FileReader(pathToData + file));
                while ((csvCell = reader.readNext()) != null && isExist==false) {
                    String testID = csvCell[0];
                    if (csvCell[0].equals(key)) {
                        output = csvCell;
                        isExist = true;
                    }
                }
            } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        return output;
    }

        /**
     * Author: HuongTT
     * Updated: 2022-03-17
     * @param file source
     * @param key from header
     * @param ID of item (the first item in a line)
     * @return value which get from file based on key and ID
     * @throws IOException
     */
    public static String getValueByKey(String file, String key, String ID) throws IOException {
        String[] csvCell = new String[] {};
        String output = new String();
        int index = getIndex(file, key);
        Boolean isExist = false;
        try {
            CSVReader reader = new CSVReader(new FileReader(pathToData + file));
//            csvReader reader = new CSVReader(new InputStreamReader(new FileInputStream(pathToData + file), "Shift-JIS"), ',');
            while ((csvCell = reader.readNext()) != null && isExist==false) {
//                String testID = csvCell[0];
                if (csvCell[0].equals(ID) ) {
                    output = csvCell[index];
                    isExist = true;
                }
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-23
     * @apiNote Return message from ErrorCode.csv file based on ID
     * @param ID of message
     * @return message
     */
    public static String getMessage(String ID) {
        String mess = new String();
        try {
            mess = getValueByKey("ErrorCode.csv", "Message", ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  mess;
    }



    /**
     * Author: HuongTT
     * Updated: 2022-03-17
     * @param file source
     * @param key value from header
     * @return index of key
     * @throws IOException
     */
    private static int getIndex(String file, String key) throws IOException {
        int index=0;
        BufferedReader brTest = new BufferedReader(new FileReader(pathToData + file));
        String text = brTest.readLine();
        // Stop. text is the first line.
        String[] strArray = text.split(",");
            for (int i=0; i<strArray.length; i++){
                if (strArray[i].equals(key)){
                    index = i;
                    break;
                }
            }
        return index;
    }

    /**
     * Author: LinhDT
     * Updated: 2022-03-28
     * @param file
     * @param key
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> getListTestDataBaseOnTCID(String file, String key) {
        file = pathToData + file;
        BufferedReader brDataTest = null;
        List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();
        try {
            brDataTest = new BufferedReader(new FileReader(file));
            //Read the first line then put it into the array Header
            String text = brDataTest.readLine();
            String[] strArrayHeader = text.split(",");

            //Read the content then compare with key, if match -> put header and content into hash
            String[] strArrayContent = null;
            String strCurrentLine;

            while ((strCurrentLine = brDataTest.readLine()) != null) {
                strArrayContent = strCurrentLine.split(",");
                for (int i = 0; i < strArrayContent.length; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if (key.equals("RUNALL")){
                        for (int j = 0; j < strArrayHeader.length; j++) {
                            map.put(strArrayHeader[j], strArrayContent[j]);
                        }
                        listOfMaps.add(map);
                        break;
                    }else {
                        if (strArrayContent[i].equals(key)) {
                            for (int j = 0; j < strArrayHeader.length; j++) {
                                map.put(strArrayHeader[j], strArrayContent[j]);
                            }
                            listOfMaps.add(map);
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listOfMaps;
    }

    public static String readCSVElem(String file, String testCaseID, String item) {
        String valueOfItem = "";
        try {
            CsvReader products = new CsvReader(file);
            products.readHeaders();
            while (products.readRecord()) {
                String TC_ID = products.get("Test_ID");
                if (TC_ID.equals(testCaseID)) {
                    valueOfItem = products.get(item);
                    return valueOfItem;
                }
            }
            products.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueOfItem;
    }

    public static String readCSV(String file, String testCaseID, String item) {
        String valueOfItem = "";
        try {
            CsvReader products = new CsvReader(file);
            products.readHeaders();
            while (products.readRecord()) {
                String TC_ID = products.get("Test_ID");
                if (TC_ID.equals(testCaseID)) {
                    valueOfItem = products.get(item);
                    return valueOfItem;
                }
            }
            products.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueOfItem;
    }


    public static Object[][] merge2D(Object[][] obj1, Object[][] obj2){
        int m = obj1.length;
        int n= obj2.length;
        int size = obj2[0].length;
        Object[][] arr3 = new Object[m+n][size];
        if (m==0){
            arr3 = obj2;
        }else {
            for (int i=0; i<m+n;i++){
                for (int j=0;j<size;j++){
                    if(i<m){
                        arr3[i][j]=obj1[i][j];
                    }else {
                        arr3[i][j]=obj2[i-m][j];
                    }
                }
            }
        }
        System.out.println("Merge2DResult: " +arr3.length);
        return arr3;
    }

    public static List<String> getCommonElements(List<String> arr1, List<String> arr2){
        List<String> tmp = new ArrayList<>();
        for (int i=0; i<arr1.size(); i++){
            if (arr2.contains(arr1.get(i))){
                tmp.add(arr1.get(i));
            }
        }
        return tmp;
    }

    /**
     *
     * @param lst List of string
     * @param n number of random items which you want to get from list
     * @return random of n items
     */
    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new ArrayList<String>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    public static List<Integer> pickRandom(int max, int n){
        List<Integer> excluded = new ArrayList<>();
        for (int i = 1; i <= max ; i++) {
            excluded.add(i);
        }

        Random rand = new Random();
        List<Integer> included = new ArrayList();
        for (int i = 0; i < n; i++) {
            included.add(excluded.remove(rand.nextInt(excluded.size())));
        }
        return included;
    }

    /**
     * @param amount need to divide
     * @param val to divide
     * @return amount / val
     */
    public static String divideBigInterger(String amount, int val){
        BigInteger amount_ = new BigInteger(amount);
        amount_ = amount_.divide(new BigInteger(String.valueOf(val)));
        return amount_.toString();
    }

    public static int getRandomNumber(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static int getRandomNumber(int max){
        int random = 0;
        random = (int) (Math.random()* max) ;
        return random;
    }

}

