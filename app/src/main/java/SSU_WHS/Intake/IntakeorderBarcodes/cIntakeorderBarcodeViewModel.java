package SSU_WHS.Intake.IntakeorderBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cIntakeorderBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    private cIntakeorderBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cIntakeorderBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cIntakeorderBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cIntakeorderBarcodeEntity pvIntakeorderBarcodeEntity) {this.Repository.insert(pvIntakeorderBarcodeEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

}
