package SSU_WHS.PackAndShip.PackAndShipBarcode;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeEntity;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeViewModel;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;

public class cPackAndShipBarcode {

    private cPackAndShipBarcodeEntity packAndShipBarcodeEntity;

    public static List<cPackAndShipBarcode> allPackAndShipOrderBarcodesObl;
    //Region Public Properties

    public String barcode;
    public String getBarcodeStr() {
        return this.barcode;
    }

    public String barcodetype;
    public String getBarcodeTypesStr() {
        return this.barcodetype;
    }

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

    public  String getItemNoAndVariantCodeStr(){
        return   this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    public Double quantityPerUnitOfMeasure;
    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasure;
    }

    public String unitOfMeasure;
    public String getUnitOfMeasureStr() {
        return this.unitOfMeasure;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return this.quantityHandledDbl;
    }

    public  String getBarcodeAndQuantityStr(){
        return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }

    public  String getKeyStr() {
        return  this.getItemNoStr() + "þ" + this.getVariantCodeStr();
    }

    private cPackAndShipBarcodeViewModel getPackAndShipBarcodeViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipBarcodeViewModel.class);
    }

    //Region Constructor
    public cPackAndShipBarcode(JSONObject pvJsonObject) {
        this.packAndShipBarcodeEntity = new cPackAndShipBarcodeEntity(pvJsonObject);
        this.barcode = this.packAndShipBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.packAndShipBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.packAndShipBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.packAndShipBarcodeEntity.getItemNoStr();
        this.variantCode = this.packAndShipBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.packAndShipBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.packAndShipBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.packAndShipBarcodeEntity.getQuantityHandled();
    }

    public cPackAndShipBarcode(cArticleBarcode pvArticleBarcode) {
        this.packAndShipBarcodeEntity = null;
        this.barcode = pvArticleBarcode.getBarcodeStr();
        this.barcodetype = cText.pIntToStringStr(pvArticleBarcode.getBarcodeTypeInt());
        this.isuniquebarcode = pvArticleBarcode.isUniqueBarcodeBln;
        this.itemno = pvArticleBarcode.getItemNoStr();
        this.variantCode = pvArticleBarcode.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = pvArticleBarcode.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = pvArticleBarcode.getUnitOfMeasureStr();
        this.quantityHandledDbl = (double) 0;
    }



    //End Region Constructor

    public static cPackAndShipBarcode pGetMoveOrderBarcodeByBarcode(String pvBarcodeStr){

        if (cPackAndShipBarcode.allPackAndShipOrderBarcodesObl == null) {
            return  null;
        }

        for (cPackAndShipBarcode packAndShipBarcode : cPackAndShipBarcode.allPackAndShipOrderBarcodesObl ) {

            if (packAndShipBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr) || packAndShipBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeStr)) {
                return  packAndShipBarcode;
            }
        }

        return  null;

    }

    public static boolean pTruncateTableBln(){

        cPackAndShipBarcodeViewModel packAndShipBarcodeViewModel=   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipBarcodeViewModel.class);
        packAndShipBarcodeViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getPackAndShipBarcodeViewModel().insert(this.packAndShipBarcodeEntity);

        if (cPackAndShipBarcode.allPackAndShipOrderBarcodesObl == null){
            cPackAndShipBarcode.allPackAndShipOrderBarcodesObl = new ArrayList<>();
        }
        cPackAndShipBarcode.allPackAndShipOrderBarcodesObl.add(this);
        return  true;
    }

    public String getBarcodeWithoutCheckDigitStr() {

        String barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (cText.pStringToIntegerInt(this.getBarcodeTypesStr()) != cBarcodeScan.BarcodeType.EAN8 && cText.pStringToIntegerInt(this.getBarcodeTypesStr()) != cBarcodeScan.BarcodeType.EAN13 ) {
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

}