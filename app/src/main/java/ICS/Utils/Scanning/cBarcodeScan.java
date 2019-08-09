package ICS.Utils.Scanning;

import android.content.Intent;
import android.os.Bundle;

public class cBarcodeScan {
    public static String p_GetBarcode(Intent pv_intentInt, Boolean clean) {
        Bundle extras = pv_intentInt.getExtras();
        String l_returnBarcode = "";
        String l_barcodeStr = "";
        String l_barcodeTypeStr = "";
        if (extras != null) {
            //so who is sending us this?
            if (pv_intentInt.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_ACTION)) {
                l_barcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_EXTRABARCODE);
                l_barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_EXTRABARCODETYPE);
            }
            if (pv_intentInt.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_ACTION)) {
                l_barcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_EXTRABARCODE);
                l_barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_EXTRABARCODETYPE);
            }
            if (pv_intentInt.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_TC55_ACTION)) {
                l_barcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_TC55_EXTRABARCODE);
                l_barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_TC55_EXTRABARCODETYPE);
            }
        }
        if (clean) {
            l_returnBarcode =  m_CleanBarcodeStr(l_barcodeStr) ;
        }
        if (!clean) {
            l_returnBarcode = l_barcodeStr;
        }
        return l_returnBarcode;
    }

    private static String m_CleanBarcodeStr(String dirtybarcodeStr) {
        String l_cleanBarcodeStr = dirtybarcodeStr.replaceAll("(\\r|\\n|\\t)","");
        return l_cleanBarcodeStr;
    }
    public int checkSum(String code){
        int val=0;
        for(int i=0;i<code.length();i++){
            val+=(Integer.parseInt(code.charAt(i)+""))*((i%2==0)?1:3);
        }
        int checksum_digit = 10 - (val % 10);
        if (checksum_digit == 10) checksum_digit = 0;

        return checksum_digit;
    }

}
