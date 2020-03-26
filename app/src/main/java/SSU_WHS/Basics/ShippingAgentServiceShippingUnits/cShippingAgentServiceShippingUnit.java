package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgentServiceShippingUnit {

    //Region Public Properties

    private String ShippingAgentStr;
    public String getShippingAgentStr() {
        return ShippingAgentStr;
    }

    private String ShippingAgentServiceStr;
    public String getShippingAgentServiceStr() {
        return ShippingAgentServiceStr;
    }

    private String ShippingUnitStr;
    public String getShippingUnitStr() {
        return ShippingUnitStr;
    }

    private String DescriptionStr;
    public String getDescriptionStr() {
        return DescriptionStr;
    }

    public Integer ShippingUnitQuantityUsedInt;
    public Integer getShippingUnitQuantityUsedInt() {
        return ShippingUnitQuantityUsedInt;
    }

    private cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity;

    public static cShippingAgentServiceShippingUnit currentShippingAgentServiceShippingUnit;

    private cShippingAgentServiceShippingUnitViewModel getShippingAgentServiceShippingUnitViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceShippingUnitViewModel.class);
    }

    public static List<cShippingAgentServiceShippingUnit> allShippingAgentServiceShippingUnitsObl;

    public static String SHIPPINGUNIT_BOX = "DOOS";
    public static String SHIPPINGUNIT_PALLET = "PALLET";
    public static String SHIPPINGUNIT_CONTAINER = "CONTAINER";
    public static String SHIPPINGUNIT_HANGING = "HANGEND";
    public static String SHIPPINGUNIT_LETTERBOX = "BP";

    public  static  Boolean shippingAgentServiceShippingUnitsAvailableBln;

    //End Region Public Properties


    //Region Constructor
    private cShippingAgentServiceShippingUnit(JSONObject pvJsonObject) {
        this.shippingAgentServiceShippingUnitEntity = new cShippingAgentServiceShippingUnitEntity(pvJsonObject);
        this.ShippingAgentStr = this.shippingAgentServiceShippingUnitEntity.getShippingAgentStr()  ;
        this.ShippingAgentServiceStr = this.shippingAgentServiceShippingUnitEntity.getServiceStr();
        this.ShippingUnitStr = this.shippingAgentServiceShippingUnitEntity.getShippingunitStr();
        this.DescriptionStr = this.shippingAgentServiceShippingUnitEntity.getDescriptionStr();
        this.ShippingUnitQuantityUsedInt = 0;
    }
    //End Region Constructor

    //Region Public Methods


    public boolean pInsertInDatabaseBln() {


        this.getShippingAgentServiceShippingUnitViewModel().insert(this.shippingAgentServiceShippingUnitEntity);

        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null){
            cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl= new ArrayList<>();
        }
        cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceShippingUnitViewModel.class);
        shippingAgentServiceShippingUnitViewModel.deleteAll();
        return true;
    }

    public static void pGetShippingAgentServicesShippingUnitsViaWebservice(Boolean pvRefreshBln) throws ExecutionException {

        if (pvRefreshBln) {
            cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl = null;
            cShippingAgentServiceShippingUnit.pTruncateTableBln();
        }

        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl  != null) {
            return;
        }

        cWebresult WebResult;
        cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cShippingAgentServiceShippingUnitViewModel.class);
        WebResult =  shippingAgentServiceShippingUnitViewModel.pGetShippingAgentServiceShippingUnitsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit = new cShippingAgentServiceShippingUnit(jsonObject);
                shippingAgentServiceShippingUnit.pInsertInDatabaseBln();
            }
            cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln = true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHPPINGUNITS);
        }
    }


    //End Region Public Methods
}
