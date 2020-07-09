package SSU_WHS.Basics.Scanners;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cScannerViewModel extends AndroidViewModel {

    //Region Public Properties
    public cScannerRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cScannerViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cScannerRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetScannersFromWebserviceWrs() {return this.Repository.pGetScannersFromWebserviceWrs(); }
    public void insert(cScannerEntity workplaceEntity) {this.Repository.insert(workplaceEntity);}
    public void deleteAll() {Repository.deleteAll();}
    //End Region Public Methods

}