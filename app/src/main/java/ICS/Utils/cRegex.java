package ICS.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cRegex {
    public static Boolean pCheckRegexBln(String pvPatternStr, String pvInputStr) {
        boolean l_resultBln;
        Pattern pattern = Pattern.compile(pvPatternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pvInputStr);
        l_resultBln = matcher.matches();
        return l_resultBln;
    }
    public static String pStripRegexPrefixStr(String pvInputStr) {
        String l_resultStr;

        if (pvInputStr.isEmpty()) {
            return  "";
        }

        if (pvInputStr.substring(0,1).equals("#") && pvInputStr.substring(4,5).equals("#") ) {
            l_resultStr = pvInputStr.substring(5);
        }
        else {
            l_resultStr = pvInputStr;
        }

        return l_resultStr;
    }
    public static Boolean pHasPrefix(String pvInputStr) {
        boolean resultBln;
        if (pvInputStr.substring(0,1).equals("#") && pvInputStr.substring(4,5).equals("#") ) {
            resultBln = true;
        }
        else {
            resultBln = false;
        }
        return resultBln;
    }
    public static String pGetPrefix(String pvInputStr) {
        String resultStr;
        if (pvInputStr.substring(0,1).equals("#") && pvInputStr.substring(4,5).equals("#") ) {
            resultStr = pvInputStr.substring(1,4);
        }
        else {
            resultStr = "";
        }
        return resultStr;
    }

    public static String pGetPrefixFromLayout(String pvInputStr) {
        String resultStr;
        if (pvInputStr.substring(1,2).equals("#") && pvInputStr.substring(5,6).equals("#") ) {
            resultStr = pvInputStr.substring(2,5);
        }
        else {
            resultStr = "";
        }
        return resultStr;
    }
}