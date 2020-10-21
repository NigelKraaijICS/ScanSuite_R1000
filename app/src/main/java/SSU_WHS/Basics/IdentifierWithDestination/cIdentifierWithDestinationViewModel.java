package SSU_WHS.Basics.IdentifierWithDestination;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.cWorkplaceRepository;
import SSU_WHS.Webservice.cWebresult;

public class cIdentifierWithDestinationViewModel extends AndroidViewModel {

    //Region Public Properties
    public cIdentifierWithDestinationRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cIdentifierWithDestinationViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cIdentifierWithDestinationRepository();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetIdentifierFromWebserviceWrs(String pvIdentifierStr) {return this.Repository.pGetIdentifierFromWebserviceWrs(pvIdentifierStr); }
    //End Region Public Methods

}