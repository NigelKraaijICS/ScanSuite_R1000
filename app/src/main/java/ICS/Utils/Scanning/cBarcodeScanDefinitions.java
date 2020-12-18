package ICS.Utils.Scanning;

import java.util.ArrayList;
import java.util.List;

public class cBarcodeScanDefinitions {
    public static List<String> getBarcodeActions() {
        List<String> barcodeActionsObl = new ArrayList<>();
        barcodeActionsObl.add("com.datalogic.decodewedge.decode_action");
        barcodeActionsObl.add("nl.icsvertex.scansuite.decodewedge.decode_action");
        barcodeActionsObl.add("nl.icsvertex.scansuite.action");
        barcodeActionsObl.add("nl.icsvertex.scansuite.decodewedge.decode_action.tc55");
        barcodeActionsObl.add("nl.icsvertex.scan.action");
        barcodeActionsObl.add("nl.icsvertex.shopscan.decodewedge.decode_action");
        barcodeActionsObl.add("com.proglove.api.BARCODE");
        barcodeActionsObl.add("nl.icsvertex.honeywell.action");
        return barcodeActionsObl;
    }
    public static List<String> getBarcodeCategories() {
        List<String> barcodeCategoriesObl = new ArrayList<>();
        barcodeCategoriesObl.add("com.datalogic.decodewedge.decode_category");
        barcodeCategoriesObl.add("nl.icsvertex.scansuite.decodewedge.decode_category");
        barcodeCategoriesObl.add("nl.icsvertex.scansuite.category");
        barcodeCategoriesObl.add("nl.icsvertex.scan.category");
        barcodeCategoriesObl.add("nl.icsvertex.shopscan.decodewedge.decode_category");
        barcodeCategoriesObl.add("nl.icsvertex.honeywell.category");
        return barcodeCategoriesObl;
    }

    public static String BARCODEINTENT_DATALOGIC_ACTION="com.datalogic.decodewedge.decode_action";
    public static String BARCODEINTENT_DATALOGIC_EXTRABARCODETYPE="com.datalogic.decode.intentwedge.barcode_type";
    public static String BARCODEINTENT_DATALOGIC_EXTRABARCODE="com.datalogic.decode.intentwedge.barcode_string";

    public static String BARCODEINTENT_ZEBRA_ACTION="nl.icsvertex.shopscan.decodewedge.decode_action";
    public static String BARCODEINTENT_ZEBRA_EXTRABARCODETYPE="com.symbol.datawedge.label_type";
    public static String BARCODEINTENT_ZEBRA_EXTRABARCODE="com.symbol.datawedge.data_string";

    public static String BARCODEINTENT_TC55_ACTION="nl.icsvertex.shopscan.decodewedge.decode_action.tc55";
    public static String BARCODEINTENT_TC55_EXTRABARCODETYPE="com.motorolasolutions.emdk.datawedge.label_type";
    public static String BARCODEINTENT_TC55_EXTRABARCODE="com.motorolasolutions.emdk.datawedge.data_string";

    public static String BARCODEINTENT_PROGLOVE_ACTION="com.proglove.api.BARCODE";
    public static String BARCODEINTENT_PROGLOVE_EXTRABARCODETYPE="com.proglove.api.extra.BARCODE_SYMBOLOGY";
    public static String BARCODEINTENT_PROGLOVE_EXTRABARCODE="com.proglove.api.extra.BARCODE_DATA";

    public static String BARCODEINTENT_HONEYWELL_ACTION="nl.icsvertex.honeywell.action";
    public static String BARCODEINTENT_HONEYWELL_EXTRABARCODETYPE="codeId";
    public static String BARCODEINTENT_HONEYWELL_EXTRABARCODE="data";

    public static String pGetHoneyWellBarcodeTypeStr(String pvScannedCodeStr) {
        switch (pvScannedCodeStr) {
            case "G":
                return "BC412";
            case "H":
                return "HAN_XIN_CODE";
            case "I":
                return "GS1_128";
            case "J":
                return "JAPAN_POST";
            case "K":
                return "KIX_CODE";
            case "L":
                return "PLANET_CODE";
            case "M":
                return "USPS_4_STATE";
            case "N":
                return "UPU_4_STATE";
            case "O":
                return "OCR";
            case "P":
                return "POSTNET";
            case "Q":
                return "HK25";
            case "R":
                return "MICROPDF";
            case "S":
                return "SECURE_CODE";
            case "T":
                return "TLC39";
            case "U":
                return "ULTRACODE";
            case "V":
                return "CODABLOCK_A";
            case "W":
                return "POSICODE";
            case "X":
                return "GRID_MATRIX";
            case "Y":
                return "NEC25";
            case "Z":
                return "MESA";
            case "a":
                return "CODABAR";
            case "b":
                return "CODE39";
            case "c":
                return "UPCA";
            case "d":
                return "EAN13";
            case "e":
                return "I25";
            case "f":
                return "S25";
            case "g":
                return "MSI";
            case "h":
                return "CODE11";
            case "i":
                return "CODE93";
            case "j":
                return "CODE128";
            case "k":
                return "UNUSED";
            case "l":
                return "CODE49";
            case "m":
                return "M25";
            case "n":
                return "PLESSEY";
            case "o":
                return "CODE16K";
            case "p":
                return "CHANNELCODE";
            case "q":
                return "CODABLOCK_F";
            case "r":
                return "PDF417";
            case "s":
                return "QRCODE";
            case "-":
                return "MICROQR_ALT";
            case "t":
                return "TELEPEN";
            case "u":
                return "CODEZ";
            case "v":
                return "VERICODE";
            case "w":
                return "DATAMATRIX";
            case "x":
                return "MAXICODE";
            case "y":
                return "GS1_DATABAR";
            case "{":
                return "GS1_DATABAR_LIM";
            case "}":
                return "GS1_DATABAR_EXP";
            case "z":
                return "AZTEC_CODE";
            default:
                return "UNKNOWN";
        }
    }


}
