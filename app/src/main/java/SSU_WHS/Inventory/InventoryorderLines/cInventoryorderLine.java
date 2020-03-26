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
    private int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    private String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    private String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() { return vendorItemDescriptionStr; }

    private int statusInt;
    public int getStatusInt() { return statusInt; }

    private String handeledTimeStampStr;
    public String getHandeledTimeStampStr() { return handeledTimeStampStr; }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() { return quantityHandledDbl; }

    private Double quantityHandledAllScannersDbl;
    public Double getQuantityHandledAllScannersDbl() { return quantityHandledAllScannersDbl; }

    private String extraField1Str;
    public String getExtraField1Str() {
        return extraField1Str;
    }

    private String extraField2Str;
    public String getExtraField2Str() {
        return extraField2Str;
    }

    private String extraField3Str;
    public String getExtraField3Str() {
        return extraField3Str;
    }

    private String extraField4Str;
    public String getExtraField4Str() {
        return extraField4Str;
    }

    private String extraField5Str;
    public String getExtraField5Str() {
        return extraField5Str;
    }

    private String extraField6Str;
    public String getExtraField6Str() {
        return extraField6Str;
    }

    private String extraField7Str;
    public String getExtraField7Str() {
        return extraField7Str;
    }

    private String extraField8Str;
    public String getExtraField8Str() {
        return extraField8Str;
    }

    public String getItemNoAndVariantCodeStr(){
        return  this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    public String getVendorItemNoAndVendorDescriptionStr(){
        return  this.getVendorItemNoStr() + " " + this.getVendorItemDescriptionStr();
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
        this.extraField1Str =  this.inventoryorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.inventoryorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.inventoryorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.inventoryorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.inventoryorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.inventoryorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.inventoryorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.inventoryorderLineEntity.getExtraField8Str();
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

    public cInventoryorderLineBarcode pAddLineBarcode(String pvBarcodeStr, Double pvQuantityDbl) {

        cInventoryorderLineBarcode inventoryorderLineBarcode = new cInventoryorderLineBarcode((long) this.getLineNoInt(),pvBarcodeStr,pvQuantityDbl);

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

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            return  result;
        }
    }





}
