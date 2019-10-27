package ICS.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class cText {

    //Region Public Properties

    public static String NEWLINE = "\r\n";
    public static String LABEL_VALUE_SEPARATOR = " - ";

    //End Region Public Properties

    //Region Public Methods

    public static String pPadRight(String pvInputStr, int pvLengthInt) {
        String resultStr;
        String helpStr;

        if (pvInputStr.length() > pvLengthInt) {
            helpStr = pvInputStr.substring(0, pvLengthInt);
        }
        else {
            helpStr = pvInputStr;
        }

        resultStr = String.format("%1$-" + pvLengthInt + "s", helpStr);
        return resultStr;
    }

    public static String pPadLeftStr(String pvInputStr, int pvLengthInt) {
        String resultStr;
        String helpStr;

        if (pvInputStr.length() > pvLengthInt) {
            helpStr = pvInputStr.substring(0, pvLengthInt);
        }
        else {
            helpStr = pvInputStr;
        }

        resultStr = String.format("%1$" + pvLengthInt + "s", helpStr);
        return resultStr;
    }

    public static String pConvertDateToFormattedDateStr(String pvInputStr, String pvOriginalFormatStr, String pvConvertToFormatStr) {
        String resultStr;
        Date hulpDat;

        try {
            hulpDat = new SimpleDateFormat(pvOriginalFormatStr).parse(pvInputStr);
            resultStr = new SimpleDateFormat(pvConvertToFormatStr).format(hulpDat);
        } catch (ParseException e) {
            //return original string
            resultStr = pvInputStr;
            e.printStackTrace();
        }

        return resultStr;
    }

    public static String pShortenStr(String pvInputStr, int pvMaxLengthInt) {
        String resultStr;

        if (pvInputStr.length() > pvMaxLengthInt -1) {
            resultStr = pvInputStr.substring(0, pvMaxLengthInt-1);
        }
        else {
            resultStr = pvInputStr;
        }

        return resultStr;
    }

    public static int pStringToIntegerInt(String pvInputStr) {

        int resultInt;

        if (pvInputStr.trim().isEmpty()) {
            return 0;
        }

        try {
            resultInt = (int) Double.parseDouble(pvInputStr);
        } catch (NumberFormatException e) {
            resultInt = 0;
        }

        return resultInt;
    }

    public static Double pStringToDoubleDbl(String pvInputStr) {

        Double resultDbl;

        if (pvInputStr.trim().isEmpty()) {
            return 0d;
        }

        try {
            String inputDotStr = pvInputStr.replace(",", ".");
            resultDbl = Double.parseDouble(inputDotStr);
        } catch (NumberFormatException e) {
            resultDbl = 0d;
        }

        return resultDbl;
    }

    public static Date pStringToDateStr(String pvInputStr,String pvConvertToFormatStr) {
        Date resultDat;
        try {

        resultDat = new SimpleDateFormat(pvConvertToFormatStr).parse(pvInputStr);

        } catch (ParseException e) {
            //return original string
            resultDat = null;
            e.printStackTrace();
        }

        return resultDat;
    }

    public static String pDoubleToStringStr(Double pvInputDbl) {

        String helpStr = Double.toString(pvInputDbl);
        return !helpStr.contains(".") ? helpStr : helpStr.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public static String pIntToStringStr(int pvInputInt) {
        return Integer.toString(pvInputInt);
    }

    public static Boolean pStringToBooleanBln(String pvInputStr, Boolean pvDefaultValueBln) {

        switch (pvInputStr.toUpperCase()) {
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
                return pvDefaultValueBln;
        }

    }

    public static Long pStringToLongLng(String pvInputStr) {

        Long resultLng;

        if (pvInputStr.trim().isEmpty()) {
            return 0L;
        }

        try {
            resultLng = Long.parseLong(pvInputStr);
        } catch (NumberFormatException e) {
            resultLng = 0L;
        }

        return resultLng;
    }

    public static String pLongToStringStr(Long pvInputLng) {
        String helpStr = Long.toString(pvInputLng);
        return !helpStr.contains(".") ? helpStr : helpStr.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public static String pAddSingleQuotesStr(String pvInputStr) {
        return "'" + pvInputStr + "'";
    }

    public static String pCleanStringStr(String pvInputStr) {
        return  pvInputStr.replaceAll("(\\r|\\n|\\t)","");
    }


    //End Region Public Methods


}
