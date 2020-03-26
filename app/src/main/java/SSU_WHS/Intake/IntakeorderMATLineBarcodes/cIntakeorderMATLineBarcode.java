package SSU_WHS.Intake.IntakeorderMATLineBarcodes;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cIntakeorderMATLineBarcode {

    //Public Properties
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    private cIntakeorderMATLineBarcodeEntity intakeorderMATLineBarcodeEntity;

    public static ArrayList<cIntakeorderMATLineBarcode> allMATLineBarcodesObl;

    private cIntakeorderMATLineBarcodeViewModel getIntakeorderMATLineBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderMATLineBarcodeViewModel.class);
    }

    //End Public Properties

    //Region Constructor
    public cIntakeorderMATLineBarcode(JSONObject pvJsonObject) {
        this.intakeorderMATLineBarcodeEntity = new cIntakeorderMATLineBarcodeEntity(pvJsonObject);
        this.lineNoLng = this.intakeorderMATLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.intakeorderMATLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.intakeorderMATLineBarcodeEntity.getQuantityhandledStr());
        }

    public cIntakeorderMATLineBarcode(Long pvLineNoLng, String pvBarcodeStr, Double pvQuantityHandledDbl ){
        this.intakeorderMATLineBarcodeEntity = new cIntakeorderMATLineBarcodeEntity(pvLineNoLng,pvBarcodeStr);
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledDbl = pvQuantityHandledDbl;
    }
    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        this.getIntakeorderMATLineBarcodeViewModel().insert(this.intakeorderMATLineBarcodeEntity);

        if (cIntakeorderMATLineBarcode.allMATLineBarcodesObl == null){
            cIntakeorderMATLineBarcode.allMATLineBarcodesObl = new ArrayList<>();
        }
        cIntakeorderMATLineBarcode.allMATLineBarcodesObl.add(this);
        return  true;
    }

    public boolean pDeleteFromDatabaseBln() {
        this.getIntakeorderMATLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoLng().intValue());
        if (cIntakeorderMATLineBarcode.allMATLineBarcodesObl != null) {
            cIntakeorderMATLineBarcode.allMATLineBarcodesObl.remove(this);
        }
        return true;
    }

    public void pUpdateAmountInDatabase(){
        this.getIntakeorderMATLineBarcodeViewModel().pUpdateAmountForLineNo(this.getBarcodeStr(), this.getQuantityhandledDbl());
    }

    public static boolean pTruncateTableBln(){
        cIntakeorderMATLineBarcodeViewModel intakeorderMATLineBarcodeViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderMATLineBarcodeViewModel.class);
        intakeorderMATLineBarcodeViewModel.deleteAll();
        return true;
    }



}