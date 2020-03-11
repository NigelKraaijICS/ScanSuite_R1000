package SSU_WHS.Move.MoveorderBarcodes;

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

    public static cMoveorderBarcodeViewModel gMoveorderBarcodeViewModel;
    public static cMoveorderBarcodeViewModel getMoveorderBarcodeViewModel() {
        if (gMoveorderBarcodeViewModel == null) {
            gMoveorderBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cMoveorderBarcodeViewModel.class);
        }
        return gMoveorderBarcodeViewModel;
    }

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

    public String getItemTypeStr() { return "";}

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

    public cMoveorderBarcode(cMoveorderBarcodeEntity moveorderBarcodeEntity) {
        this.moveorderBarcodeEntity = moveorderBarcodeEntity;
        this.barcode = this.moveorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.moveorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.moveorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.moveorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.moveorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.moveorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.moveorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandled = this.moveorderBarcodeEntity.getQuantityHandled();
    }
    public cMoveorderBarcode() {

    }
    //End Region Constructor



    public static boolean pTruncateTableBln(){
        cMoveorderBarcode.getMoveorderBarcodeViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        cMoveorderBarcode.getMoveorderBarcodeViewModel().insert(this.moveorderBarcodeEntity);

        if (cMoveorderBarcode.allMoveorderBarcodesObl == null){
            cMoveorderBarcode.allMoveorderBarcodesObl = new ArrayList<>();
        }
        cMoveorderBarcode.allMoveorderBarcodesObl.add(this);
        return  true;
    }
    public boolean pCreateBarcodesViaWebserviceBln() {

        cWebresult WebResult;

        WebResult =  cMoveorderBarcode.getMoveorderBarcodeViewModel().pCreateBarcodeViaWebserviceWrs();
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
    private String barcodeWithoutCheckDigitStr;
    public String getBarcodeWithoutCheckDigitStr() {

        this.barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (cText.pStringToIntegerInt(this.getBarcodeTypesStr()) != cBarcodeScan.BarcodeType.EAN8 && cText.pStringToIntegerInt(this.getBarcodeTypesStr()) != cBarcodeScan.BarcodeType.EAN13 ) {
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

    public static List<cMoveorderBarcode> pSortedmoveorderBarcodeList(List<cMoveorderBarcode> unsortedMoveorderBarcode) {
        List<cMoveorderBarcode> resultList = new ArrayList<>();
        boolean barcodeFoundBln = false;
        for (cMoveorderBarcode moveorderBarcode : unsortedMoveorderBarcode) {
            for (cMoveorderBarcode resultBarcode : resultList) {
                if (resultBarcode.getBarcodeStr().equalsIgnoreCase(moveorderBarcode.getBarcodeStr())) {
                    resultBarcode.quantityHandled +=  moveorderBarcode.quantityPerUnitOfMeasure;
                    barcodeFoundBln = true;
                }
            }
            if (barcodeFoundBln) {
                barcodeFoundBln = false;
            }
            else {
                //new barcode, so add
                moveorderBarcode.quantityHandled = moveorderBarcode.quantityPerUnitOfMeasure;
                resultList.add(moveorderBarcode);
            }
        }
        return resultList;
    }
}