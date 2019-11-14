package SSU_WHS.Intake.IntakeorderMATLineBarcodes;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cIntakeorderMATLineBarcode {

    //Public Properties
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    public double quantityHandledDbl;
    public double getQuantityhandledDbl() {return quantityHandledDbl;}

    public cIntakeorderMATLineBarcodeEntity intakeorderMATLineBarcodeEntity;
    public boolean inDatabaseBln;

    public static ArrayList<cIntakeorderMATLineBarcode> allMATLineBarcodesObl;
    public static cIntakeorderMATLineBarcodeViewModel intakeorderMATLineBarcodeViewModel;

    public static cIntakeorderMATLineBarcode currentIntakeorderMATLineBarcode;

    public static cIntakeorderMATLineBarcodeViewModel getIntakeorderMATLineBarcodeViewModel() {
        if (intakeorderMATLineBarcodeViewModel == null) {
            intakeorderMATLineBarcodeViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cIntakeorderMATLineBarcodeViewModel.class);
        }
        return intakeorderMATLineBarcodeViewModel;
    }

    //End Public Properties

    //Region Constructor
    public cIntakeorderMATLineBarcode(JSONObject pvJsonObject) {
        this.intakeorderMATLineBarcodeEntity = new cIntakeorderMATLineBarcodeEntity(pvJsonObject);
        this.lineNoLng = this.intakeorderMATLineBarcodeEntity.getLineNoLng();
        this.barcodeStr = this.intakeorderMATLineBarcodeEntity.getBarcodeStr();
        this.quantityHandledDbl = cText.pStringToDoubleDbl(this.intakeorderMATLineBarcodeEntity.getQuantityhandledStr());
        }

    public cIntakeorderMATLineBarcode(cIntakeorderMATLineBarcodeEntity pvIntakeorderMATLineBarcodeEntity){
        this.intakeorderMATLineBarcodeEntity = pvIntakeorderMATLineBarcodeEntity;
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
        cIntakeorderMATLineBarcode.getIntakeorderMATLineBarcodeViewModel().insert(this.intakeorderMATLineBarcodeEntity);
        this.inDatabaseBln = true;

        if (cIntakeorderMATLineBarcode.allMATLineBarcodesObl == null){
            cIntakeorderMATLineBarcode.allMATLineBarcodesObl = new ArrayList<>();
        }
        cIntakeorderMATLineBarcode.allMATLineBarcodesObl.add(this);
        return  true;
    }

    public boolean pDeleteFromDatabaseBln() {
        cIntakeorderMATLineBarcode.getIntakeorderMATLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoLng().intValue());
        this.inDatabaseBln = false;
        if (cIntakeorderMATLineBarcode.allMATLineBarcodesObl != null) {
            cIntakeorderMATLineBarcode.allMATLineBarcodesObl.remove(this);
        }
        return true;
    }

    public boolean pUpdateAmountInDatabaseBln(){
        cIntakeorderMATLineBarcode.getIntakeorderMATLineBarcodeViewModel().pUpdateAmountForLineNo(this.getBarcodeStr(), this.getQuantityhandledDbl());
        return true;
    }

    public static boolean pTruncateTableBln(){
        cIntakeorderMATLineBarcode.getIntakeorderMATLineBarcodeViewModel().deleteAll();
        return true;
    }



}