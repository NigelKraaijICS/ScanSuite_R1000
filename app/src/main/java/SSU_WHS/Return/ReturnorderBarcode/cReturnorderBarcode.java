package SSU_WHS.Return.ReturnorderBarcode;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Return.ReturnOrder.cReturnorderViewModel;

public class cReturnorderBarcode {

    private cReturnorderBarcodeEntity returnorderBarcodeEntity;

    public static List<cReturnorderBarcode> allReturnorderBarcodesObl;
    public static cReturnorderBarcode currentReturnOrderBarcode;

    //Region Public Properties

    public String barcodeStr;
    public String getBarcodeStr() {
        return this.barcodeStr;
    }

    public String barcodeTypeStr;
    public String getBarcodeTypesStr() {
        return this.barcodeTypeStr;
    }

    public Boolean isUniqueBarcodeBln;
    public Boolean getIsUniqueBarcodeBln() {
        return this.isUniqueBarcodeBln;
    }

    public String itemNoStr;
    public String getItemNoStr() {
        return this.itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return this.variantCodeStr;
    }

    public Double quantityPerUnitOfMeasureDbl;
    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasureDbl;
    }

    public String unitOfMeasureStr;
    public String getUnitOfMeasureStr() {
        return this.unitOfMeasureStr;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return this.quantityHandledDbl;
    }

    public String getBarcodeAndQuantityStr(){
      return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }

    public String getBarcodeAndQuantityAndUnitOfMeasureStr(){
        return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")"  + " " +  this.getUnitOfMeasureStr();
    }



    private cReturnorderBarcodeViewModel getReturnorderBarcodeViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderBarcodeViewModel.class);
    }

    //Region Constructor
    public cReturnorderBarcode(JSONObject pvJsonObject) {
        this.returnorderBarcodeEntity = new cReturnorderBarcodeEntity(pvJsonObject);
        this.barcodeStr = this.returnorderBarcodeEntity.getBarcodeStr();
        this.barcodeTypeStr = this.returnorderBarcodeEntity.getBarcodeTypesStr();
        this.isUniqueBarcodeBln = this.returnorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemNoStr = this.returnorderBarcodeEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasureDbl = this.returnorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasureStr = this.returnorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.returnorderBarcodeEntity.getQuantityHandledDbl();
    }

    public cReturnorderBarcode(cArticleBarcode pvArticleBarcode) {
        this.returnorderBarcodeEntity = new cReturnorderBarcodeEntity(pvArticleBarcode);
        this.barcodeStr = this.returnorderBarcodeEntity.getBarcodeStr();
        this.barcodeTypeStr = this.returnorderBarcodeEntity.getBarcodeTypesStr();
        this.isUniqueBarcodeBln = this.returnorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemNoStr = this.returnorderBarcodeEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderBarcodeEntity.getVariantCodeStr();
        this.quantityPerUnitOfMeasureDbl = this.returnorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasureStr = this.returnorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.returnorderBarcodeEntity.getQuantityHandledDbl();
    }

    //End Region Constructor



    public static boolean pTruncateTableBln(){
        cReturnorderViewModel returnorderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
        returnorderViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getReturnorderBarcodeViewModel().insert(this.returnorderBarcodeEntity);

        if (cReturnorderBarcode.allReturnorderBarcodesObl == null){
            cReturnorderBarcode.allReturnorderBarcodesObl = new ArrayList<>();
        }
        cReturnorderBarcode.allReturnorderBarcodesObl.add(this);
        return  true;
    }

}
