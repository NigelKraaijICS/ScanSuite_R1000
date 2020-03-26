package SSU_WHS.Picken.PickorderLineBarcodes;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cPickorderLineBarcode {

    //Public Properties
    private Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    private String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    private cPickorderLineBarcodeEntity pickorderLineBarcodeEntity;

    public static ArrayList<cPickorderLineBarcode> allLineBarcodesObl;

    private cPickorderLineBarcodeViewModel getPickorderLineBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLineBarcodeViewModel.class);
    }

    public cPickorderLineBarcode (JSONObject pvJsonObject) {
        this.pickorderLineBarcodeEntity = new cPickorderLineBarcodeEntity(pvJsonObject);
        this.barcodeStr = this.pickorderLineBarcodeEntity.getBarcodeStr();
        this.lineNoLng = cText.pStringToLongLng(this.pickorderLineBarcodeEntity.getLineNoStr());
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.pickorderLineBarcodeEntity.getQuantityhandledStr());
    }

    public cPickorderLineBarcode (Long pvLineNoLng, String pvBarcodeStr) {
        this.pickorderLineBarcodeEntity = new cPickorderLineBarcodeEntity(pvLineNoLng,pvBarcodeStr);
        this.barcodeStr = this.pickorderLineBarcodeEntity.getBarcodeStr();
        this.lineNoLng = cText.pStringToLongLng(this.pickorderLineBarcodeEntity.getLineNoStr());
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.pickorderLineBarcodeEntity.getQuantityhandledStr());
    }

    public boolean pInsertInDatabaseBln() {
        this.getPickorderLineBarcodeViewModel().insert(this.pickorderLineBarcodeEntity);

        if (cPickorderLineBarcode.allLineBarcodesObl == null) {
            cPickorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cPickorderLineBarcode.allLineBarcodesObl.add(this);
        return true;
    }

    public boolean pDeleteFromDatabaseBln() {
        this.getPickorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoLng().intValue());

        if (cPickorderLineBarcode.allLineBarcodesObl != null) {
            cPickorderLineBarcode.allLineBarcodesObl.remove(this);
        }
        return true;
    }

    public void pUpdateAmountInDatabase(){
        this.getPickorderLineBarcodeViewModel().pUpdateAmountForLineNo(this.getBarcodeStr(), this.getQuantityhandledDbl());
    }

    public static boolean pTruncateTableBln() {

        cPickorderLineBarcodeViewModel pickorderLineBarcodeViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLineBarcodeViewModel.class);
        pickorderLineBarcodeViewModel.deleteAll();
        cPickorderLineBarcode.allLineBarcodesObl = null;
        return true;
    }

    //todo: this is not used?
//    public static cPickorderLineBarcode pPickorderLineBarcode(String pvScannedBarcode) {
//        if (cPickorderLineBarcode.allLineBarcodesObl == null || cPickorderLineBarcode.allLineBarcodesObl.size() == 0 ) {
//            return null;
//        }
//        for (cPickorderLineBarcode pickorderLineBarcode : cPickorderLineBarcode.allLineBarcodesObl) {
//            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcode)) {
//                return pickorderLineBarcode;
//            }
//        }return null;
//    }

}
