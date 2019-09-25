package SSU_WHS.Basics.ItemProperty;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cItemPropertyViewModel extends AndroidViewModel {
    //Region Public Properties
    public cItemPropertyRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cItemPropertyViewModel (Application pvApplication) {
        super(pvApplication);
        this.Repository = new cItemPropertyRepository(pvApplication);
    }
    //End Region Constructor


    //Region Public Methods
    public cWebresult pGetItemPropertyFromWebserviceWrs() {return this.Repository.pGetItemPropertyFromWebserviceWrs(); }
    public void insert(cItemPropertyEntity pvItemPropertyEntity) {this.Repository.pInsert(pvItemPropertyEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods
}
