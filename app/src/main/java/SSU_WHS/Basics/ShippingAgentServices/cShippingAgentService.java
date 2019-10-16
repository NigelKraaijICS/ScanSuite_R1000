package SSU_WHS.Basics.ShippingAgentServices;

import androidx.lifecycle.ViewModelProviders;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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

    public String serviceStr;
    public String getServiceStr() {
        return serviceStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }
    public String serviceCountriesStr;
    public String getServiceCountriesStr() {
        return serviceCountriesStr;
    }

    public cShippingAgentServiceEntity shippingAgentServiceEntity;
    public boolean indatabaseBln;

    public static cShippingAgentServiceViewModel gShippingAgentServiceViewModel;

    public static cShippingAgentServiceViewModel getShippingAgentServiceViewModel() {
        if (gShippingAgentServiceViewModel == null) {
            gShippingAgentServiceViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cShippingAgentServiceViewModel.class);
        }
        return gShippingAgentServiceViewModel;
    }

    public static List<cShippingAgentService> allShippingAgentServicesObl;
    public  static  Boolean shippingAgentServicesAvailableBln;


    public  List<cShippingAgentServiceShippingUnit>  shippingUnitsObl () {

        List<cShippingAgentServiceShippingUnit> resultObl = new ArrayList<>();


        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null || cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.size() == 0) {
            resultObl = null;
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
    cShippingAgentService(JSONObject pvJsonObject) {
        this.shippingAgentServiceEntity = new cShippingAgentServiceEntity(pvJsonObject);
        this.shippingagentStr = this.shippingAgentServiceEntity.getShippingagentStr();
        this.serviceStr = this.shippingAgentServiceEntity.getServiceStr();
        this.descriptionStr = this.shippingAgentServiceEntity.getDescriptionStr();
        this.serviceCountriesStr = this.shippingAgentServiceEntity.getServiceCountriesStr();
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {

        cShippingAgentService.getShippingAgentServiceViewModel().insert(this.shippingAgentServiceEntity);
        this.indatabaseBln = true;

        if (cShippingAgentService.allShippingAgentServicesObl == null){
            cShippingAgentService.allShippingAgentServicesObl= new ArrayList<>();
        }
        cShippingAgentService.allShippingAgentServicesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cShippingAgentService.getShippingAgentServiceViewModel().deleteAll();
        return true;
    }

    public static boolean pGetShippingAgentServicesViaWebserviceBln(Boolean pvRefreshBln) {


        if (pvRefreshBln == true) {
            cShippingAgentService.allShippingAgentServicesObl = null;
            cShippingAgentService.pTruncateTableBln();
        }

        if (cShippingAgentService.allShippingAgentServicesObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cShippingAgentService.getShippingAgentServiceViewModel().pGetShippingAgentServicesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cShippingAgentService shippingAgentService = new cShippingAgentService(jsonObject);
                shippingAgentService.pInsertInDatabaseBln();
            }
            cShippingAgentService.shippingAgentServicesAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICES);
            return  false;
        }
    }

    public cShippingAgentServiceShippingUnit pGetShippingAgentShippingUnitByStr(String pvScannedBarcodeStr){

        cShippingAgentServiceShippingUnit resultUnit;

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
