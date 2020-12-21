package SSU_WHS.PackAndShip.PackAndShipShippingMethod;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressEntity;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressRepository;

public class cPackAndShipShippingMethodViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipShippingMethodRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShippingMethodViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipShippingMethodRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipShippingMethodEntity pvPackAndShipShippingMethodEntity) {this.Repository.insert(pvPackAndShipShippingMethodEntity);}
    public void deleteAll() {this.Repository.deleteAll();}
}
