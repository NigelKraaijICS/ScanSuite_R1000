package ICS.Utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ICS.cAppExtension;

public class cText {

    //Region Public Properties

    public static String NEWLINE = "\r\n";
    public static String LABEL_VALUE_SEPARATOR = " - ";

    public  enum ePadLocation{
        Left,
        Right
    }

    //End Region Public Properties

    //Region Public Methods



    public static String rPadStr(String pvInputStr,String pvPadCharacterStr,int pvLenghtInt, cText.ePadLocation pvPadLocationEnu) {

    String resultStr;
    Character padCharachterChar;

    resultStr = pvInputStr;

    if (pvPadCharacterStr.isEmpty()) {
        pvPadCharacterStr = " ";
    }

        padCharachterChar= pvPadCharacterStr.charAt(0);

    if (pvPadLocationEnu == ePadLocation.Right) {

     if (resultStr.length() > pvLenghtInt ) {
         resultStr = resultStr.substring(0,pvLenghtInt);
     }
     else {
         resultStr =  String.format("%1$" + pvLenghtInt + "s", pvInputStr).replace(' ', padCharachterChar);
     }

    }

        if (pvPadLocationEnu == ePadLocation.Left) {
            if (resultStr.length() >= pvLenghtInt) {
                resultStr = resultStr.substring(resultStr.length() - pvLenghtInt);
            }
            else {
                resultStr =  String.format("%1$" + pvLenghtInt + "s", pvInputStr).replace(' ', padCharachterChar);
            }
        }

    return  resultStr;


    }

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

    public static String pPadLeftStr(String pvInputStr, Character pvPadChar, int pvLengthInt) {
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


        //Convert the double to a big decimal
       BigDecimal bigDecimal = new BigDecimal(String.valueOf(pvInputDbl));

       //Seperate the integer value
       int intValue = bigDecimal.intValue();

       //Seperate decimals and covert them to a double
       Double decimalDbl= cText.pStringToDoubleDbl(bigDecimal.subtract(new BigDecimal(intValue)).toPlainString());

      //If we have no decimals bigger then zero, return the number without decimals
       if (decimalDbl <= 0.00) {
           return  cText.pIntToStringStr(intValue);
       }

       //We have decimals bigger then zero, so return number with two decimals
        DecimalFormat df = new DecimalFormat("0.00##");
        String resultStr = df.format(pvInputDbl);

        return  resultStr;
    }


    public static int pDoubleToInt(Double pvInputDbl) {


        //Convert the double to a big decimal
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(pvInputDbl));

        //Seperate the integer value
        int intValue = bigDecimal.intValue();

        //Seperate decimals and covert them to a double
        Double decimalDbl= cText.pStringToDoubleDbl(bigDecimal.subtract(new BigDecimal(intValue)).toPlainString());

        //If we have no decimals bigger then zero, return the number without decimals
        if (decimalDbl <= 0.00) {
            return  0;
        }

        //We have decimals bigger then zero, so return number with two decimals
        DecimalFormat df = new DecimalFormat("0.00##");
        String resultStr = df.format(pvInputDbl);

        return  cText.pStringToIntegerInt(resultStr);
    }

    public static String pIntToStringStr(int pvInputInt) {
        return Integer.toString(pvInputInt);
    }

    public static boolean pStringToBooleanBln(String pvInputStr, Boolean pvDefaultValueBln) {
        if (pvInputStr == null) {
            return pvDefaultValueBln;
        }
        switch (pvInputStr.toUpperCase()) {
            case "N":
            case "FALSE":
            case "F":
            case "0":
                return false;
            case "J":
            case "Y":
            case "TRUE":
            case "T":
            case "1":
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

    public static Boolean pCopyTextToClipboard(View view, String label) {
        if (view instanceof TextView) {
            ClipboardManager clipboard = (ClipboardManager) cAppExtension.activity.getSystemService(cAppExtension.context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, ((TextView) view).getText());
            clipboard.setPrimaryClip(clip);
            return true;
        }
        else {
            return false;
        }
    }
    //End Region Public Methods


}
