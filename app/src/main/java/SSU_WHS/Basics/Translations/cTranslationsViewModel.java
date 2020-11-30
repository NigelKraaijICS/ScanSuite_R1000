package SSU_WHS.Basics.Translations;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.cWorkplaceRepository;
import SSU_WHS.Webservice.cWebresult;

public class cTranslationsViewModel extends AndroidViewModel {

    //Region Public Properties
    public cTranslationRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cTranslationsViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cTranslationRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetTranslationsFromWebserviceWrs() {return this.Repository.pGetTranslationsFromWebserviceWrs(); }
    public void insert(cTranslationEntity pvTranslationEntity) {this.Repository.insert(pvTranslationEntity);}
    public void deleteAll() {Repository.deleteAll();}
    //End Region Public Methods

}