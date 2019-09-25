package ICS.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cText {
    public static String NEWLINE = "\r\n";
    public static String LABEL_VALUE_SEPARATOR = " - ";

    public static String padRight(String pv_inputStr, int pv_length) {
        String l_resultStr;
        String l_helpStr;
        if (pv_inputStr.length() > pv_length) {
            l_helpStr = pv_inputStr.substring(0, pv_length);
        }
        else {
            l_helpStr = pv_inputStr;
        }
        l_resultStr = String.format("%1$-" + pv_length + "s", l_helpStr);
        return l_resultStr;
    }
    public static String padLeft(String pv_inputStr, int pv_length) {
        String l_resultStr;
        String l_helpStr;
        if (pv_inputStr.length() > pv_length) {
            l_helpStr = pv_inputStr.substring(0, pv_length);
        }
        else {
            l_helpStr = pv_inputStr;
        }
        l_resultStr = String.format("%1$" + pv_length + "s", l_helpStr);
        return l_resultStr;
    }
    public static String convertDateFromToFormat(String pv_inputDate, String pv_originalformatStr, String pv_converttoformatStr) {
        String l_resultStr;
        Date l_tempDat;

        try {
            l_tempDat = new SimpleDateFormat(pv_originalformatStr).parse(pv_inputDate);
            l_resultStr = new SimpleDateFormat(pv_converttoformatStr).format(l_tempDat);
        } catch (ParseException e) {
            //return original string
            l_resultStr = pv_inputDate;
            e.printStackTrace();
        }

        return l_resultStr;
    }
    public static String maxLength(String pv_inputStr, int pv_lengthInt) {
        String l_resultStr;
        if (pv_inputStr.length() > pv_lengthInt -1) {
            l_resultStr = pv_inputStr.substring(0, pv_lengthInt-1);
        }
        else {
            l_resultStr = pv_inputStr;
        }

        return l_resultStr;
    }
    public static int stringToInteger(String inputStr) {
        int l_resultInt;
        if (inputStr.trim().isEmpty()) {
            return 0;
        }
        try {
            l_resultInt = (int) Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            l_resultInt = 0;
        }
            return l_resultInt;
    }

    public static Double stringToDouble(String inputStr) {
        Double l_resultDbl;
        if (inputStr.trim().isEmpty()) {
            return 0d;
        }
        try {
            String inputDotStr = inputStr.replace(",", ".");
            l_resultDbl = Double.parseDouble(inputDotStr);
        } catch (NumberFormatException e) {
            l_resultDbl = 0d;
        }
        return l_resultDbl;
    }

    public static String doubleToString(Double inputDbl) {
        String helpStr = Double.toString(inputDbl);
        return !helpStr.contains(".") ? helpStr : helpStr.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public static Boolean stringToBoolean(String inputStr, Boolean defaultBln) {
        switch (inputStr.toUpperCase()) {
            case "N":
            case "FALSE":
            case "F":
                return false;
            case "J":
            case "Y":
            case "TRUE":
            case "T":
                return true;
            default:
                return defaultBln;
        }
    }

    public static Long stringToLong(String pvInput) {
        Long lResultLng;
        if (pvInput.trim().isEmpty()) {
            return 0L;
        }
        try {
            lResultLng = Long.parseLong(pvInput);
        } catch (NumberFormatException e) {
            lResultLng = 0L;
        }
        return lResultLng;
    }

    public static String longToString(Long inputLng) {
        String helpStr = Long.toString(inputLng);
        return !helpStr.contains(".") ? helpStr : helpStr.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public static String addSingleQuotes(String inputStr) {
        return "'" + inputStr + "'";
    }

    public static String cleanString(String dirtyString) {
        String cleanString = dirtyString.replaceAll("(\\r|\\n|\\t)","");
        return cleanString;
    }
    public static boolean isValidURL(String inputStr) {
        boolean result = true;
        try {
            new URL(inputStr);
        } catch (MalformedURLException e) {
            result = false;
        }
        return result;
    }
}
