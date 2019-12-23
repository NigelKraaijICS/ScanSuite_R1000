package SSU_WHS.Move.MoveorderLineBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cMoveorderLineBarcode {

    //Public Properties
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    public cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity;
    public boolean inDatabaseBln;

    public static ArrayList<cMoveorderLineBarcode> allLineBarcodesObl;
    public static cMoveorderLineBarcodeViewModel moveorderLineBarcodeViewModel;

    public static cMoveorderLineBarcode currentMoveorderLineBarcode;

    public static cMoveorderLineBarcodeViewModel getMoveorderLineBarcodeViewModel() {
        if (moveorderLineBarcodeViewModel == null) {
            moveorderLineBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cMoveorderLineBarcodeViewModel.class);
        }
        return moveorderLineBarcodeViewModel;
    }

    //End Public Properties

    //Region Constructor
    public cMoveorderLineBarcode(JSONObject pvJsonObject) {
        this.moveorderLineBarcodeEntity = new cMoveorderLineBarcodeEntity(pvJsonObject);
        this.lineNoLng = this.moveorderLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.moveorderLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.moveorderLineBarcodeEntity.getQuantityhandledStr());
        }

    public cMoveorderLineBarcode(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity){
        this.moveorderLineBarcodeEntity = pvMoveorderLineBarcodeEntity;
        this.lineNoLng = this.moveorderLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.moveorderLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.moveorderLineBarcodeEntity.getQuantityhandledStr());
    }

    public cMoveorderLineBarcode(Long pvLineNoLng, String pvBarcodeStr, Double pvQuantityHandledDbl ){
        this.moveorderLineBarcodeEntity = null;
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledDbl = pvQuantityHandledDbl;
    }

    public cMoveorderLineBarcode (Long pvLineNoLng, String pvBarcodeStr) {
        this.moveorderLineBarcodeEntity = new cMoveorderLineBarcodeEntity(pvLineNoLng,pvBarcodeStr);
        this.barcodeStr = this.moveorderLineBarcodeEntity.getBarcodeStr();
        this.lineNoLng = this.moveorderLineBarcodeEntity.getLineNoLng();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.moveorderLineBarcodeEntity.getQuantityhandledStr());
    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        cMoveorderLineBarcode.getMoveorderLineBarcodeViewModel().insert(this.moveorderLineBarcodeEntity);
        this.inDatabaseBln = true;

        if (cMoveorderLineBarcode.allLineBarcodesObl == null){
            cMoveorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cMoveorderLineBarcode.allLineBarcodesObl.add(this);
        return  true;
    }

   public static boolean pTruncateTableBln(){
        cMoveorderLineBarcode.getMoveorderLineBarcodeViewModel().deleteAll();
        return true;
    }
    public boolean pUpdateAmountInDatabaseBln(){
        cMoveorderLineBarcode.getMoveorderLineBarcodeViewModel().pUpdateAmountForLineNo(this.getBarcodeStr(), this.getQuantityhandledDbl());
        return true;
    }
    public boolean pDeleteFromDatabaseBln() {
        cMoveorderLineBarcode.getMoveorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoLng().intValue());
        this.inDatabaseBln = false;
        if (cMoveorderLineBarcode.allLineBarcodesObl != null) {
            cMoveorderLineBarcode.allLineBarcodesObl.remove(this);
        }
        return true;
    }

}
