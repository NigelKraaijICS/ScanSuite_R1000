package SSU_WHS.Picken.PickorderLineBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cPickorderLineBarcode {

    //Public Properties
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    public Boolean isManualBln;
    public Boolean getIsManualBln() {return isManualBln;}

    public cPickorderLineBarcodeEntity pickorderLineBarcodeEntity;
    public boolean inDatabaseBln;

    public static ArrayList<cPickorderLineBarcode> allLineBarcodesObl;
    public static cPickorderLineBarcodeViewModel pickorderLineBarcodeViewModel;

    public static cPickorderLineBarcodeViewModel getPickorderLineBarcodeViewModel() {
        if (pickorderLineBarcodeViewModel == null) {
            pickorderLineBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cPickorderLineBarcodeViewModel.class);
        }
        return pickorderLineBarcodeViewModel;
    }

    public cPickorderLineBarcode (JSONObject pvJsonObject) {
        this.pickorderLineBarcodeEntity = new cPickorderLineBarcodeEntity(pvJsonObject);
        this.barcodeStr = this.pickorderLineBarcodeEntity.getBarcodeStr();
        this.lineNoLng = cText.stringToLong(this.pickorderLineBarcodeEntity.getLineNoStr());
        this.isManualBln = cText.stringToBoolean(this.pickorderLineBarcodeEntity.getIsManualStr(),false);
        this.quantityHandledDbl = cText.stringToDouble(this.pickorderLineBarcodeEntity.getQuantityhandledStr());
    }

    public cPickorderLineBarcode (Long pvLineNoLng, String pvBarcodeStr) {
        this.pickorderLineBarcodeEntity = new cPickorderLineBarcodeEntity(pvLineNoLng,pvBarcodeStr);
        this.barcodeStr = this.pickorderLineBarcodeEntity.getBarcodeStr();
        this.lineNoLng = cText.stringToLong(this.pickorderLineBarcodeEntity.getLineNoStr());
        this.isManualBln = cText.stringToBoolean(this.pickorderLineBarcodeEntity.getIsManualStr(),false);
        this.quantityHandledDbl = cText.stringToDouble(this.pickorderLineBarcodeEntity.getQuantityhandledStr());
    }

    public boolean pInsertInDatabaseBln() {
        cPickorderLineBarcode.getPickorderLineBarcodeViewModel().insert(this.pickorderLineBarcodeEntity);
        this.inDatabaseBln = true;
        if (cPickorderLineBarcode.allLineBarcodesObl == null) {
            cPickorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cPickorderLineBarcode.allLineBarcodesObl.add(this);
        return true;
    }

    public boolean pDeleteFromDatabaseBln() {
        cPickorderLineBarcode.getPickorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoLng().intValue());
        this.inDatabaseBln = false;
        if (cPickorderLineBarcode.allLineBarcodesObl != null) {
            cPickorderLineBarcode.allLineBarcodesObl.remove(this);
        }
        return true;
    }

    public boolean pUpdateAmountInDatabaseBln(){
        cPickorderLineBarcode.getPickorderLineBarcodeViewModel().pUpdateAmountForLineNo(this.getBarcodeStr(), this.getQuantityhandledDbl());
        return true;
    }

    public static boolean pTruncateTableBln() {
        cPickorderLineBarcode.getPickorderLineBarcodeViewModel().deleteAll();
        cPickorderLineBarcode.allLineBarcodesObl = null;
        return true;
    }

    public static cPickorderLineBarcode pPickorderLineBarcode(String pvScannedBarcode) {
        if (cPickorderLineBarcode.allLineBarcodesObl == null || cPickorderLineBarcode.allLineBarcodesObl.size() == 0 ) {
            return null;
        }
        for (cPickorderLineBarcode pickorderLineBarcode : cPickorderLineBarcode.allLineBarcodesObl) {
            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcode) == true) {
                return pickorderLineBarcode;
            }
        }return null;
    }

    public static List <cPickorderLineBarcode> pPickorderLineNumberBarcodeObl(Long pvLineNo) {
        if (cPickorderLineBarcode.allLineBarcodesObl == null || cPickorderLineBarcode.allLineBarcodesObl.size() == 0 ) {
        return null;
        }
        List <cPickorderLineBarcode> resultObl = null;

        for (cPickorderLineBarcode pickorderLineBarcode : cPickorderLineBarcode.allLineBarcodesObl) {
            if (pickorderLineBarcode.getLineNoLng().equals(pvLineNo) == true) {
                if (resultObl == null ){
                    resultObl = new ArrayList<>();
                }
                resultObl.add(pickorderLineBarcode);
            }
        }return resultObl;
    }
}
