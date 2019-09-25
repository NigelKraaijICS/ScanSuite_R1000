package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import ICS.Weberror.cWeberror;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgentShipMethod {

    //Region Public Properties
    public String ShippingAgentStr;
    public String getShippingAgentStr() {
        return ShippingAgentStr;
    }

    public String ShippingAgentServiceStr;
    public String getShippingAgentServiceStr() {
        return ShippingAgentServiceStr;
    }

    public String ShippingMethodStr;
    public String getShippingMethodStr() {
        return ShippingMethodStr;
    }

    public String DescriptionStr;
    public String getDescriptionStr() {
        return DescriptionStr;
    }

    public String ValueTypeStr;
    public String getValueTypeStr() {
        return ValueTypeStr;
    }

    public String DefaultValueStr;
    public String getDefaultValueStr() {
        return DefaultValueStr;
    }

    public String EnumerationValuesStr;
    public String getEnumerationValuesStr() {
        return EnumerationValuesStr;
    }

    public cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity;
    public boolean indatabaseBln;

    public static cShippingAgentServiceShipMethodViewModel gShippingAgentServiceShipMethodViewModel;

    public static cShippingAgentServiceShipMethodViewModel getShippingAgentServiceShipMethodViewModel() {
        if (gShippingAgentServiceShipMethodViewModel == null) {
            gShippingAgentServiceShipMethodViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cShippingAgentServiceShipMethodViewModel.class);
        }
        return gShippingAgentServiceShipMethodViewModel;
    }

    public static List<cShippingAgentShipMethod> allShippingAgentServiceShippingMethodsObl;
    public  static  Boolean ShippingAgentServiceShippingMethodsAvailableBln;
       //End Region Public Properties


    //Region Constructor
    cShippingAgentShipMethod(JSONObject pvJsonObject) {
        this.shippingAgentServiceShipMethodEntity = new cShippingAgentServiceShipMethodEntity(pvJsonObject);
        this.ShippingAgentStr = this.shippingAgentServiceShipMethodEntity.getShippingagentStr()  ;
        this.ShippingAgentServiceStr = this.shippingAgentServiceShipMethodEntity.getServiceStr();
        this.DescriptionStr = this.shippingAgentServiceShipMethodEntity.getDescriptionStr();
        this.ValueTypeStr = this.shippingAgentServiceShipMethodEntity.getValuetypeStr();
        this.EnumerationValuesStr = this.shippingAgentServiceShipMethodEntity.getEnumerationValuesStr();
    }
    //End Region Constructor

    //Region Public Methods


    public boolean pInsertInDatabaseBln() {


        cShippingAgentShipMethod.getShippingAgentServiceShipMethodViewModel().insert(this.shippingAgentServiceShipMethodEntity);
        this.indatabaseBln = true;

        if (cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl == null){
            cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl= new ArrayList<>();
        }
        cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cShippingAgentShipMethod.getShippingAgentServiceShipMethodViewModel().deleteAll();
        return true;
    }

    public static boolean pGetShippingAgentServicesShippingUnitsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl = null;
            cShippingAgentShipMethod.pTruncateTableBln();
        }

        if (cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl   != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cShippingAgentShipMethod.getShippingAgentServiceShipMethodViewModel().pGetShippingAgentServiceShipMethodsFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cShippingAgentShipMethod shippingAgentShipMethod = new cShippingAgentShipMethod(jsonObject);
                shippingAgentShipMethod.pInsertInDatabaseBln();
            }
            cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHIPMETHODS);
            return  false;
        }
    }

    public static cShippingAgentShipMethod pGetShippingAgentServiceShipMethodByStr(String pvShippingAgentStr, String pvShippingAgentServiceStr, String pvShippingMethodStr){
        if(cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl == null){
            return null;
        }

        for (cShippingAgentShipMethod shippingAgentShipMethod : cShippingAgentShipMethod.allShippingAgentServiceShippingMethodsObl)
        {
            if(shippingAgentShipMethod.getShippingAgentStr() == pvShippingAgentStr &&
                    shippingAgentShipMethod.getShippingAgentServiceStr() == pvShippingAgentServiceStr &&
                    shippingAgentShipMethod.getShippingMethodStr() == pvShippingMethodStr) {

                return  shippingAgentShipMethod;
            }
        }
        return null;
    }


    //End Region Public Methods
}
