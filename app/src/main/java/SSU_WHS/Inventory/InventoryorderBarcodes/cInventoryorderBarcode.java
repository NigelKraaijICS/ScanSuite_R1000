package SSU_WHS.Inventory.InventoryorderBarcodes;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;

public class cInventoryorderBarcode {

    public cInventoryorderBarcodeEntity inventoryorderBarcodeEntity;

    private cInventoryorderBarcodeViewModel getInventoryorderBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderBarcodeViewModel.class);
    }
    public static List<cInventoryorderBarcode> allInventoryorderBarcodesObl;
    public static cInventoryorderBarcode currentInventoryOrderBarcode;

    //Region Public Properties

    private String barcode;
    public String getBarcodeStr() {
        return this.barcode;
    }


    public String getBarcodeWithoutCheckDigitStr() {
        String barcodeWithoutCheckDigitStr;
        barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (cText.pStringToIntegerInt(this.getBarcodeTypeStr()) != cBarcodeScan.BarcodeType.EAN8 && cText.pStringToIntegerInt(this.getBarcodeTypeStr()) != cBarcodeScan.BarcodeType.EAN13 ) {
            return  barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() != 8 && this.getBarcodeStr().length() != 13 ) {
            return  barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() == 8)  {
            barcodeWithoutCheckDigitStr = barcodeWithoutCheckDigitStr.substring(0,7);
        }

        if (this.getBarcodeStr().length() == 13)  {
            barcodeWithoutCheckDigitStr = barcodeWithoutCheckDigitStr.substring(0,12);
        }

        return barcodeWithoutCheckDigitStr;
    }

    private String barcodetype;
    public String getBarcodeTypeStr() {
        return this.barcodetype;
    }

    private Boolean isuniquebarcode;
    public Boolean getIsUniqueBarcodeBln() {
        return this.isuniquebarcode;
    }

    private String itemno;
    public String getItemNoStr() {
        return this.itemno;
    }

    private String variantCode;
    public String getVariantCodeStr() {
        return this.variantCode;
    }

    private String itemType;
    public String getItemTypeStr() {
        return this.itemType;
    }

    private Double quantityPerUnitOfMeasure;
    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasure;
    }

    private String unitOfMeasure;
    public String getUnitOfMeasureStr() {
        return this.unitOfMeasure;
    }



    public  String getUnitOfMeasureInfoStr(){
     return    this.getBarcodeAndQuantityStr() + " " + this.getUnitOfMeasureStr();
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return this.quantityHandledDbl;
    }

    public  String getBarcodeAndQuantityStr(){
        return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }

    //Region Constructor
    public cInventoryorderBarcode(JSONObject pvJsonObject) {
        this.inventoryorderBarcodeEntity = new cInventoryorderBarcodeEntity(pvJsonObject);
        this.barcode = this.inventoryorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.inventoryorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.inventoryorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.inventoryorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.inventoryorderBarcodeEntity.getVariantCodeStr();
        this.itemType = this.inventoryorderBarcodeEntity.getItemTypeStr();
        this.quantityPerUnitOfMeasure = this.inventoryorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.inventoryorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.inventoryorderBarcodeEntity.getQuantityHandled();
    }

    public cInventoryorderBarcode(cArticleBarcode pvArticleBarcode) {
        this.inventoryorderBarcodeEntity = new cInventoryorderBarcodeEntity(pvArticleBarcode);
        this.barcode = this.inventoryorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.inventoryorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.inventoryorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.inventoryorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.inventoryorderBarcodeEntity.getVariantCodeStr();
        this.itemType = this.inventoryorderBarcodeEntity.getItemTypeStr();
        this.quantityPerUnitOfMeasure = this.inventoryorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.inventoryorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandledDbl = this.inventoryorderBarcodeEntity.getQuantityHandled();

    }

    //End Region Constructor



    public static boolean pTruncateTableBln(){
        cInventoryorderBarcodeViewModel inventoryorderBarcodeViewModel  = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderBarcodeViewModel.class);
        inventoryorderBarcodeViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        getInventoryorderBarcodeViewModel().insert(this.inventoryorderBarcodeEntity);

        if (cInventoryorderBarcode.allInventoryorderBarcodesObl == null){
            cInventoryorderBarcode.allInventoryorderBarcodesObl = new ArrayList<>();
        }
        cInventoryorderBarcode.allInventoryorderBarcodesObl.add(this);
        return  true;
    }

    public static void pInsertAllInDatabase(List<cInventoryorderBarcodeEntity> inventoryorderBarcodeEntities ) {
        cInventoryorderBarcodeViewModel inventoryorderBarcodeViewModel  = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderBarcodeViewModel.class);
        inventoryorderBarcodeViewModel.insertAll (inventoryorderBarcodeEntities);
    }

}
