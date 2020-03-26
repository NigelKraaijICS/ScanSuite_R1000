package SSU_WHS.Move.MoveorderBarcodes;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cMoveorderBarcode {

    private cMoveorderBarcodeEntity moveorderBarcodeEntity;

    public static List<cMoveorderBarcode> allMoveorderBarcodesObl;
    public static cMoveorderBarcode currentMoveOrderBarcode;

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
        this.quantityHandled = this.moveorderBarcodeEntity.getQuantityHandled();
    }

    //End Region Constructor



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
    public boolean pCreateBarcodesViaWebserviceBln() {

        cWebresult WebResult;

        WebResult =  this.getMoveorderBarcodeViewModel().pCreateBarcodeViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_CREATEMOVEORDERBARCODES);
            return  false;
        }
    }

    public static List <cMoveorderBarcode> pGetMovebarcodeViaVariantAndItemNoObl(String pvItemNo, String pvVariantcode) {
        if (cMoveorderBarcode.allMoveorderBarcodesObl == null || cMoveorderBarcode.allMoveorderBarcodesObl.size() == 0){
            return null;
        }
        List <cMoveorderBarcode> resultObl = null;

        for (cMoveorderBarcode moveorderBarcode : cMoveorderBarcode.allMoveorderBarcodesObl) {
            if (moveorderBarcode.getVariantCodeStr().equalsIgnoreCase(pvVariantcode) && moveorderBarcode.getItemNoStr().equalsIgnoreCase(pvItemNo)){
                if (resultObl == null ){
                    resultObl = new ArrayList<>();
                }

                resultObl.add(moveorderBarcode);
            }
        }return resultObl;
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