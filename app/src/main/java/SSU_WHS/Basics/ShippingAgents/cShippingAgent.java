package SSU_WHS.Basics.ShippingAgents;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgent {

    //region Public Properties
    private String shippingAgentStr;
    public String getShippingAgentStr() {
        return shippingAgentStr;
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private cShippingAgentEntity shippingAgentEntity;

    private cShippingAgentViewModel getShippingAgentViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentViewModel.class);
    }

    public List<cShippingAgentService>  shippingAgentServicesObl() {

        List<cShippingAgentService> resultObl;

        if (cShippingAgentService.allShippingAgentServicesObl == null) {
            return  null;
        }

        resultObl =  new ArrayList<>();

        for (cShippingAgentService shippingAgentService : cShippingAgentService.allShippingAgentServicesObl)
        {
            if (shippingAgentService.shippingagentStr.equalsIgnoreCase(this.shippingAgentStr)) {
                resultObl.add(shippingAgentService);
            }
        }
        return  resultObl;
    }

    public static List<cShippingAgent> allShippingAgentsObl;
    public  static  Boolean shippingAgentsAvailableBln;

    //End region Public Properties

     //Region Constructor
     private cShippingAgent(JSONObject pvJsonObject) {
        this.shippingAgentEntity = new cShippingAgentEntity(pvJsonObject);
        this.shippingAgentStr = this.shippingAgentEntity.getShippingagentStr();
        this.descriptionStr = this.shippingAgentEntity.getDescriptionStr();
    }
       //End Region Constructor

    //Region Public Methods

    public static cShippingAgent pGetShippingAgentByCodeStr(String pvShippingAgentCodeStr){

         if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0 ) {
             return  null;
         }

         for (cShippingAgent shippingAgent : cShippingAgent.allShippingAgentsObl) {

             if (shippingAgent.getShippingAgentStr().equalsIgnoreCase(pvShippingAgentCodeStr)) {
                 return shippingAgent;
             }
         }

         return  null;

    }

    public static cShippingAgent pGetShippingAgentByDescriptionStr(String pvShippingAgentCodeDescriptionStr){

        if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0 ) {
            return  null;
        }

        for (cShippingAgent shippingAgent : cShippingAgent.allShippingAgentsObl) {

            if (shippingAgent.getDescriptionStr().equalsIgnoreCase(pvShippingAgentCodeDescriptionStr)) {
                return shippingAgent;
            }
        }

        return  null;
    }

    public  cShippingAgentService pGetShippingAgentServiceByDescriptionStr(String pvShippingAgentServiceCodeDescriptionStr){

        if (this.shippingAgentServicesObl() == null || this.shippingAgentServicesObl().size() == 0 ) {
            return  null;
        }

        for (cShippingAgentService shippingAgentService : this.shippingAgentServicesObl()) {

            if (shippingAgentService.getDescriptionStr().equalsIgnoreCase(pvShippingAgentServiceCodeDescriptionStr)) {
                return shippingAgentService;
            }
        }

        return  null;
    }

    public boolean pInsertInDatabaseBln() {
        this.getShippingAgentViewModel().insert(this.shippingAgentEntity);

        if (cShippingAgent.allShippingAgentsObl == null){
            cShippingAgent.allShippingAgentsObl = new ArrayList<>();
        }
        cShippingAgent.allShippingAgentsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cShippingAgentViewModel shippingAgentViewModel =    new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentViewModel.class);
        shippingAgentViewModel.deleteAll();
        return true;
    }

    public static void pGetShippingAgentsViaWebservice(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cShippingAgent.allShippingAgentsObl = null;
            cShippingAgent.pTruncateTableBln();
        }

        if ( cShippingAgent.allShippingAgentsObl != null) {
            return;
        }

        cWebresult WebResult;
        cShippingAgentViewModel shippingAgentViewModel =    new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentViewModel.class);
        WebResult =  shippingAgentViewModel.pGetShippingAgentsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cShippingAgent shippingAgent = new cShippingAgent(jsonObject);
                shippingAgent.pInsertInDatabaseBln();
            }
            cShippingAgent.shippingAgentsAvailableBln = true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTS);
        }
    }


    //End Region Public Methods
}
