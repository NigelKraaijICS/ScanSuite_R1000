package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import androidx.lifecycle.ViewModelProvider;

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

    private cInventoryorderLineBarcodeViewModel getInventoryorderLineBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineBarcodeViewModel.class);
    }
    public static ArrayList<cInventoryorderLineBarcode> allLineBarcodesObl;
    public static cInventoryorderLineBarcode currentInventoryorderLineBarcode;

    //End Public Properties

    //Region Constructor
    public cInventoryorderLineBarcode(JSONObject pvJsonObject) {
        this.inventoryorderLineBarcodeEntity = new cInventoryorderLineBarcodeEntity(pvJsonObject);
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
        getInventoryorderLineBarcodeViewModel().insert(this.inventoryorderLineBarcodeEntity);

        if (cInventoryorderLineBarcode.allLineBarcodesObl == null){
            cInventoryorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cInventoryorderLineBarcode.allLineBarcodesObl.add(this);
        return  true;
    }

    public static void pDeleteAllOtherLinesForBarcode(int pvLineNoInt, String pvBarcodeStr) {

        List<cInventoryorderLineBarcode> inventoryorderLineBarcodesObl = new ArrayList<>();
        inventoryorderLineBarcodesObl.addAll(cInventoryorderLineBarcode.allLineBarcodesObl);

        for (cInventoryorderLineBarcode inventoryorderLineBarcode : inventoryorderLineBarcodesObl) {
            if (inventoryorderLineBarcode.getLineNoLng().intValue() == (pvLineNoInt) && !inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr)) {
                inventoryorderLineBarcode.pDeleteFromDatabaseBln();
            }
        }
    }

    public static  void pInsertAllInDatabase(List<cInventoryorderLineBarcodeEntity> pvInventoryorderLineBarcodeEntities ) {
        cInventoryorderLineBarcodeViewModel inventoryorderLineBarcodeViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineBarcodeViewModel.class);
        inventoryorderLineBarcodeViewModel.insertAll (pvInventoryorderLineBarcodeEntities);
    }

    public boolean pDeleteFromDatabaseBln() {
        getInventoryorderLineBarcodeViewModel().delete(this.inventoryorderLineBarcodeEntity);
        cInventoryorderLineBarcode.allLineBarcodesObl.remove(this);
        return  true;
    }


    public static boolean pTruncateTableBln(){
        cInventoryorderLineBarcodeViewModel inventoryorderLineBarcodeViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineBarcodeViewModel.class);
        inventoryorderLineBarcodeViewModel.deleteAll();
        return true;
    }

}
