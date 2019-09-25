package SSU_WHS.Basics.Branches;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cBranchViewModel extends AndroidViewModel {

    //Region Public Properties
    public cBranchRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cBranchViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cBranchRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetBranchesFromWebserviceWrs() {return this.Repository.pGetBranchesFromWebserviceWrs(); }
    public void insert(cBranchEntity pvBranchEntity) {this.Repository.pInsert(pvBranchEntity);}
    public void deleteAll() {Repository.pDeleteAll();}

    //End Region Public Methods

}
