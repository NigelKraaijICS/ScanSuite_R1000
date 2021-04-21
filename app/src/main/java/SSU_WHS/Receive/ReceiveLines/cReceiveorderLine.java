package SSU_WHS.Receive.ReceiveLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineViewModel;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
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

    public String handledTimeStampStr;
    public  String getHandledTimeStampStr(){ return  this.handledTimeStampStr;}

    public  List<cIntakeorderBarcode> barcodesObl;

    private cReceiveorderLineEntity receiveorderLineEntity;

    private cReceiveorderLineViewModel  getReceiveorderLineViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReceiveorderLineViewModel.class);
    }

    public static List<cReceiveorderLine> allReceiveorderLines;
    public static cReceiveorderLine currentReceiveorderLine;
    public ArrayList<cLinePropertyValue> presetValueObl;

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

    public cReceiveorderLine(JSONObject pvJsonObject, boolean pvLineCreatedBln) {

        if (pvLineCreatedBln) {
            this.receiveorderLineEntity = new cReceiveorderLineEntity(pvJsonObject, true);
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
            List<cLinePropertyValue> linesToDeleteObl = new ArrayList<>();

            if (cLinePropertyValue.allLinePropertysValuesObl != null) {
                for (cLinePropertyValue linePropertyValue : cLinePropertyValue.allLinePropertysValuesObl) {
                    if (linePropertyValue.getLineNoInt() == this.getLineNoInt() && linePropertyValue.getLineProperty().getIsRequiredBln() && linePropertyValue.getLineProperty().getIsInputBln()) {
                        linesToDeleteObl.add(linePropertyValue);
                    }
                }

                for (cLinePropertyValue linePropertyValue : linesToDeleteObl) {
                    cLinePropertyValue.allLinePropertysValuesObl.remove(linePropertyValue);
                }
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVELINRESET);
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

    private  List<cLineProperty> linePropertyCachedObl;
    private List<cLineProperty> linePropertyObl() {

        if (this.linePropertyCachedObl != null) {
            return  this.linePropertyCachedObl;
        }

        this.linePropertyCachedObl = new ArrayList<>();

        if (cIntakeorder.currentIntakeOrder.linePropertysObl() == null || cIntakeorder.currentIntakeOrder.linePropertysObl().size() == 0) {
            return  this.linePropertyCachedObl;
        }

        for (cLineProperty lineProperty :cIntakeorder.currentIntakeOrder.linePropertysObl() ) {
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
