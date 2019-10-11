package SSU_WHS.Basics.ShippingAgents;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgent {

    //region Public Properties
    public String shippingAgentStr;
    public String getShippintAgentStr() {
        return shippingAgentStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public Integer importfileInt;
    public Integer getImportfileInt() {
        return importfileInt;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessageObl() {
        return errorMessagesObl;
    }

    public cShippingAgentEntity shippingAgentEntity;

    public boolean loggedInBln;
    public boolean indatabaseBln;

    public static cShippingAgentViewModel gShippingAgentViewModel;

    public static cShippingAgentViewModel getShippingAgentViewModel() {
        if (gShippingAgentViewModel == null) {
            gShippingAgentViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cShippingAgentViewModel.class);
        }
        return gShippingAgentViewModel;
    }

    public List<cShippingAgentService>  shippingAgentServicesObl() {

        List<cShippingAgentService> resultObl;

        if (cShippingAgentService.allShippingAgentServicesObl == null) {
            return  null;
        }

        resultObl =  new ArrayList<>();

        for (cShippingAgentService shippingAgentService : cShippingAgentService.allShippingAgentServicesObl)
        {
            if (shippingAgentService.shippingagentStr == this.shippingAgentStr) {
                resultObl.add(shippingAgentService);
            }
        }
        return  resultObl;
    }

    public static List<cShippingAgent> allShippingAgentsObl;
    public  static  Boolean shippingAgentsAvailableBln;

    //End region Public Properties

     //Region Constructor
    cShippingAgent(JSONObject pvJsonObject) {
        this.shippingAgentEntity = new cShippingAgentEntity(pvJsonObject);
        this.shippingAgentStr = this.shippingAgentEntity.getShippingagentStr();
        this.descriptionStr = this.shippingAgentEntity.getDescriptionStr();
        this.importfileInt = cText.stringToInteger(this.shippingAgentEntity.getImportfileStr());
    }
       //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        cShippingAgent.getShippingAgentViewModel().insert(this.shippingAgentEntity);
        this.indatabaseBln = true;

        if (cShippingAgent.allShippingAgentsObl == null){
            cShippingAgent.allShippingAgentsObl = new ArrayList<>();
        }
        cShippingAgent.allShippingAgentsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cShippingAgent.getShippingAgentViewModel().deleteAll();
        return true;
    }

    public static boolean pGetShippingAgentsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cShippingAgent.allShippingAgentsObl = null;
            cShippingAgent.pTruncateTableBln();
        }

        if ( cShippingAgent.allShippingAgentsObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cShippingAgent.getShippingAgentViewModel().pGetShippingAgentsFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cShippingAgent shippingAgent = new cShippingAgent(jsonObject);
                shippingAgent.pInsertInDatabaseBln();
            }
            cShippingAgent.shippingAgentsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTS);
            return  false;
        }
    }

    public static cShippingAgent pGetShippingAgentByStr(String pvShippingAgentStr){
        if(cShippingAgent.allShippingAgentsObl == null){
            return null;
        }

        for (cShippingAgent shippingAgent : cShippingAgent.allShippingAgentsObl)
        {
            if(shippingAgent.getShippintAgentStr() == pvShippingAgentStr){
                return  shippingAgent;
            }
        }
        return null;
    }



    //End Region Public Methods
}
