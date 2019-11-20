package SSU_WHS.Intake.IntakeorderBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;

public class cIntakeorderBarcode {
    public cIntakeorderBarcodeEntity intakeorderBarcodeEntity;
    public boolean indatabaseBln;

    public static cIntakeorderBarcodeViewModel gIntakeorderBarcodeViewModel;

    public static cIntakeorderBarcodeViewModel getIntakeorderBarcodeViewModel() {
        if (gIntakeorderBarcodeViewModel == null) {
            gIntakeorderBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cIntakeorderBarcodeViewModel.class);
        }
        return gIntakeorderBarcodeViewModel;
    }

    public static cIntakeorderBarcodeAdapter gIntakeorderBarcodeAdapter;
    public static cIntakeorderBarcodeAdapter getIntakeorderBarcodeAdapter() {
        if (gIntakeorderBarcodeAdapter == null) {
            gIntakeorderBarcodeAdapter = new cIntakeorderBarcodeAdapter();
        }
        return gIntakeorderBarcodeAdapter;
    }

    public static List<cIntakeorderBarcode> allBarcodesObl;
    public static cIntakeorderBarcode currentIntakeOrderBarcode;

    //Region Public Properties

    public String barcode;
    public String getBarcodeStr() {
        return this.barcode;
    }

    public String barcodeWithoutCheckDigitStr;
    public String getBarcodeWithoutCheckDigitStr() {

        this.barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (cText.pStringToIntegerInt(this.getBarcodeTypeStr()) != cBarcodeScan.BarcodeType.EAN8 && cText.pStringToIntegerInt(this.getBarcodeTypeStr()) != cBarcodeScan.BarcodeType.EAN13 ) {
            return  this.barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() != 8 && this.getBarcodeStr().length() != 13 ) {
            return  this.barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() == 8)  {
            this.barcodeWithoutCheckDigitStr = this.barcodeWithoutCheckDigitStr.substring(0,7);
        }

        if (this.getBarcodeStr().length() == 13)  {
            this.barcodeWithoutCheckDigitStr = this.barcodeWithoutCheckDigitStr.substring(0,12);
        }

        return barcodeWithoutCheckDigitStr;
    }

    public String barcodetypeStr;
    public String getBarcodeTypeStr() {return barcodetypeStr;}

    public Boolean isuniquebarcode;
    public Boolean getIsUniqueBarcodeBln() {
        return this.isuniquebarcode;
    }

    public String itemno;
    public String getItemNoStr() {
        return this.itemno;
    }

    public String variantCode;
    public String getVariantCodeStr() {
        return this.variantCode;
    }

    public Double quantityPerUnitOfMeasure;
    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasure;
    }

    public String unitOfMeasure;
    public String getUnitOfMeasureStr() {
        return this.unitOfMeasure;
    }

    public Double quantityHandled;
    public Double getQuantityHandled() {
        return this.quantityHandled;
    }

    public Boolean receiveAmountManual;
    public Boolean getReceiveAmountManualBln() {
        return this.receiveAmountManual;
    }

    //Region Constructor
    public cIntakeorderBarcode(JSONObject pvJsonObject) {
        this.intakeorderBarcodeEntity = new cIntakeorderBarcodeEntity(pvJsonObject);
        this.barcode = this.intakeorderBarcodeEntity.getBarcodeStr();
        this.barcodetypeStr = this.intakeorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.intakeorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.intakeorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.intakeorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.intakeorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.intakeorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandled = this.intakeorderBarcodeEntity.getQuantityHandled();
        this.receiveAmountManual = this.intakeorderBarcodeEntity.getReceiveAmountManualBln();
    }

    public cIntakeorderBarcode(cIntakeorderBarcodeEntity pvIntakeorderBarcodeEntity) {
        this.intakeorderBarcodeEntity = pvIntakeorderBarcodeEntity;
        this.barcode = this.intakeorderBarcodeEntity.getBarcodeStr();
        this.barcodetypeStr = this.intakeorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.intakeorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.intakeorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.intakeorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.intakeorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.intakeorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandled = this.intakeorderBarcodeEntity.getQuantityHandled();
        this.receiveAmountManual = this.intakeorderBarcodeEntity.getReceiveAmountManualBln();
    }

    public cIntakeorderBarcode() {

    }

    //End Region Constructor


    public static boolean pTruncateTableBln(){
        cIntakeorderBarcode.getIntakeorderBarcodeViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        cIntakeorderBarcode.getIntakeorderBarcodeViewModel().insert(this.intakeorderBarcodeEntity);
        this.indatabaseBln = true;

        if (cIntakeorderBarcode.allBarcodesObl == null){
            cIntakeorderBarcode.allBarcodesObl = new ArrayList<>();
        }
        cIntakeorderBarcode.allBarcodesObl.add(this);
        return  true;
    }

    public List<cIntakeorderMATLine> linesObl(){

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();

        if (cIntakeorder.currentIntakeOrder == null || cIntakeorder.currentIntakeOrder.linesObl() == null || cIntakeorder.currentIntakeOrder.linesObl().size() == 0) {
            return resultObl;
        }

        for (cIntakeorderMATLine intakeorderMATLine :cIntakeorder.currentIntakeOrder.linesObl() ) {
            if (intakeorderMATLine.getItemNoStr().equalsIgnoreCase(this.getItemNoStr()) && intakeorderMATLine.getVariantCodeStr().equalsIgnoreCase(this.getVariantCodeStr())) {
                resultObl.add(intakeorderMATLine);
            }
        }

        return  resultObl;

    }

    public static List <cIntakeorderBarcode> pGetIntakeBarcodesViaVariantAndItemNoObl(String pvItemNo, String pvVariantcode) {
        if (cIntakeorderBarcode.allBarcodesObl == null || cIntakeorderBarcode.allBarcodesObl.size() == 0){
            return null;
        }
        List <cIntakeorderBarcode> resultObl = null;

        for (cIntakeorderBarcode intakeorderBarcode : cIntakeorderBarcode.allBarcodesObl) {
            if (intakeorderBarcode.getVariantCodeStr().equalsIgnoreCase(pvVariantcode) && intakeorderBarcode.getItemNoStr().equalsIgnoreCase(pvItemNo)){
                if (resultObl == null ){
                    resultObl = new ArrayList<>();
                }

                resultObl.add(intakeorderBarcode);
            }
        }return resultObl;
    }



}
