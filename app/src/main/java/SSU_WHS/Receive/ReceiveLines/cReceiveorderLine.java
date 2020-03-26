package SSU_WHS.Receive.ReceiveLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineViewModel;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cReceiveorderLine {

    private Integer lineNoInt;
    public Integer getLineNoInt() {
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

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    private String vendorItemNo;
    public String getVendorItemNoStr() {
        return vendorItemNo;
    }

    private String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    private int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private Integer localStatusInt;
    public Integer getLocalStatusInt() {
        return localStatusInt;
    }

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

    private String handledTimeStampStr;
    public  String getHandledTimeStampStr(){ return  this.handledTimeStampStr;}

    public  List<cIntakeorderBarcode> barcodesObl;

    private cReceiveorderLineEntity receiveorderLineEntity;

    private cReceiveorderLineViewModel  getReceiveorderLineViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReceiveorderLineViewModel.class);
    }

    public static List<cReceiveorderLine> allReceiveorderLines;
    public static cReceiveorderLine currentReceiveorderLine;

    //Region Constructor

    public cReceiveorderLine(JSONObject pvJsonObject) {

        this.receiveorderLineEntity = new cReceiveorderLineEntity(pvJsonObject);
        this.lineNoInt = this.receiveorderLineEntity.getSortingSequenceInt();
        this.itemNoStr = this.receiveorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.receiveorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.receiveorderLineEntity.getDescriptionStr();
        this.description2Str = this.receiveorderLineEntity.getDescription2Str();
        this.binCodeStr= this.receiveorderLineEntity.getBincodeStr();

        this.quantityDbl = this.receiveorderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.receiveorderLineEntity.getQuantityHandledDbl();

        this.vendorItemNo = this.receiveorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.receiveorderLineEntity.getVendorItemDescriptionStr();
        this.statusInt = this.receiveorderLineEntity.getStatusInt();
        this.localStatusInt =  this.receiveorderLineEntity.getLocalStatusInt();

        this.extraField1Str =  this.receiveorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.receiveorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.receiveorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.receiveorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.receiveorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.receiveorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.receiveorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.receiveorderLineEntity.getExtraField8Str();
        this.handledTimeStampStr  = this.receiveorderLineEntity.getHandledTimeStampStr();

    }

    public cReceiveorderLine(JSONObject pvJsonObject, boolean pvLineCreatedBln, int pvLineNoint) {

        if (pvLineCreatedBln) {
            this.receiveorderLineEntity = new cReceiveorderLineEntity(pvJsonObject, true);
            this.lineNoInt = pvLineNoint;
            this.itemNoStr = this.receiveorderLineEntity.getItemNoStr();
            this.variantCodeStr = this.receiveorderLineEntity.getVariantCodeStr();
            this.descriptionStr = this.receiveorderLineEntity.getDescriptionStr();
            this.description2Str = this.receiveorderLineEntity.getDescription2Str();
            this.binCodeStr= this.receiveorderLineEntity.getBincodeStr();

            this.quantityDbl = this.receiveorderLineEntity.getQuantityDbl();
            this.quantityHandledDbl = this.receiveorderLineEntity.getQuantityHandledDbl();

            this.vendorItemNo = this.receiveorderLineEntity.getVendorItemNoStr();
            this.vendorItemDescriptionStr = this.receiveorderLineEntity.getVendorItemDescriptionStr();
            this.localStatusInt =  this.receiveorderLineEntity.getLocalStatusInt();

            this.extraField1Str =  this.receiveorderLineEntity.getExtraField1Str();
            this.extraField2Str = this.receiveorderLineEntity.getExtraField2Str();
            this.extraField3Str =  this.receiveorderLineEntity.getExtraField3Str();
            this.extraField4Str =  this.receiveorderLineEntity.getExtraField4Str();
            this.extraField5Str =  this.receiveorderLineEntity.getExtraField5Str();
            this.extraField6Str =  this.receiveorderLineEntity.getExtraField6Str();
            this.extraField7Str =  this.receiveorderLineEntity.getExtraField7Str();
            this.extraField8Str =  this.receiveorderLineEntity.getExtraField8Str();

            this.handledTimeStampStr  = this.receiveorderLineEntity.getHandledTimeStampStr();
        }

    }

     //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        this.getReceiveorderLineViewModel().insert(this.receiveorderLineEntity);

        if (cReceiveorderLine.allReceiveorderLines == null) {
            cReceiveorderLine.allReceiveorderLines= new ArrayList<>();
        }
        cReceiveorderLine.allReceiveorderLines.add(this);
        return true;
    }
    public boolean pResetBln() {

        cWebresult WebResult;
        WebResult =  this.getReceiveorderLineViewModel().pResetReceiveLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            cReceiveorderLine.allReceiveorderLines.remove(this);
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receiveLinesObl.remove(this);
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET);
            return  false;
        }

    }

    public static boolean pTruncateTableBln() {

        cIntakeorderMATLineViewModel intakeorderMATLineViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderMATLineViewModel.class);
        intakeorderMATLineViewModel.deleteAll();
        return true;
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
}
