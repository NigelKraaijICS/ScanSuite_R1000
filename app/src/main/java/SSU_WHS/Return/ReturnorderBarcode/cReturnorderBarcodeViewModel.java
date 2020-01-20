package SSU_WHS.Return.ReturnorderBarcode;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cReturnorderBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    private cReturnorderBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cReturnorderBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cReturnorderBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cReturnorderBarcodeEntity pvReturnorderBarcodeEntity) {this.Repository.insert(pvReturnorderBarcodeEntity);}
    public void deleteAll() {this.Repository.deleteAll();}
    public cWebresult pCreateBarcodeViaWebserviceWrs() {return this.Repository.pCreateBarcodeViaWebserviceWrs();}
}
