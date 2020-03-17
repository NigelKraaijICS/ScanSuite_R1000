package SSU_WHS.Basics.Packaging;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cPackagingViewModel extends AndroidViewModel{
    //Region Public Properties
    public cPackagingRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackagingViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackagingRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetPackagingFromWebserviceWrs() {return this.Repository.pGetPackagingFromWebserviceWrs(); }
    public void insert(cPackagingEntity pvPackagingEntity) {this.Repository.pInsert(pvPackagingEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods

}



