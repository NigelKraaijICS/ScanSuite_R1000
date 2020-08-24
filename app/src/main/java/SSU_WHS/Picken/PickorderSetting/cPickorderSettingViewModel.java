package SSU_WHS.Picken.PickorderSetting;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressRepository;

public class cPickorderSettingViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPickorderSettingRepository repository;
    //Eend Region Public Properties

    //Region Constructor
    public cPickorderSettingViewModel(Application pvApplication) {
        super(pvApplication);
        this.repository = new cPickorderSettingRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPickorderSettingEntity pvPickorderSettingEntity) {
        repository.pInsert(pvPickorderSettingEntity);
    }

    public void deleteAll() {
        repository.pDeleteAll();
    }

    //End Region Public Methods


}
