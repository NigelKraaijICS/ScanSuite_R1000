package SSU_WHS.Basics.ShippingAgentServices;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgentService {

    //Region Public Properties
    public String shippingagentStr;
    public String getShippingAgentStr() {
        return shippingagentStr;
    }

    private String serviceStr;
    public String getServiceStr() {
        return serviceStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private cShippingAgentServiceEntity shippingAgentServiceEntity;

    private cShippingAgentServiceViewModel getShippingAgentServiceViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceViewModel.class);
    }

    public static List<cShippingAgentService> allShippingAgentServicesObl;
    public  static  Boolean shippingAgentServicesAvailableBln;


    public  List<cShippingAgentServiceShippingUnit>  shippingUnitsObl () {

        List<cShippingAgentServiceShippingUnit> resultObl = new ArrayList<>();


        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null || cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.size() == 0) {
            return  resultObl;
        }

        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl) {
            if (shippingAgentServiceShippingUnit.getShippingAgentStr().equalsIgnoreCase(this.getShippingAgentStr()) &&
                shippingAgentServiceShippingUnit.getShippingAgentServiceStr().equalsIgnoreCase(this.getServiceStr())) {
                resultObl.add(shippingAgentServiceShippingUnit);
            }
        }


        return  resultObl;

    }

    //End Region Public Properties

    //Region Constructor
    private cShippingAgentService(JSONObject pvJsonObject) {
        this.shippingAgentServiceEntity = new cShippingAgentServiceEntity(pvJsonObject);
        this.shippingagentStr = this.shippingAgentServiceEntity.getShippingagentStr();
        this.serviceStr = this.shippingAgentServiceEntity.getServiceStr();
        this.descriptionStr = this.shippingAgentServiceEntity.getDescriptionStr();
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {

        this.getShippingAgentServiceViewModel().insert(this.shippingAgentServiceEntity);

        if (cShippingAgentService.allShippingAgentServicesObl == null){
            cShippingAgentService.allShippingAgentServicesObl= new ArrayList<>();
        }
        cShippingAgentService.allShippingAgentServicesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cShippingAgentServiceViewModel shippingAgentServiceViewModel =    new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceViewModel.class);
        shippingAgentServiceViewModel.deleteAll();
        return true;
    }

    public static void pGetShippingAgentServicesViaWebservice(Boolean pvRefreshBln) {


        if (pvRefreshBln) {
            cShippingAgentService.allShippingAgentServicesObl = null;
            cShippingAgentService.pTruncateTableBln();
        }

        if (cShippingAgentService.allShippingAgentServicesObl != null) {
            return;
        }

        cWebresult WebResult = new cWebresult();

        cShippingAgentServiceViewModel shippingAgentServiceViewModel =    new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceViewModel.class);
        try {
            WebResult =  shippingAgentServiceViewModel.pGetShippingAgentServicesFromWebserviceWrs();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cShippingAgentService shippingAgentService = new cShippingAgentService(jsonObject);
                shippingAgentService.pInsertInDatabaseBln();
            }
            cShippingAgentService.shippingAgentServicesAvailableBln = true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICES);
        }
    }

    public cShippingAgentServiceShippingUnit pGetShippingAgentShippingUnitByStr(String pvScannedBarcodeStr){


        if(this.shippingUnitsObl() == null || this.shippingUnitsObl().size() == 0){
            return null;
        }

        for (cShippingAgentServiceShippingUnit  shippingAgentServiceShippingUnit : this.shippingUnitsObl())
        {
            if(shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(pvScannedBarcodeStr)){
                return  shippingAgentServiceShippingUnit;
            }
        }
        return null;
    }

    //End Region Public Methods
}
