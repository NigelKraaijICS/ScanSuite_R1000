package SSU_WHS.Inventory.InventoryorderLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cInventoryorderLine {

    public cInventoryorderLineEntity inventoryorderLineEntity;

    public static List<cInventoryorderLine> allLinesObl;
    public static cInventoryorderLine currentInventoryOrderLine;

    public List<cInventoryorderBarcode> barcodesObl(){

        List<cInventoryorderBarcode> resultObl = new ArrayList<>();

        if (cInventoryorder.currentInventoryOrder.barcodesObl() == null || cInventoryorder.currentInventoryOrder.barcodesObl().size() == 0) {
            return  resultObl;
        }

        //Loop through all barcodes, and if item matches add it to the list
        for (cInventoryorderBarcode inventoryorderBarcode : cInventoryorder.currentInventoryOrder.barcodesObl()) {
                        if (inventoryorderBarcode.getItemNoStr().equalsIgnoreCase(this.getItemNoStr()) &&
                            inventoryorderBarcode.getVariantCodeStr().equalsIgnoreCase(this.getVariantCodeStr())) {
                resultObl.add(inventoryorderBarcode);
            }
        }

        return  resultObl;

    }
    public List<cInventoryorderLineBarcode> lineBarcodesObl(){

        List<cInventoryorderLineBarcode> resultObl = new ArrayList<>();

        if (cInventoryorderLineBarcode.allLineBarcodesObl == null || cInventoryorderLineBarcode.allLineBarcodesObl.size() == 0) {
            return  resultObl;
        }

        //Loop through all barcodes, and if item matches add it to the list
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : cInventoryorderLineBarcode.allLineBarcodesObl) {
            if (cText.pLongToStringStr(inventoryorderLineBarcode.getLineNoLng()).equalsIgnoreCase(cText.pIntToStringStr(this.getLineNoInt()))) {
                resultObl.add(inventoryorderLineBarcode);
            }
        }

        return  resultObl;
    }

    private cInventoryorderLineViewModel getInventoryorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineViewModel.class);
    }

    //Region Public Properties
    private final int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    private final String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private final String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    private final String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private final String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private final String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private final Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    private final String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    private final String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() { return vendorItemDescriptionStr; }

    private final int statusInt;
    public int getStatusInt() { return statusInt; }

    private final String handeledTimeStampStr;
    public String getHandeledTimeStampStr() { return handeledTimeStampStr; }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() { return quantityHandledDbl; }

    private final Double quantityHandledAllScannersDbl;
    public Double getQuantityHandledAllScannersDbl() { return quantityHandledAllScannersDbl; }

    public String getItemNoAndVariantCodeStr(){
        return  this.getItemNoStr() + "~"  + this.getVariantCodeStr();
    }

    public cArticleImage articleImage;

    //End Region Public Properties

    //Region Constructor
    public cInventoryorderLine(JSONObject pvJsonObject) {
        this.inventoryorderLineEntity = new cInventoryorderLineEntity(pvJsonObject);
        this.lineNoInt = this.inventoryorderLineEntity.getLineNoInt();
        this.itemNoStr = this.inventoryorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.inventoryorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.inventoryorderLineEntity.getDescriptionStr();
        this.description2Str = this.inventoryorderLineEntity.getDescription2Str();
        this.binCodeStr = this.inventoryorderLineEntity.getBincodeStr();
        this.quantityDbl = this.inventoryorderLineEntity.getQuantityDbl();
        this.vendorItemNoStr = this.inventoryorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.inventoryorderLineEntity.getVendorItemDescriptionStr();
        this.statusInt = this.inventoryorderLineEntity.getStatusInt();
        this.handeledTimeStampStr = this.inventoryorderLineEntity.getHandledtimestampStr();
        this.quantityHandledDbl = this.inventoryorderLineEntity.getQuantityHandledDbl();
        this.quantityHandledAllScannersDbl = this.inventoryorderLineEntity.getQuantityHandledAllScannersDbl();
    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        getInventoryorderLineViewModel().insert(this.inventoryorderLineEntity);

        if (cInventoryorderLine.allLinesObl == null){
            cInventoryorderLine.allLinesObl = new ArrayList<>();
        }
        cInventoryorderLine.allLinesObl.add(this);
        return  true;
    }

    public static void pInsertAllInDatabase(List<cInventoryorderLine> pvInventoryOrderLinesObl, List<cInventoryorderLineEntity> pvInventoryOrderLineEntityObl ) {

        cInventoryorderLine.allLinesObl.addAll(pvInventoryOrderLinesObl);
        cInventoryorderLineViewModel  inventoryorderLineViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineViewModel.class);
        inventoryorderLineViewModel.insertAll (pvInventoryOrderLineEntityObl);

    }

    public static boolean pTruncateTableBln(){

        cInventoryorderLineViewModel  inventoryorderLineViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineViewModel.class);
        inventoryorderLineViewModel.deleteAll();
        return true;
    }

    public cInventoryorderLineBarcode pAddLineBarcode(String pvBarcodeStr, Double pvQuantityDbl, boolean pvSkipQuantityBln) {

        cInventoryorderLineBarcode inventoryorderLineBarcode = new cInventoryorderLineBarcode((long) this.getLineNoInt(),pvBarcodeStr,pvQuantityDbl);

        if (pvSkipQuantityBln){
            inventoryorderLineBarcode.quantityHandledDbl = 0;
        }

        if (cInventoryorderLineBarcode.allLineBarcodesObl == null){
            cInventoryorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }

        cInventoryorderLineBarcode.allLineBarcodesObl.add(inventoryorderLineBarcode);

        return  inventoryorderLineBarcode;
    }

    public boolean pGetArticleImageBln(){

        if (this.articleImage != null) {
            return  true;
        }

        this.articleImage = cArticleImage.pGetArticleImageByItemNoAndVariantCode(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.articleImage != null){
            return  true;
        }

        cWebresult Webresult;

        cArticleImageViewModel articleImageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
        Webresult = articleImageViewModel.pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        if (Webresult.getResultDtt().size() == 1) {
            cArticleImage articleImage = new cArticleImage(Webresult.getResultDtt().get(0));
            articleImage.pInsertInDatabaseBln();
            this.articleImage = articleImage;
            return true;
        }


        return  false;

    }

    public boolean pSaveLineViaWebserviceBln (){

        cWebresult WebResult;

        WebResult =  getInventoryorderLineViewModel().pSaveLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
                return  true;
            }
        else {
            cInventoryorder.currentInventoryOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINESAVE);
            return  false;
        }
    }

    public void pUpdateQuantityInDatabase(){
        getInventoryorderLineViewModel().pUpdateQuantity();
    }

    public cInventoryorderLineBarcode pGetLineBarcodeByScannedBarcode(cBarcodeScan pvBarcodeScan) {


        if (this.lineBarcodesObl().size() == 0) {
            this.quantityHandledDbl = 0.0;
        }

        //We scanned a barcodeStr that belongs to the current article, so check if we already have a line barcodeStr
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : this.lineBarcodesObl()) {

            //We have a match, so set the current line
            if (inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                return inventoryorderLineBarcode;
            }
        }

        return  null;
    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        WebResult =  getInventoryorderLineViewModel().pResetLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            for (cInventoryorderLineBarcode inventoryorderLineBarcode : this.lineBarcodesObl()) {
                inventoryorderLineBarcode.pDeleteFromDatabaseBln();
            }

            //Reset this line
            this.quantityHandledDbl = 0.0;
            this.pUpdateQuantityInDatabase();

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
        }
        return  result;
    }

    public  boolean hasPropertysBln() {
        return this.linePropertyObl().size() != 0;
    }
    public ArrayList<cLinePropertyValue> presetValueObl;

    private  List<cLineProperty> linePropertyCachedObl;
    private List<cLineProperty> linePropertyObl() {

//        if (this.linePropertyCachedObl != null) {
//            return  this.linePropertyCachedObl;
//        }

        this.linePropertyCachedObl = new ArrayList<>();

        if (cLineProperty.allLinePropertysObl == null || cLineProperty.allLinePropertysObl.size() == 0) {
            return  this.linePropertyCachedObl;
        }

        for (cLineProperty lineProperty :cLineProperty.allLinePropertysObl ) {
            if (lineProperty.getLineNoInt().equals(this.getLineNoInt())) {
                this.linePropertyCachedObl.add(lineProperty);
            }
        }
        return  this.linePropertyCachedObl;
    }

    private  List<cLineProperty> linePropertyNoInputCachedObl;
    public List<cLineProperty> linePropertyNoInputObl() {

        if (this.linePropertyNoInputCachedObl != null) {
            return  this.linePropertyNoInputCachedObl;
        }

        this.linePropertyNoInputCachedObl = new ArrayList<>();

        if (this.linePropertyObl() == null || this.linePropertyObl().size() == 0) {
            return  this.linePropertyNoInputCachedObl;
        }

        for (cLineProperty lineProperty :this.linePropertyObl()) {
            if (!lineProperty.getIsInputBln() &&  !lineProperty.getIsRequiredBln()) {
                this.linePropertyNoInputCachedObl.add(lineProperty);
            }
        }

        return  this.linePropertyNoInputCachedObl;
    }

    public List<cLineProperty> linePropertyInputObl() {

        List<cLineProperty> resultObl = new ArrayList<>();

        if (this.linePropertyObl() == null || this.linePropertyObl().size() == 0) {
            return  resultObl;
        }

        for (cLineProperty lineProperty :this.linePropertyObl()) {
            if (lineProperty.getIsInputBln() &&  lineProperty.getIsRequiredBln()) {
                resultObl.add(lineProperty);
            }
        }

        return  resultObl;
    }

    public  cLineProperty getLineProperty(String pvPropertyCodeStr){

        if (this.linePropertyInputObl().size() == 0) {
            return  null;
        }

        for (cLineProperty lineProperty : this.linePropertyObl() ) {
            if (lineProperty.getLineNoInt().equals(this.getLineNoInt()) && lineProperty.getPropertyCodeStr().equalsIgnoreCase(pvPropertyCodeStr)) {
                return lineProperty;
            }
        }

        return  null;

    }

    public  List<cLinePropertyValue> linePropertyValuesObl() {

        List<cLinePropertyValue> resultObl = new ArrayList<>();

        for (cLineProperty inputLineProperty : this.linePropertyInputObl()) {
            resultObl.addAll(inputLineProperty.propertyValueObl());
        }

        return  resultObl;

    }



}
