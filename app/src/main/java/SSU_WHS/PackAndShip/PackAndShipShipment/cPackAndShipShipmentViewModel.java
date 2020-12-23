package SSU_WHS.PackAndShip.PackAndShipShipment;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPackAndShipShipmentViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipShipmentRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShipmentViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipShipmentRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity) {this.Repository.insert(pvPackAndShipShipmentEntity);}
    public void delete(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity) {this.Repository.delete(pvPackAndShipShipmentEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pHandledViaWebserviceWrs() {return this.Repository.pHandledViaWebserviceWrs(); }
    public cWebresult pShipViaWebserviceWrs(List<cShippingAgentServiceShippingUnit> pvShippingUnitsObl) {return this.Repository.pShipViaWebserviceWrs(pvShippingUnitsObl); }

}

