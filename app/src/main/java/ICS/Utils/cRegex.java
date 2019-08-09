package ICS.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cRegex {
    public static Boolean p_checkRegexBln(String pv_PatternStr, String pv_InputStr) {
        Boolean l_resultBln;
        Pattern pattern = Pattern.compile(pv_PatternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pv_InputStr);
        if (matcher.matches()) {
            l_resultBln = true;
        }
        else {
            l_resultBln = false;
        }
        //l_resultBln = pv_InputStr.toUpperCase().matches(pv_PatternStr.toUpperCase());

        return l_resultBln;
    }
    public static String p_stripRegexPrefixStr(String pv_inputStr) {
        String l_resultStr;

        if (pv_inputStr.substring(0,1).equals("#") && pv_inputStr.substring(4,5).equals("#") ) {
            l_resultStr = pv_inputStr.substring(5);
        }
        else {
            l_resultStr = pv_inputStr;
        }

        return l_resultStr;
    }
    public static Boolean hasPrefix(String input) {
        Boolean resultBln;
        if (input.substring(0,1).equals("#") && input.substring(4,5).equals("#") ) {
            resultBln = true;
        }
        else {
            resultBln = false;
        }
        return resultBln;
    }
    public static String getPrefix(String input) {
        String resultStr;
        if (input.substring(0,1).equals("#") && input.substring(4,5).equals("#") ) {
            resultStr = input.substring(1,4);
        }
        else {
            resultStr = "";
        }
        return resultStr;
    }
}