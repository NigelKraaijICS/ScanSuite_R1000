package SSU_WHS.PackAndShip.PackAndShipAddress;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcodeEntity;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcodeRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPackAndShipAddressViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipAddressRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipAddressViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipAddressRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipAddressEntity pvPackAndShipAddressEntity) {this.Repository.insert(pvPackAndShipAddressEntity);}
    public void deleteAll() {this.Repository.deleteAll();}
}
