package SSU_WHS.Move.MoveorderLineBarcodes;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineViewModel;

public class cMoveorderLineBarcode {

    //Public Properties
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    private cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity;

    public static ArrayList<cMoveorderLineBarcode> allLineBarcodesObl;
    public static cMoveorderLineBarcodeViewModel moveorderLineBarcodeViewModel;

    public static cMoveorderLineBarcode currentMoveorderLineBarcode;


    private cMoveorderLineBarcodeViewModel getMoveorderLineBarcodeViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineBarcodeViewModel.class);
    }


    //End Public Properties

    //Region Constructor
    public cMoveorderLineBarcode(JSONObject pvJsonObject) {
        this.moveorderLineBarcodeEntity = new cMoveorderLineBarcodeEntity(pvJsonObject);
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
        this.getMoveorderLineBarcodeViewModel().insert(this.moveorderLineBarcodeEntity);

        if (cMoveorderLineBarcode.allLineBarcodesObl == null){
            cMoveorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cMoveorderLineBarcode.allLineBarcodesObl.add(this);
        return  true;
    }

   public static boolean pTruncateTableBln(){

       cMoveorderLineBarcodeViewModel moveorderLineBarcodeViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineBarcodeViewModel.class);
       moveorderLineBarcodeViewModel.deleteAll();
        return true;
    }

    public void pUpdateAmountInDatabase(){
        this.getMoveorderLineBarcodeViewModel().pUpdateAmountForLineNo(this.getBarcodeStr(), this.getQuantityhandledDbl());
    }

    public boolean pDeleteFromDatabaseBln() {
        this.getMoveorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoLng().intValue());
        if (cMoveorderLineBarcode.allLineBarcodesObl != null) {
            cMoveorderLineBarcode.allLineBarcodesObl.remove(this);
        }
        return true;
    }

}
