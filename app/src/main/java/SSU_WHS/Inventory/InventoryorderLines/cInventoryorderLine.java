package SSU_WHS.Inventory.InventoryorderLines;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cInventoryorderLine {
    public cInventoryorderLineEntity inventoryorderLineEntity;
    public boolean indatabaseBln;

    public static List<cInventoryorderLine> allLinesObl;
    public static cInventoryorderLine currentInventoryOrderLine;

    public List<cInventoryorderLineBarcode> lineBarcodesObl;

    public static cInventoryorderLineViewModel gInventoryorderLineViewModel;
    public static cInventoryorderLineViewModel getInventoryorderLineViewModel() {
        if (gInventoryorderLineViewModel == null) {
            gInventoryorderLineViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cInventoryorderLineViewModel.class);
        }
        return gInventoryorderLineViewModel;
    }

    public static cInventoryorderLineAdapter gInventoryorderLineAdapter;
    public static cInventoryorderLineAdapter getInventoryorderLineAdapter() {
        if (gInventoryorderLineAdapter == null) {
            gInventoryorderLineAdapter = new cInventoryorderLineAdapter();
        }
        return gInventoryorderLineAdapter;
    }


    //Region Public Properties
    public int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public String itemTypeStr;
    public String getItemTypeStr() {
        return itemTypeStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    public String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() { return vendorItemDescriptionStr; }

    public int sortingSequenceNoInt;
    public int getSortingSequenceNoInt() { return sortingSequenceNoInt; }

    public int statusInt;
    public int getStatusInt() { return statusInt; }

    public String handeledTimeStampStr;
    public String getHandeledTimeStampStr() { return handeledTimeStampStr; }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() { return quantityHandledDbl; }

    public Double quantityHandledAllScannersDbl;
    public Double getQuantityHandledAllScannersDbl() { return quantityHandledAllScannersDbl; }

    public String extraField1Str;
    public String getExtraField1Str() {
        return extraField1Str;
    }

    public String extraField2Str;
    public String getExtraField2Str() {
        return extraField2Str;
    }

    public String extraField3Str;
    public String getExtraField3Str() {
        return extraField3Str;
    }

    public String extraField4Str;
    public String getExtraField4Str() {
        return extraField4Str;
    }

    public String extraField5Str;
    public String getExtraField5Str() {
        return extraField5Str;
    }

    public String extraField6Str;
    public String getExtraField6Str() {
        return extraField6Str;
    }

    public String extraField7Str;
    public String getExtraField7Str() {
        return extraField7Str;
    }

    public String extraField8Str;
    public String getExtraField8Str() {
        return extraField8Str;
    }

    public cArticleImage articleImage;

    //End Region Public Properties

    //Region Constructor
    public cInventoryorderLine(JSONObject pvJsonObject) {
        this.inventoryorderLineEntity = new cInventoryorderLineEntity(pvJsonObject);
        this.lineNoInt = this.inventoryorderLineEntity.getLineNoInt();
        this.itemNoStr = this.inventoryorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.inventoryorderLineEntity.getVariantCodeStr();
        this.itemTypeStr = this.inventoryorderLineEntity.getItemtypeStr();
        this.descriptionStr = this.inventoryorderLineEntity.getDescriptionStr();
        this.description2Str = this.inventoryorderLineEntity.getDescription2Str();
        this.binCodeStr = this.inventoryorderLineEntity.getBincodeStr();
        this.quantityDbl = this.inventoryorderLineEntity.getQuantityDbl();
        this.vendorItemNoStr = this.inventoryorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.inventoryorderLineEntity.getVendorItemDescriptionStr();
        this.sortingSequenceNoInt = this.inventoryorderLineEntity.getSortingsequencenoInt();
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

    public cInventoryorderLine(cInventoryorderLineEntity pvInventoryorderLineEntity){
        this.inventoryorderLineEntity = pvInventoryorderLineEntity;
        this.lineNoInt = this.inventoryorderLineEntity.getLineNoInt();
        this.itemNoStr = this.inventoryorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.inventoryorderLineEntity.getVariantCodeStr();
        this.itemTypeStr = this.inventoryorderLineEntity.getItemtypeStr();
        this.descriptionStr = this.inventoryorderLineEntity.getDescriptionStr();
        this.description2Str = this.inventoryorderLineEntity.getDescription2Str();
        this.binCodeStr = this.inventoryorderLineEntity.getBincodeStr();
        this.quantityDbl = this.inventoryorderLineEntity.getQuantityDbl();
        this.vendorItemNoStr = this.inventoryorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.inventoryorderLineEntity.getVendorItemDescriptionStr();
        this.sortingSequenceNoInt = this.inventoryorderLineEntity.getSortingsequencenoInt();
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
        cInventoryorderLine.getInventoryorderLineViewModel().insert(this.inventoryorderLineEntity);
        this.indatabaseBln = true;

        if (cInventoryorderLine.allLinesObl == null){
            cInventoryorderLine.allLinesObl = new ArrayList<>();
        }
        cInventoryorderLine.allLinesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cInventoryorderLine.getInventoryorderLineViewModel().deleteAll();
        return true;
    }

    public boolean pAddLineBarcodeBln(String pvBarcodeStr, Double pvQuantityDbl) {

        if (this.lineBarcodesObl == null) {
            this.lineBarcodesObl = new ArrayList<>();
        }

        cInventoryorderLineBarcode inventoryorderLineBarcode = new cInventoryorderLineBarcode((long) this.getLineNoInt(),pvBarcodeStr,pvQuantityDbl);

        if (cInventoryorderLineBarcode.allLineBarcodesObl == null){
            cInventoryorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }

        cInventoryorderLineBarcode.allLineBarcodesObl.add(inventoryorderLineBarcode);
        this.lineBarcodesObl.add(inventoryorderLineBarcode);

        return  true;
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

        Webresult = cArticleImage.getArticleImageViewModel().pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return  false;
        }

        List<JSONObject> myList = Webresult.getResultDtt();
        for (int i = 0; i < myList.size(); i++) {
            JSONObject jsonObject;
            jsonObject = myList.get(i);

            cArticleImage articleImage = new cArticleImage(jsonObject);
            articleImage.pInsertInDatabaseBln();
            this.articleImage = articleImage;
            return true;

        }
        return  false;

    }

    public boolean pSaveLineViaWebserviceBln (){

        cWebresult WebResult;

        WebResult =  cInventoryorderLine.getInventoryorderLineViewModel().pSaveLineViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
                return  true;
            }
        else {
            cInventoryorder.currentInventoryOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINESAVE);
            return  false;
        }
    }

    public boolean pUpdateQuantityInDatabaseBln(){

        boolean resultBln;
        resultBln =   cInventoryorderLine.getInventoryorderLineViewModel().pUpdateQuantityBln();

        if (resultBln == false) {
            return  false;
        }

        return true;

    }

    public cInventoryorderLineBarcode pGetLineBarcodeByScannedBarcode(String pvBarcodeStr) {

        //We scanned a barcode that belongs to the current article, so check if we already have a line barcode
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : this.lineBarcodesObl) {

            //We have a match, so set the current line
            if (inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr)) {
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

        WebResult =  cInventoryorderLine.getInventoryorderLineViewModel().pResetLineViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            //Remove line barcodes from the database
            cInventoryorderLineBarcode.getInventoryorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            this.quantityHandledDbl = 0.0;
            this.pUpdateQuantityInDatabaseBln();
            this.lineBarcodesObl.clear();

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
