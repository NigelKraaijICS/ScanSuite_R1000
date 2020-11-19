package SSU_WHS.Basics.PropertyGroup;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Basics.ItemProperty.cItemPropertyEntity;
import SSU_WHS.Basics.ItemProperty.cItemPropertyRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPropertyGroupViewModel extends AndroidViewModel {
    //Region Public Properties
    public cPropertyGroupRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPropertyGroupViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPropertyGroupRepository(pvApplication);
    }
    //End Region Constructor


    //Region Public Methods
    public cWebresult pGetPropertyGroupsFromWebserviceWrs() {return this.Repository.pGetPropertyGroupsFromWebserviceWrs(); }
    public cWebresult pGetPropertyDataFromWebserviceWrs(String pvOrderTypeStr,  String pvOrderStr, String pvPropertyGroupStr ) {return this.Repository.pGetPropertyDataFromWebserviceWrs(pvOrderTypeStr,pvOrderStr,pvPropertyGroupStr); }
    public void insert(cPropertyGroupEntity pvPropertyGroupEntity) {this.Repository.pInsert(pvPropertyGroupEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods
}
