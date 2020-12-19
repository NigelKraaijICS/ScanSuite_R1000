package SSU_WHS.PackAndShip.PackAndShipSetting;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cPackAndShipSettingViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipSettingRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipSettingViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipSettingRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipSettingEntity pvPackAndShipSettingEntity) {this.Repository.insert(pvPackAndShipSettingEntity);}
    public void delete(cPackAndShipSettingEntity pvPackAndShipSettingEntity) {this.Repository.delete(pvPackAndShipSettingEntity);}
    public void deleteAll() {this.Repository.deleteAll();}



}

