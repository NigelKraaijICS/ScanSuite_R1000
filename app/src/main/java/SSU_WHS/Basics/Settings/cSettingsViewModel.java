package SSU_WHS.Basics.Settings;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cSettingsViewModel extends AndroidViewModel {

    //Region Public Properties
    public cSettingsRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cSettingsViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cSettingsRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetSettingsFromWebserviceWrs() {return this.Repository.pGetSettingsFromWebserviceWrs(); }
    public void insert(cSettingsEntity pvSettingsEntity) {this.Repository.pInsert(pvSettingsEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods

}

