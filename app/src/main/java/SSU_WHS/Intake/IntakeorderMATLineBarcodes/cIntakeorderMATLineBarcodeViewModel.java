package SSU_WHS.Intake.IntakeorderMATLineBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cIntakeorderMATLineBarcodeViewModel extends AndroidViewModel {
    public cIntakeorderMATLineBarcodeRepository Repository;

    public cIntakeorderMATLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cIntakeorderMATLineBarcodeRepository(pvApplication);
    }

    public void deleteAll() { this.Repository.pDeleteAll(); }
    public void pDeleteForLineNo(Integer pvLineNoInt) {this.Repository.pDeleteForLineNo(pvLineNoInt);}




    public void pUpdateAmountForLineNo(String pvBarcodeStr, Double pvAmoundDbl) {this.Repository.updateBarcodeAmount(pvBarcodeStr,pvAmoundDbl);}

    public void insert(cIntakeorderMATLineBarcodeEntity pvIntakeorderMATLineBarcodeEntity) { Repository.pInsert(pvIntakeorderMATLineBarcodeEntity); }
}
