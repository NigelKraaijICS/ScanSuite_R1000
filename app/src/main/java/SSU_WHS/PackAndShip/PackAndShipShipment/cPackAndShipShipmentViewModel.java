package SSU_WHS.PackAndShip.PackAndShipShipment;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingRepository;

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



}

