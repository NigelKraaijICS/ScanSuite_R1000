package SSU_WHS.Basics.Authorisations;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cAuthorisationViewModel  extends AndroidViewModel {
    //Region Public Properties
    public cAuthorisationRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cAuthorisationViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cAuthorisationRepository(pvApplication);
    }
    //End Region Constructor

    public void deleteAll() {Repository.pDeleteAll();}
    public void insert(cAuthorisationEntity pvAuthorisationEntity) {this.Repository.pInsert(pvAuthorisationEntity);}
    public cWebresult pGetAutorisationsFromWebserviceWrs() {return this.Repository.pGetAutorisationsFromWebserviceWrs(); }
        //End Region Public Methods
}


