package SSU_WHS.Move.MoveorderBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cMoveorderBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    private cMoveorderBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cMoveorderBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cMoveorderBarcodeEntity pvMoveorderBarcodeEntity) {this.Repository.insert(pvMoveorderBarcodeEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pCreateBarcodeViaWebserviceWrs() {return this.Repository.pCreateBarcodeViaWebserviceWrs();}
}
