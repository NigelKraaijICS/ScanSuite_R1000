package SSU_WHS.Basics.Workplaces;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import SSU_WHS.Webservice.cWebresult;
public class cWorkplaceViewModel extends AndroidViewModel {

    //Region Public Properties
    public cWorkplaceRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cWorkplaceViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cWorkplaceRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetWorkplacesFromWebserviceWrs() {return this.Repository.pGetWorkplacesFromWebserviceWrs(); }
    public void insert(cWorkplaceEntity workplaceEntity) {this.Repository.insert(workplaceEntity);}
    public void deleteAll() {Repository.deleteAll();}
    //End Region Public Methods

}