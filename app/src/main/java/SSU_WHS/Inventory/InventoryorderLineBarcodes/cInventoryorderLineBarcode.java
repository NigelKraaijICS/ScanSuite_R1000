package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cInventoryorderLineBarcode {

    //Public Properties
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    public cInventoryorderLineBarcodeEntity inventoryorderLineBarcodeEntity;
    public boolean inDatabaseBln;

    public static ArrayList<cInventoryorderLineBarcode> allLineBarcodesObl;
    public static cInventoryorderLineBarcodeViewModel inventoryorderLineBarcodeViewModel;

    public static cInventoryorderLineBarcode currentInventoryorderLineBarcode;

    public static cInventoryorderLineBarcodeViewModel getInventoryorderLineBarcodeViewModel() {
        if (inventoryorderLineBarcodeViewModel == null) {
            inventoryorderLineBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cInventoryorderLineBarcodeViewModel.class);
        }
        return inventoryorderLineBarcodeViewModel;
    }

    //End Public Properties

    //Region Constructor
    public cInventoryorderLineBarcode(JSONObject pvJsonObject) {
        this.inventoryorderLineBarcodeEntity = new cInventoryorderLineBarcodeEntity(pvJsonObject);
        this.lineNoLng = this.inventoryorderLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.inventoryorderLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.inventoryorderLineBarcodeEntity.getQuantityhandledStr());
        }

    public cInventoryorderLineBarcode(cInventoryorderLineBarcodeEntity pvInventoryorderLineBarcodeEntity){
        this.inventoryorderLineBarcodeEntity = pvInventoryorderLineBarcodeEntity;
        this.lineNoLng = this.inventoryorderLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.inventoryorderLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.inventoryorderLineBarcodeEntity.getQuantityhandledStr());
    }

    public cInventoryorderLineBarcode(Long pvLineNoLng,String pvBarcodeStr, Double pvQuantityHandledDbl ){

        this.inventoryorderLineBarcodeEntity = new cInventoryorderLineBarcodeEntity(pvLineNoLng,pvBarcodeStr);
        this.inventoryorderLineBarcodeEntity.quantityHandledStr = cText.pDoubleToStringStr(pvQuantityHandledDbl);

        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledDbl = pvQuantityHandledDbl;
    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        cInventoryorderLineBarcode.getInventoryorderLineBarcodeViewModel().insert(this.inventoryorderLineBarcodeEntity);
        this.inDatabaseBln = true;

        if (cInventoryorderLineBarcode.allLineBarcodesObl == null){
            cInventoryorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cInventoryorderLineBarcode.allLineBarcodesObl.add(this);
        return  true;
    }

    public static  void pInsertAllInDatabase(List<cInventoryorderLineBarcodeEntity> pvInventoryorderLineBarcodeEntities ) {
        cInventoryorderLineBarcode.getInventoryorderLineBarcodeViewModel().insertAll (pvInventoryorderLineBarcodeEntities);
    }

    public boolean pDeleteFromDatabaseBln() {
        cInventoryorderLineBarcode.getInventoryorderLineBarcodeViewModel().delete(this.inventoryorderLineBarcodeEntity);
        cInventoryorderLineBarcode.allLineBarcodesObl.remove(this);
        return  true;
    }


    public static boolean pTruncateTableBln(){
        cInventoryorderLineBarcode.getInventoryorderLineBarcodeViewModel().deleteAll();
        return true;
    }

}
