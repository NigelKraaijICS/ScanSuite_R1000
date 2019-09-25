package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cShippingAgentServiceShippingUnitViewModel extends AndroidViewModel {

    //Region Public Properties
    public cShippingAgentServiceShippingUnitRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cShippingAgentServiceShippingUnitViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cShippingAgentServiceShippingUnitRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetShippingAgentServiceShippingUnitsFromWebserviceWrs() {return this.Repository.pGetShippingAgentServiceShippingUnitsFromWebserviceWrs(); }
    public void insert(cShippingAgentServiceShippingUnitEntity pvShippingAgentServiceShippingUnitEntity) {Repository.pInsert(pvShippingAgentServiceShippingUnitEntity);}
    public void deleteAll() {Repository.pDeleteAll();}

    //End Region Public Methods
}
