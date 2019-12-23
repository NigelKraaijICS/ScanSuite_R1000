package SSU_WHS.Intake.IntakeorderMATLines;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.cIntakeorderMATLineBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATLine {

    public Integer lineNoInt;
    public Integer getLineNoInt() {
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

    public String binCodeHandledStr;
    public String getBinCodeHandledStr() {
        return binCodeHandledStr;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    public String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    public Boolean isPartOfMultiLineOrderBln;
    public Boolean getPartOfMultiLineOrderBln() {
        return isPartOfMultiLineOrderBln;
    }

    public int sortingSequenceNoInt;
    public int getSortingSequenceNoInt() {
        return sortingSequenceNoInt;
    }

    public String vendorItemNo;
    public String getVendorItemNoStr() {
        return vendorItemNo;
    }

    public String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    public String actionTypeCodeStr;
    public String getActionTypeCodeStr() {return actionTypeCodeStr;}

    public Integer localStatusInt;
    public Integer getLocalStatusInt() {
        return localStatusInt;
    }

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

    public int sourceTypeInt;
    public int getSourceTypeInt(){return  this.sourceTypeInt;}

    public  List<cIntakeorderBarcode> barcodesObl;

    public static ArrayList<cIntakeorderMATLineBarcode> allLineBarcodesObl;

    public List<cIntakeorderMATLineBarcode> handledBarcodesObl(){


        List<cIntakeorderMATLineBarcode> resultObl;
        resultObl = new ArrayList<>();

        if (cIntakeorderMATLineBarcode.allMATLineBarcodesObl == null) {
            return  resultObl;
        }

        for (cIntakeorderMATLineBarcode intakeorderMATLineBarcode :cIntakeorderMATLineBarcode.allMATLineBarcodesObl ) {

            int result = Long.compare(intakeorderMATLineBarcode.getLineNoLng(), this.getLineNoInt().longValue());
            if (result == 0) {
                resultObl.add(intakeorderMATLineBarcode);
            }
        }

        return  resultObl;
    }

    public cIntakeorderMATLineEntity intakeorderMATLineEntity;
    public boolean indatabaseBln;

    public static cIntakeorderMATLineViewModel gIntakeorderMATLineViewModel;
    public static cIntakeorderMATLineViewModel getIntakeorderMATLineViewModel() {
        if (gIntakeorderMATLineViewModel == null) {
            gIntakeorderMATLineViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cIntakeorderMATLineViewModel.class);
        }
        return gIntakeorderMATLineViewModel;
    }

    public static cIntakeorderMATLineAdapter gIntakeorderMATLineAdapter;
    public static cIntakeorderMATLineAdapter getIntakeorderMATLineAdapter() {
        if (gIntakeorderMATLineAdapter == null) {
            gIntakeorderMATLineAdapter = new cIntakeorderMATLineAdapter();
        }
        return gIntakeorderMATLineAdapter;
    }

    public static List<cIntakeorderMATLine> allIntakeorderMATLinesObl;
    public static cIntakeorderMATLine currentIntakeorderMATLine;

    //Region Constructor

    public cIntakeorderMATLine(JSONObject pvJsonObject) {

        this.intakeorderMATLineEntity = new cIntakeorderMATLineEntity(pvJsonObject);
        this.lineNoInt = this.intakeorderMATLineEntity.getLineNoInt();
        this.itemNoStr = this.intakeorderMATLineEntity.getItemNoStr();
        this.variantCodeStr = this.intakeorderMATLineEntity.getVariantCodeStr();
        this.descriptionStr = this.intakeorderMATLineEntity.getDescriptionStr();
        this.description2Str = this.intakeorderMATLineEntity.getDescription2Str();
        this.binCodeStr= this.intakeorderMATLineEntity.getBincodeStr();
        this.binCodeHandledStr = this.intakeorderMATLineEntity.getBincodehandledStr();
        this.quantityDbl = this.intakeorderMATLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.intakeorderMATLineEntity.getQuantityHandledDbl();
        this.sourceNoStr = this.intakeorderMATLineEntity.getSourceNoStr();
        this.destinationNoStr = this.intakeorderMATLineEntity.getDestinationNoStr();
        this.isPartOfMultiLineOrderBln = cText.pStringToBooleanBln(this.intakeorderMATLineEntity.getIspartOfMultilLneOrderStr(), false);
        this.sortingSequenceNoInt = this.intakeorderMATLineEntity.getSortingSequenceStr();
        this.vendorItemNo = this.intakeorderMATLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.intakeorderMATLineEntity.getVendorItemDescriptionStr();
        this.statusInt = this.intakeorderMATLineEntity.getStatusInt();
        this.localStatusInt =  this.intakeorderMATLineEntity.getLocalStatusInt();
        this.actionTypeCodeStr = this.getActionTypeCodeStr();
        this.extraField1Str =  this.intakeorderMATLineEntity.getExtraField1Str();
        this.extraField2Str = this.intakeorderMATLineEntity.getExtraField2Str();
        this.extraField3Str =  this.intakeorderMATLineEntity.getExtraField3Str();
        this.extraField4Str =  this.intakeorderMATLineEntity.getExtraField4Str();
        this.extraField5Str =  this.intakeorderMATLineEntity.getExtraField5Str();
        this.extraField6Str =  this.intakeorderMATLineEntity.getExtraField6Str();
        this.extraField7Str =  this.intakeorderMATLineEntity.getExtraField7Str();
        this.extraField8Str =  this.intakeorderMATLineEntity.getExtraField8Str();
        this.sourceTypeInt = this.intakeorderMATLineEntity.getSourceTypeInt();
    }

    public cIntakeorderMATLine(cIntakeorderMATLineEntity pvIntakeorderMATLineEntity) {
        this.intakeorderMATLineEntity = pvIntakeorderMATLineEntity;
        this.lineNoInt = this.intakeorderMATLineEntity.getLineNoInt();
        this.itemNoStr = this.intakeorderMATLineEntity.getItemNoStr();
        this.variantCodeStr = this.intakeorderMATLineEntity.getVariantCodeStr();
        this.descriptionStr = this.intakeorderMATLineEntity.getDescriptionStr();
        this.description2Str = this.intakeorderMATLineEntity.getDescription2Str();
        this.binCodeStr= this.intakeorderMATLineEntity.getBincodeStr();
        this.binCodeHandledStr = this.intakeorderMATLineEntity.getBincodehandledStr();
        this.quantityDbl = this.intakeorderMATLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.intakeorderMATLineEntity.getQuantityHandledDbl();
        this.sourceNoStr = this.intakeorderMATLineEntity.getSourceNoStr();
        this.destinationNoStr = this.intakeorderMATLineEntity.getDestinationNoStr();
        this.isPartOfMultiLineOrderBln = cText.pStringToBooleanBln(this.intakeorderMATLineEntity.getIspartOfMultilLneOrderStr(), false);
        this.sortingSequenceNoInt = this.intakeorderMATLineEntity.getSortingSequenceStr();
        this.vendorItemNo = this.intakeorderMATLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.intakeorderMATLineEntity.getVendorItemDescriptionStr();
        this.statusInt = this.intakeorderMATLineEntity.getStatusInt();
        this.actionTypeCodeStr = this.getActionTypeCodeStr();
        this.localStatusInt =  this.intakeorderMATLineEntity.getLocalStatusInt();
        this.extraField1Str =  this.intakeorderMATLineEntity.getExtraField1Str();
        this.extraField2Str = this.intakeorderMATLineEntity.getExtraField2Str();
        this.extraField3Str =  this.intakeorderMATLineEntity.getExtraField3Str();
        this.extraField4Str =  this.intakeorderMATLineEntity.getExtraField4Str();
        this.extraField5Str =  this.intakeorderMATLineEntity.getExtraField5Str();
        this.extraField6Str =  this.intakeorderMATLineEntity.getExtraField6Str();
        this.extraField7Str =  this.intakeorderMATLineEntity.getExtraField7Str();
        this.extraField8Str =  this.intakeorderMATLineEntity.getExtraField8Str();
        this.sourceTypeInt = this.intakeorderMATLineEntity.getSourceTypeInt();
    }
    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        cIntakeorderMATLine.getIntakeorderMATLineViewModel().insert(this.intakeorderMATLineEntity);
        this.indatabaseBln = true;

        if (cIntakeorderMATLine.allIntakeorderMATLinesObl == null) {
            cIntakeorderMATLine.allIntakeorderMATLinesObl = new ArrayList<>();
        }
        cIntakeorderMATLine.allIntakeorderMATLinesObl.add(this);
        return true;
    }


    public boolean pHandledIndatabaseBln(){


        if (this.mUpdateQuanitityHandled(this.quantityHandledDbl)  == false) {
            return  false;
        }

        if (this.mUpdateQuanitity(this.quantityDbl)  == false) {
            return  false;
        }

        if (this.mUpdateBinCodeHandled(this.binCodeHandledStr)  == false) {
            return  false;
        }

        return  true;

    }

    public boolean pErrorSendingBln() {
        return true;
    }

    public boolean pAddOrUpdateLineBarcodeBln(cIntakeorderBarcode pvIntakeorderBarcode){


        Boolean matchBln = false;

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            cIntakeorderMATLineBarcode intakeorderMATLineBarcode = new cIntakeorderMATLineBarcode(this.getLineNoInt().longValue(),
                                                                                                  pvIntakeorderBarcode.getBarcodeStr(),
                                                                                                  pvIntakeorderBarcode.getQuantityPerUnitOfMeasureDbl());
            intakeorderMATLineBarcode.pInsertInDatabaseBln();
            return true;
        }

        for (cIntakeorderMATLineBarcode intakeorderMATLineBarcode : this.handledBarcodesObl()) {

            if (intakeorderMATLineBarcode.getBarcodeStr().equalsIgnoreCase(pvIntakeorderBarcode.getBarcodeStr())) {
                matchBln = true;
                intakeorderMATLineBarcode.quantityHandledDbl += pvIntakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
                intakeorderMATLineBarcode.pUpdateAmountInDatabaseBln();
                return  true;
            }
        }

        if (! matchBln) {
            cIntakeorderMATLineBarcode intakeorderMATLineBarcode = new cIntakeorderMATLineBarcode(this.getLineNoInt().longValue(),
                                                                                                  pvIntakeorderBarcode.getBarcodeStr(),
                                                                                                  pvIntakeorderBarcode.getQuantityPerUnitOfMeasureDbl());
            intakeorderMATLineBarcode.pInsertInDatabaseBln();
            return true;
        }

        return  false;
    }



    public static List<cIntakeorderMATLine> pGetIntakeorderMATLinesFromDatabasObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl = cIntakeorderMATLine.getIntakeorderMATLineViewModel().pGetIntakeorderMATLinesFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl) {
            cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(intakeorderMATLine);
        }

        return resultObl;
    }

    public static boolean pTruncateTableBln() {
        cIntakeorderMATLine.getIntakeorderMATLineViewModel().deleteAll();
        return true;
    }

    public cIntakeorderBarcode pGetBarcodeForLine(cBarcodeScan pvBarcodeScan){

        if (pvBarcodeScan.getBarcodeOriginalStr().isEmpty()) {
            return  null;
        }


        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        for (cIntakeorderBarcode intakeorderBarcode : this.barcodesObl) {
            if (intakeorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                intakeorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                return  intakeorderBarcode;
            }
        }

        return  null;

    }

    public  List<cIntakeorderBarcode> pGetBarcodesObl(){

        //If barcodes already filled, then we are done
        if (this.barcodesObl != null) {
            return this.barcodesObl;
        }

        // Get barcodes via IntakeorderBarcode class
        this.barcodesObl = cIntakeorderBarcode.pGetIntakeBarcodesViaVariantAndItemNoObl(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        return  this.barcodesObl;
    }

    private boolean mUpdateQuanitityHandled(double pvQuantityHandledDbl) {

        boolean resultBln;
        resultBln =   cIntakeorderMATLine.getIntakeorderMATLineViewModel().pUpdateQuantityHandledBln(pvQuantityHandledDbl);

        if (resultBln == false) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledDbl;
        return true;

    }

    private boolean mUpdateQuanitity(double pvQuantityDbl) {

        boolean resultBln;
        resultBln =   cIntakeorderMATLine.getIntakeorderMATLineViewModel().pUpdateQuantityHandledBln(pvQuantityDbl);

        if (resultBln == false) {
            return  false;
        }

        this.quantityDbl = pvQuantityDbl;
        return true;

    }

    private boolean mUpdateBinCodeHandled(String pvBinCodeHandledStr) {

        boolean resultBln;
        resultBln =   cIntakeorderMATLine.getIntakeorderMATLineViewModel().pUpdateBinCodeHandledBln(pvBinCodeHandledStr);

        if (resultBln == false) {
            return  false;
        }

        this.binCodeHandledStr = pvBinCodeHandledStr;
        return true;

    }

}
