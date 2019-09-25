package SSU_WHS.Basics.ShippingAgents;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cShippingAgentViewModel extends AndroidViewModel {

    //Region Public Properties
    public cShippingAgentRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cShippingAgentViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cShippingAgentRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetShippingAgentsFromWebserviceWrs() {return this.Repository.pGetShippingAgentsFromWebserviceWrs(); }
    public void insert(cShippingAgentEntity pvShippingAgentEntity) {this.Repository.pInsert(pvShippingAgentEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods
}
