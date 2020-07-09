package SSU_WHS.Move.MoveorderLineBarcode;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeEntity;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeViewModel;

public class cMoveorderLineBarcode {

    private cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity;

    public static List<cMoveorderLineBarcode> allMoveorderLineBarcodesObl;
    //Region Public Properties

    public int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }



    public String barcode;
    public String getBarcodeStr() {
        return this.barcode;
    }

    public Double quantityHandled;
    public Double getQuantityHandled() {
        return this.quantityHandled;
    }

    private cMoveorderLineBarcodeViewModel getMoveorderLineBarcodeViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineBarcodeViewModel.class);
    }



    //Region Constructor
    public cMoveorderLineBarcode(JSONObject pvJsonObject) {
        this.moveorderLineBarcodeEntity = new cMoveorderLineBarcodeEntity(pvJsonObject);
        this.lineNoInt = this.moveorderLineBarcodeEntity.getLineNoInt();
        this.barcode = this.moveorderLineBarcodeEntity.getBarcodeStr();
        this.quantityHandled = this.moveorderLineBarcodeEntity.getQuantityHandled();
    }


    //End Region Constructor



    public static boolean pTruncateTableBln(){

        cMoveorderBarcodeViewModel moveorderBarcodeViewModel=   new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderBarcodeViewModel.class);
        moveorderBarcodeViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getMoveorderLineBarcodeViewModel().insert(this.moveorderLineBarcodeEntity);

        if (cMoveorderLineBarcode.allMoveorderLineBarcodesObl == null){
            cMoveorderLineBarcode.allMoveorderLineBarcodesObl  = new ArrayList<>();
        }
        cMoveorderLineBarcode.allMoveorderLineBarcodesObl .add(this);
        return  true;
    }

}