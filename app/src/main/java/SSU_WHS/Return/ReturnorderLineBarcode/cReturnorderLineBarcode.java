package SSU_WHS.Return.ReturnorderLineBarcode;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cReturnorderLineBarcode {

    //Public Properties
    private Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    private String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    private cReturnorderLineBarcodeEntity returnorderLineBarcodeEntity;

    public static ArrayList<cReturnorderLineBarcode> allLineBarcodesObl;
    public static cReturnorderLineBarcode currentreturnorderLineBarcode;

    private cReturnorderLineBarcodeViewModel getReturnorderLineBarcodeViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineBarcodeViewModel.class);
    }


    //End Public Properties

    //Region Constructor
    public cReturnorderLineBarcode(JSONObject pvJsonObject) {
        this.returnorderLineBarcodeEntity = new cReturnorderLineBarcodeEntity(pvJsonObject);
        this.lineNoLng = this.returnorderLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.returnorderLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.returnorderLineBarcodeEntity.getQuantityhandledStr());
        }

    public cReturnorderLineBarcode(Long pvLineNoLng, String pvBarcodeStr, Double pvQuantityHandled){
        this.returnorderLineBarcodeEntity = new cReturnorderLineBarcodeEntity(pvLineNoLng, pvBarcodeStr, pvQuantityHandled);
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledDbl = pvQuantityHandled;
    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        this.getReturnorderLineBarcodeViewModel().insert(this.returnorderLineBarcodeEntity);

        if (cReturnorderLineBarcode.allLineBarcodesObl == null){
            cReturnorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }
        cReturnorderLineBarcode.allLineBarcodesObl.add(this);
        return  true;
    }

   public static boolean pTruncateTableBln(){
       cReturnorderLineBarcodeViewModel returnorderLineBarcodeViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineBarcodeViewModel.class);
       returnorderLineBarcodeViewModel.deleteAll();
        return true;
    }

}
