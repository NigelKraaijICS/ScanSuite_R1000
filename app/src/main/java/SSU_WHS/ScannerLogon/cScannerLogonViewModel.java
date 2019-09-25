package SSU_WHS.ScannerLogon;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import SSU_WHS.Webservice.cWebresult;

public class cScannerLogonViewModel extends AndroidViewModel {

    //Region Public Properties
    public cScannerLogonRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cScannerLogonViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cScannerLogonRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pScannerLogonWrs() {return this.Repository.pScannerLogonWrs(); }
    public void insert(cScannerLogonEntity scannerLogonEntity) {Repository.insert(scannerLogonEntity);}
    public void deleteAll() {Repository.deleteAll();}
    //End Region Public Methods
}
