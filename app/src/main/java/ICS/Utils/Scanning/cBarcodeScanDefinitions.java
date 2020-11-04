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
        return barcodeActionsObl;
    }
    public static List<String> getBarcodeCategories() {
        List<String> barcodeCategoriesObl = new ArrayList<>();
        barcodeCategoriesObl.add("com.datalogic.decodewedge.decode_category");
        barcodeCategoriesObl.add("nl.icsvertex.scansuite.decodewedge.decode_category");
        barcodeCategoriesObl.add("nl.icsvertex.scansuite.category");
        barcodeCategoriesObl.add("nl.icsvertex.scan.category");
        barcodeCategoriesObl.add("nl.icsvertex.shopscan.decodewedge.decode_category");
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


}
