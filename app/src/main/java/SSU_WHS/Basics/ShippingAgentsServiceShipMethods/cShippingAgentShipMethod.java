package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgentShipMethod {

    //Region Public Properties
    private String ShippingAgentStr;
    public String getShippingAgentStr() {
        return ShippingAgentStr;
    }

    private String ShippingAgentServiceStr;
    public String getShippingAgentServiceStr() {
        return ShippingAgentServiceStr;
    }

    private String DescriptionStr;
    public String getDescriptionStr() {
        return DescriptionStr;
    }

    private cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity;

    private cShippingAgentServiceShipMethodViewModel getShippingAgentServiceShipMethodViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceShipMethodViewModel.class);
    }

    private static List<cShippingAgentShipMethod> allShippingAgentServiceShippingMethodsObl;
    public  static  Boolean ShippingAgentServiceShippingMethodsAvailableBln;
       //End Region Public Properties


    //Region Constructor
    private cShippingAgentShipMethod(JSONObject pvJsonObject) {
        this.shippingAgentServiceShipMethodEntity = new cShippingAgentServiceShipMethodEntity(pvJsonObject);
        this.ShippingAgentStr = this.shippingAgentServiceShipMethodEntity.getShippingagentStr()  ;
        this.ShippingAgentServiceStr = this.shippingAgentServiceShipMethodEntity.getServiceStr();
        this.DescriptionStr = this.shippingAgentServiceShipMethodEntity.getDescriptionStr();
    }
    //End Region Constructor

    //Region Public Methods


    public boolean pInsertInDatabaseBln() {

        this.getShippingAgentServiceShipMethodViewModel().insert(this.shippingAgentServiceShipMethodEntity);

        if (cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl == null){
            cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl= new ArrayList<>();
        }
        cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cShippingAgentServiceShipMethodViewModel shippingAgentServiceShipMethodViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceShipMethodViewModel.class);
        shippingAgentServiceShipMethodViewModel.deleteAll();
        return true;
    }

    public static void pGetShippingAgentServicesShippingUnitsViaWebservice(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl = null;
            cShippingAgentShipMethod.pTruncateTableBln();
        }

        if (cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl   != null) {
            return;
        }

        cWebresult WebResult;
        cShippingAgentServiceShipMethodViewModel shippingAgentServiceShipMethodViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceShipMethodViewModel.class);
        WebResult =  shippingAgentServiceShipMethodViewModel.pGetShippingAgentServiceShipMethodsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cShippingAgentShipMethod shippingAgentShipMethod = new cShippingAgentShipMethod(jsonObject);
                shippingAgentShipMethod.pInsertInDatabaseBln();
            }
            cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln = true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHIPMETHODS);
        }
    }

    //End Region Public Methods
}
