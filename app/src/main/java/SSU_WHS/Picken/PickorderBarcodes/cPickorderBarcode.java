package SSU_WHS.Picken.PickorderBarcodes;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;

public class cPickorderBarcode {

    private String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

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

    public static ArrayList<cPickorderBarcode> allBarcodesObl;
    public static cPickorderBarcode currentPickorderBarcode;

    private cPickorderBarcodeViewModel getPickorderBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderBarcodeViewModel.class);
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
        this.getPickorderBarcodeViewModel().insert(this.pickorderBarcodeEntity);
        if (cPickorderBarcode.allBarcodesObl == null ){
            cPickorderBarcode.allBarcodesObl = new ArrayList<>();

        }
        cPickorderBarcode.allBarcodesObl.add(this);
        return true;

    }

    public boolean pDeleteFromDatabaseBln() {
        this.getPickorderBarcodeViewModel().delete(this.pickorderBarcodeEntity);
        if (cPickorderBarcode.allBarcodesObl == null ){
            cPickorderBarcode.allBarcodesObl = new ArrayList<>();

        }
        cPickorderBarcode.allBarcodesObl.add(this);
        return true;

    }

    public static boolean pTruncateTableBln(){
        cPickorderBarcodeViewModel pickorderBarcodeViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderBarcodeViewModel.class);
        pickorderBarcodeViewModel.deleteAll();
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

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}
