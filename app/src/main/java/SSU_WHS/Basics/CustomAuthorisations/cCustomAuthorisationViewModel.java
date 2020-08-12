package SSU_WHS.Basics.CustomAuthorisations;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cCustomAuthorisationViewModel extends AndroidViewModel {
    //Region Public Properties
    public cCustomAuthorisationRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cCustomAuthorisationViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cCustomAuthorisationRepository(pvApplication);
    }
    //End Region Constructor

    public void deleteAll() {Repository.pDeleteAll();}
    public void insert(cCustomAuthorisationEntity pvCustomAuthorisationEntity) {this.Repository.pInsert(pvCustomAuthorisationEntity);}
    public cWebresult pGetCustomAutorisationsFromWebserviceWrs() {return this.Repository.pGetCustomAutorisationsFromWebserviceWrs(); }
    public cCustomAuthorisationEntity pGetCustomAuthorisationByAutorisation(String authorisation) {return Repository.pGetCustomAuthorisationByAutorisation(authorisation);}
        //End Region Public Methods
}


