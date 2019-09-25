package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cShippingAgentServiceShipMethodViewModel extends AndroidViewModel {

    //Region Public Properties
    public cShippingAgentServiceShipMethodRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cShippingAgentServiceShipMethodViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cShippingAgentServiceShipMethodRepository(pvApplication);
    }
    //End Region Constructor

    public cWebresult pGetShippingAgentServiceShipMethodsFromWebserviceWrs() {return this.Repository.pGetShippingAgentServiceShipMethodsFromWebserviceWrs(); }
    public void insert(cShippingAgentServiceShipMethodEntity pvShippingAgentServiceShipMethodEntity) {Repository.pInsert(pvShippingAgentServiceShipMethodEntity);}
    public void deleteAll() {Repository.pDeleteAll();}

}
