package SSU_WHS.Intake.IntakeorderBarcodes;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;

public class cIntakeorderBarcode {

    private cIntakeorderBarcodeEntity intakeorderBarcodeEntity;

    public static List<cIntakeorderBarcode> allBarcodesObl;
    public static cIntakeorderBarcode currentIntakeOrderBarcode;

    //Region Public Properties

    private String barcodeStr;
    public String getBarcodeStr() {
        return this.barcodeStr;
    }

    public String getBarcodeWithoutCheckDigitStr() {

        String barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (cText.pStringToIntegerInt(this.getBarcodeTypeStr()) != cBarcodeScan.BarcodeType.EAN8 && cText.pStringToIntegerInt(this.getBarcodeTypeStr()) != cBarcodeScan.BarcodeType.EAN13 ) {
            return barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() != 8 && this.getBarcodeStr().length() != 13 ) {
            return barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() == 8)  {
            barcodeWithoutCheckDigitStr = barcodeWithoutCheckDigitStr.substring(0,7);
        }

        if (this.getBarcodeStr().length() == 13)  {
            barcodeWithoutCheckDigitStr = barcodeWithoutCheckDigitStr.substring(0,12);
        }

        return barcodeWithoutCheckDigitStr;
    }

    private String barcodetypeStr;
    private String getBarcodeTypeStr() {return barcodetypeStr;}

    private Boolean isUniqueBarcodeBln;
    public Boolean getIsUniqueBarcodeBln() {
        return this.isUniqueBarcodeBln;
    }

    private String itemNoStr;
    public String getItemNoStr() {
        return this.itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return this.variantCodeStr;
    }

    public  String getItemNoAndVariantCodeStr(){
        return   this.getItemNoStr() + " " + this.getVariantCodeStr();
    }


    public Double quantityPerUnitOfMeasureDbl;
    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasureDbl;
    }

    private String unitOfMeasureStr;
    public String getUnitOfMeasureStr() {
        return this.unitOfMeasureStr;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return this.quantityHandledDbl;
    }

    public  String getBarcodeAndQuantityStr(){
        return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }

    public  String getBarcodeAndQuantityAndUnitOfMeasureStr(){
        return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")"      + " " + this.getUnitOfMeasureStr();
    }



    private cIntakeorderBarcodeViewModel getIntakeorderBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderBarcodeViewModel.class);
    }

    //Region Constructor
    public cIntakeorderBarcode(JSONObject pvJsonObject) {
        this.intakeorderBarcodeEntity = new cIntakeorderBarcodeEntity(pvJsonObject);
        this.barcodeStr = this.intakeorderBarcodeEntity.getBarcodeStr();
        this.barcodetypeStr = this.intakeorderBarcodeEntity.getBarcodeTypesStr();
        this.isUniqueBarcodeBln = this.intakeorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemNoStr = this.intakeorderBarcodeEntity.getItemNoStr();
        this.variantCodeStr = this.intakeorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasureDbl = this.intakeorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasureStr = this.intakeorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.intakeorderBarcodeEntity.getQuantityHandled();
    }

    public cIntakeorderBarcode(cIntakeorderBarcodeEntity pvIntakeorderBarcodeEntity) {
        this.intakeorderBarcodeEntity = pvIntakeorderBarcodeEntity;
        this.barcodeStr = this.intakeorderBarcodeEntity.getBarcodeStr();
        this.barcodetypeStr = this.intakeorderBarcodeEntity.getBarcodeTypesStr();
        this.isUniqueBarcodeBln = this.intakeorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemNoStr = this.intakeorderBarcodeEntity.getItemNoStr();
        this.variantCodeStr = this.intakeorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasureDbl = this.intakeorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasureStr = this.intakeorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.intakeorderBarcodeEntity.getQuantityHandled();
    }

    public cIntakeorderBarcode(cIntakeorderBarcode pvBarcodeToCopyObj) {
        this.intakeorderBarcodeEntity = null;
        this.barcodeStr = pvBarcodeToCopyObj.getBarcodeStr();
        this.barcodetypeStr = "";
        this.isUniqueBarcodeBln = false;
        this.itemNoStr = pvBarcodeToCopyObj.getItemNoStr();
        this.variantCodeStr = pvBarcodeToCopyObj.getVariantCodeStr();
        this.quantityPerUnitOfMeasureDbl = 1d;
        this.unitOfMeasureStr = "";
        this.quantityHandledDbl = 0d;

    }

    public cIntakeorderBarcode() {

    }


    //End Region Constructor

    public static boolean pTruncateTableBln(){

        cIntakeorderBarcodeViewModel intakeorderBarcodeViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderBarcodeViewModel.class);
        intakeorderBarcodeViewModel.deleteAll();
        return true;
    }

    public static cIntakeorderBarcode pGetIntakeOrderBarcodeByBarcode(String pvBarcodeStr){

        if (cIntakeorderBarcode.allBarcodesObl == null) {
            return  null;
        }

        for (cIntakeorderBarcode intakeorderBarcode : cIntakeorderBarcode.allBarcodesObl ) {

            if (intakeorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr) || intakeorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeStr)) {
                return  intakeorderBarcode;
            }
        }

        return  null;

    }

    public boolean pInsertInDatabaseBln() {
        this.getIntakeorderBarcodeViewModel().insert(this.intakeorderBarcodeEntity);

        if (cIntakeorderBarcode.allBarcodesObl == null){
            cIntakeorderBarcode.allBarcodesObl = new ArrayList<>();
        }
        cIntakeorderBarcode.allBarcodesObl.add(this);
        return  true;
    }

    public List<cIntakeorderMATLine> linesObl(){

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();

        if (cIntakeorder.currentIntakeOrder == null || cIntakeorder.currentIntakeOrder.linesMATObl() == null || cIntakeorder.currentIntakeOrder.linesMATObl().size() == 0) {
            return resultObl;
        }

        for (cIntakeorderMATLine intakeorderMATLine :cIntakeorder.currentIntakeOrder.linesMATObl() ) {
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
