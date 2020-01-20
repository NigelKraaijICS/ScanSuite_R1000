package SSU_WHS.Return.ReturnorderBarcode;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;

public class cReturnorderBarcode {

    private cReturnorderBarcodeEntity returnorderBarcodeEntity;
    public boolean indatabaseBln;

    private static cReturnorderBarcodeViewModel gReturnorderBarcodeViewModel;

    public static cReturnorderBarcodeViewModel getReturnorderBarcodeViewModel() {
        if (gReturnorderBarcodeViewModel == null) {
            gReturnorderBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cReturnorderBarcodeViewModel.class);
        }
        return gReturnorderBarcodeViewModel;
    }

    public static List<SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode> allReturnorderBarcodesObl;
    public static SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode currentReturnOrderBarcode;

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

    public String getBarcodeAndQuantityStr(){
      return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }

    //Region Constructor
    public cReturnorderBarcode(JSONObject pvJsonObject) {
        this.returnorderBarcodeEntity = new cReturnorderBarcodeEntity(pvJsonObject);
        this.barcode = this.returnorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.returnorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.returnorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.returnorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.returnorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.returnorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.returnorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandled = this.returnorderBarcodeEntity.getQuantityHandledDbl();
    }

    public cReturnorderBarcode(cArticleBarcode pvArticleBarcode) {
        this.returnorderBarcodeEntity = new cReturnorderBarcodeEntity(pvArticleBarcode);
        this.barcode = this.returnorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.returnorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.returnorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.returnorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.returnorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasure = this.returnorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.returnorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandled = this.returnorderBarcodeEntity.getQuantityHandledDbl();
    }

    //End Region Constructor



    public static boolean pTruncateTableBln(){
       SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode.getReturnorderBarcodeViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode.getReturnorderBarcodeViewModel().insert(this.returnorderBarcodeEntity);
        this.indatabaseBln = true;

        if (SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode.allReturnorderBarcodesObl == null){
            SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode.allReturnorderBarcodesObl = new ArrayList<>();
        }
        SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode.allReturnorderBarcodesObl.add(this);
        return  true;
    }

}
