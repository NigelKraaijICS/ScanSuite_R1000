package SSU_WHS.PackAndShip.PackAndShipShippingPackage;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressEntity;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressRepository;

public class cPackAndShipShippingPackageViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipShippingPackageRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShippingPackageViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipShippingPackageRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipShippingPackageEntity pvPackAndShipShippingPackageEntity) {this.Repository.insert(pvPackAndShipShippingPackageEntity);}
    public void deleteAll() {this.Repository.deleteAll();}
}
