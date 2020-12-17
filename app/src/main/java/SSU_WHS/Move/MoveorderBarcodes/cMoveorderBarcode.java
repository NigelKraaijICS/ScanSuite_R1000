package SSU_WHS.Move.MoveorderBarcodes;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;

public class cMoveorderBarcode {

    private cMoveorderBarcodeEntity moveorderBarcodeEntity;

    public static List<cMoveorderBarcode> allMoveorderBarcodesObl;
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

    private cMoveorderBarcodeViewModel getMoveorderBarcodeViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderBarcodeViewModel.class);
    }

    //Region Constructor
    public cMoveorderBarcode(JSONObject pvJsonObject) {
        this.moveorderBarcodeEntity = new cMoveorderBarcodeEntity(pvJsonObject);
        this.barcode = this.moveorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.moveorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.moveorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.moveorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.moveorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.moveorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.moveorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.moveorderBarcodeEntity.getQuantityHandled();
    }

    public cMoveorderBarcode(cArticleBarcode pvArticleBarcode) {
        this.moveorderBarcodeEntity = null;
        this.barcode = pvArticleBarcode.getBarcodeStr();
        this.barcodetype = cText.pIntToStringStr(pvArticleBarcode.getBarcodeTypeInt());
        this.isuniquebarcode = pvArticleBarcode.isUniqueBarcodeBln;
        this.itemno = pvArticleBarcode.getItemNoStr();
        this.variantCode = pvArticleBarcode.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = pvArticleBarcode.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = pvArticleBarcode.getUnitOfMeasureStr();
        this.quantityHandledDbl = (double) 0;
    }

    public cMoveorderBarcode(cMoveorderLine pvMoveorderLine, cMoveorderLineBarcode pvMoveorderLineBarcode) {
        this.moveorderBarcodeEntity = null;
        this.barcode = pvMoveorderLineBarcode.getBarcodeStr();
        this.barcodetype = cText.pIntToStringStr(3);
        this.isuniquebarcode =false;
        this.itemno = pvMoveorderLine.getItemNoStr();
        this.variantCode = pvMoveorderLine.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = Double.valueOf(1);
        this.unitOfMeasure = "STUK";
        this.quantityHandledDbl = Double.valueOf(1);
    }


    //End Region Constructor

    public static cMoveorderBarcode pGetMoveOrderBarcodeByBarcode(String pvBarcodeStr){

        if (cMoveorderBarcode.allMoveorderBarcodesObl == null) {
            return  null;
        }

        for (cMoveorderBarcode moveorderBarcode : cMoveorderBarcode.allMoveorderBarcodesObl ) {

            if (moveorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr) || moveorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeStr)) {
                return  moveorderBarcode;
            }
        }

        return  null;

    }

    public static boolean pTruncateTableBln(){

        cMoveorderBarcodeViewModel moveorderBarcodeViewModel=   new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderBarcodeViewModel.class);
        moveorderBarcodeViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getMoveorderBarcodeViewModel().insert(this.moveorderBarcodeEntity);

        if (cMoveorderBarcode.allMoveorderBarcodesObl == null){
            cMoveorderBarcode.allMoveorderBarcodesObl = new ArrayList<>();
        }
        cMoveorderBarcode.allMoveorderBarcodesObl.add(this);
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

    public boolean pPlacedItemsAvailableBln() {

        if (cMoveorderBarcode.allMoveorderBarcodesObl == null || cMoveorderBarcode.allMoveorderBarcodesObl .size() == 0 ||
                cMoveorderLineBarcode.allMoveorderLineBarcodesObl == null || cMoveorderLineBarcode.allMoveorderLineBarcodesObl.size() == 0 ||
                cMoveorderLine.allLinesObl == null ||  cMoveorderLine.allLinesObl.size() == 0 )  {
            return false;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode : cMoveorderLineBarcode.allMoveorderLineBarcodesObl) {

            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(this.getBarcodeStr()) || moveorderLineBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(this.getBarcodeWithoutCheckDigitStr())) {

                cMoveorderLine moveorderLine =   cMoveorderLine.pGetLineByLineNo(moveorderLineBarcode.getLineNoInt());
                if (moveorderLine == null) {
                    return  false;
                }

                if (moveorderLine.getQuantityHandledDbl() > 0 ) {
                    return  true;
                }
                else
                {
                    return  false;
                }
            }

        }

        return  false;
    }


}