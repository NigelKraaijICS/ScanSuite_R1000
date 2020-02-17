package SSU_WHS.Basics.Users;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cUserViewModel extends AndroidViewModel {

    //Region Public Properties
    public cUserRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cUserViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cUserRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetUsersFromWebserviceWrs() {return this.Repository.pGetUsersFromWebserviceWrs(); }
    public void insert(cUserEntity pvUserEntity) {this.Repository.pInsert(pvUserEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    public cWebresult pUserLoginViaWebserviceWrs(String pvPassword) {return this.Repository.userLogonWrs(pvPassword);}
    //End Region Public Methods

}

