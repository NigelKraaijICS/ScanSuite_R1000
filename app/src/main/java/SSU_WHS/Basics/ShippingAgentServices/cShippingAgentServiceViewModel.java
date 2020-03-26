package SSU_WHS.Basics.ShippingAgentServices;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;

public class cShippingAgentServiceViewModel extends AndroidViewModel {


    //Region Public Properties
    public cShippingAgentServiceRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cShippingAgentServiceViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cShippingAgentServiceRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetShippingAgentServicesFromWebserviceWrs() throws ExecutionException {return this.Repository.pGetShippingAgentServicesFromWebserviceWrs(); }
    public void insert(cShippingAgentServiceEntity pvShippingAgentServiceEntity) {this.Repository.pInsert(pvShippingAgentServiceEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods

}
