package SSU_WHS.Inventory.InventoryorderBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderBarcode {
    public cInventoryorderBarcodeEntity inventoryorderBarcodeEntity;
    public boolean indatabaseBln;

    private static cInventoryorderBarcodeViewModel gInventoryorderBarcodeViewModel;
    private static cInventoryorderBarcodeViewModel getInventoryorderBarcodeViewModel() {
        if (gInventoryorderBarcodeViewModel == null) {
            gInventoryorderBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cInventoryorderBarcodeViewModel.class);
        }
        return gInventoryorderBarcodeViewModel;
    }

    private static cInventoryorderBarcodeAdapter gInventoryorderBarcodeAdapter;
    public static cInventoryorderBarcodeAdapter getInventoryorderBarcodeAdapter() {
        if (gInventoryorderBarcodeAdapter == null) {
            gInventoryorderBarcodeAdapter = new cInventoryorderBarcodeAdapter();
        }
        return gInventoryorderBarcodeAdapter;
    }

    public static List<cInventoryorderBarcode> allInventoryorderBarcodesObl;
    public static cInventoryorderBarcode currentInventoryOrderBarcode;

    //Region Public Properties

    private String barcode;
    public String getBarcodeStr() {
        return this.barcode;
    }

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

    public Double quantityHandled;
    public Double getQuantityHandled() {
        return this.quantityHandled;
    }

    public  String getBarcodeAndQuantityStr(){
        return  this.getBarcodeStr() + " (" + this.getQuantityPerUnitOfMeasureDbl().intValue() + ")";
    }

    private Boolean invAmountManual;
    public Boolean getInvAmountManualBln() {
        return this.invAmountManual;
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
        this.quantityHandled = this.inventoryorderBarcodeEntity.getQuantityHandled();
        this.invAmountManual = this.inventoryorderBarcodeEntity.getInvAmountManualBln();
    }

    public cInventoryorderBarcode(cInventoryorderBarcodeEntity inventoryorderBarcodeEntity) {
        this.inventoryorderBarcodeEntity = inventoryorderBarcodeEntity;
        this.barcode = this.inventoryorderBarcodeEntity.getBarcodeStr();
        this.barcodetype = this.inventoryorderBarcodeEntity.getBarcodeTypesStr();
        this.isuniquebarcode = this.inventoryorderBarcodeEntity.getIsUniqueBarcodeBln();
        this.itemno = this.inventoryorderBarcodeEntity.getItemNoStr();
        this.variantCode = this.inventoryorderBarcodeEntity.getVariantCodeStr();
        this.itemType = this.inventoryorderBarcodeEntity.getItemTypeStr();
        this.quantityPerUnitOfMeasure = this.inventoryorderBarcodeEntity.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasure = this.inventoryorderBarcodeEntity.getUnitOfMeasureStr();
        this.quantityHandled = this.inventoryorderBarcodeEntity.getQuantityHandled();
        this.invAmountManual = this.inventoryorderBarcodeEntity.getInvAmountManualBln();
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
        this.quantityHandled = this.inventoryorderBarcodeEntity.getQuantityHandled();
        this.invAmountManual = this.inventoryorderBarcodeEntity.getInvAmountManualBln();
    }

    public cInventoryorderBarcode() {

    }
    //End Region Constructor



    public static boolean pTruncateTableBln(){
        cInventoryorderBarcode.getInventoryorderBarcodeViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        cInventoryorderBarcode.getInventoryorderBarcodeViewModel().insert(this.inventoryorderBarcodeEntity);
        this.indatabaseBln = true;

        if (cInventoryorderBarcode.allInventoryorderBarcodesObl == null){
            cInventoryorderBarcode.allInventoryorderBarcodesObl = new ArrayList<>();
        }
        cInventoryorderBarcode.allInventoryorderBarcodesObl.add(this);
        return  true;
    }

    public static boolean pInsertAllInDatabaseBln(List<cInventoryorderBarcodeEntity> inventoryorderBarcodeEntities ) {
        cInventoryorderBarcode.getInventoryorderBarcodeViewModel().insertAll (inventoryorderBarcodeEntities);
        return  true;
    }

}
