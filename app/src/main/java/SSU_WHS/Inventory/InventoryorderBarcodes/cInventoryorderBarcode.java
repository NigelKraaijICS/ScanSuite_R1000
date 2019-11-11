package SSU_WHS.Inventory.InventoryorderBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderBarcode {
    public cInventoryorderBarcodeEntity inventoryorderBarcodeEntity;
    public boolean indatabaseBln;

    public static cInventoryorderBarcodeViewModel gInventoryorderBarcodeViewModel;

    public static cInventoryorderBarcodeViewModel getInventoryorderBarcodeViewModel() {
        if (gInventoryorderBarcodeViewModel == null) {
            gInventoryorderBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cInventoryorderBarcodeViewModel.class);
        }
        return gInventoryorderBarcodeViewModel;
    }

    public static List<cInventoryorderBarcode> allInventoryorderBarcodesObl;
    public static cInventoryorderBarcode currentInventoryOrderBarcode;

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

    public String itemType;
    public String getItemTypeStr() {
        return this.itemType;
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

    public Boolean invAmountManual;
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
    public boolean pCreateBarcodesViaWebserviceBln() {

        cWebresult WebResult;

        WebResult =  cInventoryorderBarcode.getInventoryorderBarcodeViewModel().pCreateBarcodeViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ) {
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return  false;
        }
    }

}