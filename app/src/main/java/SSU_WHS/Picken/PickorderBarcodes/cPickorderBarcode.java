package SSU_WHS.Picken.PickorderBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import SSU_WHS.Picken.Pickorders.cPickorder;
import ICS.cAppExtension;

public class cPickorderBarcode {

    private String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    private String barcodeWithoutCheckDigitStr;
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

    private String barcodetypeStr;
    private String getBarcodeTypeStr() {return barcodetypeStr;}

    private Boolean isUniqueBarcodeBln;
    public Boolean getIsUniqueBarcodeBln() {return isUniqueBarcodeBln;}

    private String itemNoStr;
    public String getItemNoStr() {return itemNoStr;}

    private String variantcodeStr;
    public String getVariantcodeStr() {return variantcodeStr;}

    private Double quantityPerUnitOfMeasureDbl;
    public Double getQuantityPerUnitOfMeasureDbl() {return quantityPerUnitOfMeasureDbl;}

    private Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {return quantityHandledDbl;}

    public  String getBarcodeAndQuantityStr(){
        return    this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }



    private cPickorderBarcodeEntity pickorderBarcodeEntity;
    public boolean inDatabaseBln;

    public static ArrayList<cPickorderBarcode> allBarcodesObl;
    public static cPickorderBarcode currentPickorderBarcode;


    public iPickorderBarcodeDao pickorderBarcodeDao;

    private static cPickorderBarcodeViewModel pickorderBarcodeViewModel;
    private static cPickorderBarcodeViewModel getPickorderBarcodeViewModel(){
        if (pickorderBarcodeViewModel == null) {
            pickorderBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cPickorderBarcodeViewModel.class);
        }
        return pickorderBarcodeViewModel;
    }

    private static cPickorderBarcodeAdapter gPickorderBarcodeAdapter;
    public static cPickorderBarcodeAdapter getPickorderBarcodeAdapter() {
        if (gPickorderBarcodeAdapter == null) {
            gPickorderBarcodeAdapter = new cPickorderBarcodeAdapter();
        }
        return gPickorderBarcodeAdapter;
    }

    public cPickorderBarcode(JSONObject pvJsonObject) {
        this.pickorderBarcodeEntity = new cPickorderBarcodeEntity(pvJsonObject);
        this.barcodeStr = this.pickorderBarcodeEntity.getBarcodeStr();
        this.barcodetypeStr = this.pickorderBarcodeEntity.getBarcodeTypeStr();
        this.isUniqueBarcodeBln = cText.pStringToBooleanBln(this.pickorderBarcodeEntity.getIsuniquebarcodeStr(),false) ;
        this.itemNoStr = this.pickorderBarcodeEntity.getItemnoStr();
        this.variantcodeStr = this.pickorderBarcodeEntity.getVariantcodeStr();
        this.quantityPerUnitOfMeasureDbl =  cText.pStringToDoubleDbl(this.pickorderBarcodeEntity.getQuantityperunitofmeasureStr());
        this.quantityHandledDbl =  cText.pStringToDoubleDbl(this.pickorderBarcodeEntity.getQuantityhandledStr());
    }
    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cPickorderBarcode.getPickorderBarcodeViewModel().insert(this.pickorderBarcodeEntity);
        this.inDatabaseBln = true;
        if (cPickorderBarcode.allBarcodesObl == null ){
            cPickorderBarcode.allBarcodesObl = new ArrayList<>();

        }
        cPickorderBarcode.allBarcodesObl.add(this);
        return true;

    }

    public boolean pDeleteFromDatabaseBln() {
        cPickorderBarcode.getPickorderBarcodeViewModel().delete(this.pickorderBarcodeEntity);
        this.inDatabaseBln = true;
        if (cPickorderBarcode.allBarcodesObl == null ){
            cPickorderBarcode.allBarcodesObl = new ArrayList<>();

        }
        cPickorderBarcode.allBarcodesObl.add(this);
        return true;

    }

    public static boolean pTruncateTableBln(){
        cPickorderBarcode.getPickorderBarcodeViewModel().deleteAll();
        return true;
    }

    public static cPickorderBarcode pGetPickbarcodeViaBarcode(cBarcodeScan pvBarcodeScan) {
        if (cPickorderBarcode.allBarcodesObl == null || cPickorderBarcode.allBarcodesObl.size() == 0){
            return null;
        }
        for (cPickorderBarcode pickorderBarcode : cPickorderBarcode.allBarcodesObl) {
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())){
                return pickorderBarcode;
            }
        }return null;
    }

    public static List <cPickorderBarcode> pGetPickbarcodesViaVariantAndItemNoObl(String pvItemNo, String pvVariantcode) {
        if (cPickorderBarcode.allBarcodesObl == null || cPickorderBarcode.allBarcodesObl.size() == 0){
            return null;
        }
        List <cPickorderBarcode> resultObl = null;

        for (cPickorderBarcode pickorderBarcode : cPickorderBarcode.allBarcodesObl) {
            if (pickorderBarcode.getVariantcodeStr().equalsIgnoreCase(pvVariantcode) && pickorderBarcode.getItemNoStr().equalsIgnoreCase(pvItemNo)){
                if (resultObl == null ){
                    resultObl = new ArrayList<>();
                }

                resultObl.add(pickorderBarcode);
            }
        }return resultObl;
    }

    public static cPickorderBarcode pGetPickorderBarcodeByBarcodeStr(String pvBarcodeStr) {

       if (cPickorder.currentPickOrder == null || cPickorder.currentPickOrder.barcodesObl() == null || cPickorder.currentPickOrder.barcodesObl().size() == 0 ) {
           return  null;
       }

       for (cPickorderBarcode pickorderBarcode : cPickorder.currentPickOrder.barcodesObl()) {
           if (pickorderBarcode.barcodeStr.equalsIgnoreCase(pvBarcodeStr)){
               return  pickorderBarcode;
           }
       }

       return  null;

    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}
