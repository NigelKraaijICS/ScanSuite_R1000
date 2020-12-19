package SSU_WHS.PackAndShip.PackAndShipOrders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cPackAndShipOrderViewModel extends AndroidViewModel {
    //Region Public Properties
    private final cPackAndShipOrderRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipOrderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipOrderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipOrderEntity pvPackAndShipOrderEntity) {this.Repository.insert(pvPackAndShipOrderEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pGetPackAndShipOrdersFromWebserviceWrs(String pvSearchTextStr) {return this.Repository.pGePackAndShipOrdersFromWebserviceWrs(pvSearchTextStr);}

    public List<cPackAndShipOrderEntity> pGetPackAndShipOrdersWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetPackAndShipOrdersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pCreatePackAndShipOrderPS1ViaWebserviceWrs(String pvDocumentStr) {return this.Repository.pCreatePackAndShipOrderPS1ViaWebserviceWrs(pvDocumentStr);}

    public cWebresult pCreatePackAndShipOrderPSMViaWebserviceWrs(String pvDocumentStr) {return this.Repository.pCreatePackAndShipOrderPSMViaWebserviceWrs(pvDocumentStr);}

    public cWebresult pGetSettingsFromWebserviceWrs() {return this.Repository.pGetLinesFromWebserviceWrs(); }


    public cWebresult pGetLinesFromWebserviceWrs() {return this.Repository.pGetLinesFromWebserviceWrs(); }

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}

    public cWebresult pGetShipmentsFromWebserviceWrs() {return this.Repository.pGetShipmentsFromWebserviceWrs();}

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

}
