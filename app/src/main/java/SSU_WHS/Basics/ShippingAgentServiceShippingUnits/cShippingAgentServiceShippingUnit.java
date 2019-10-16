package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cShippingAgentServiceShippingUnit {

    //Region Public Properties

    public String ShippingAgentStr;
    public String getShippingAgentStr() {
        return ShippingAgentStr;
    }

    public String ShippingAgentServiceStr;
    public String getShippingAgentServiceStr() {
        return ShippingAgentServiceStr;
    }

    public String ShippingUnitStr;
    public String getShippingUnitStr() {
        return ShippingUnitStr;
    }

    public String DescriptionStr;
    public String getDescriptionStr() {
        return DescriptionStr;
    }

    public double DefaultWeightInGramDbl;
    public double getDefaultWeightInGramDbl() {
        return DefaultWeightInGramDbl;
    }

    public String ContainerTypeStr;
    public String getContainerTypeStr() {
        return ContainerTypeStr;
    }

    public Integer ShippingUnitQuantityUsedInt;
    public Integer getShippingUnitQuantityUsedInt() {
        return ShippingUnitQuantityUsedInt;
    }

    public cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity;
    public boolean indatabaseBln;

    public static cShippingAgentServiceShippingUnit currentShippingAgentServiceShippingUnit;

    public static cShippingAgentServiceShippingUnitViewModel gShippingAgentServiceShippingUnitViewModel;
    public static cShippingAgentServiceShippingUnitViewModel getShippingAgentServiceShippingUnitViewModel() {
        if (gShippingAgentServiceShippingUnitViewModel == null) {
            gShippingAgentServiceShippingUnitViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cShippingAgentServiceShippingUnitViewModel.class);
        }
        return gShippingAgentServiceShippingUnitViewModel;
    }

    public static cShippingAgentServiceShippingUnitAdapter gShippingAgentServiceShippingUnitAdapter;
    public static cShippingAgentServiceShippingUnitAdapter getShippingAgentServiceShippingUnitAdapter() {
        if (gShippingAgentServiceShippingUnitAdapter == null) {
            gShippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter();
        }
        return gShippingAgentServiceShippingUnitAdapter;
    }

    public static List<cShippingAgentServiceShippingUnit> allShippingAgentServiceShippingUnitsObl;

    public enum shippingUnitsEnu {
        DOOS,
        PALLET,
        CONTAINER,
        HANGEND,
        BP
    }

    public static String SHIPPINGUNIT_BOX = "DOOS";
    public static String SHIPPINGUNIT_PALLET = "PALLET";
    public static String SHIPPINGUNIT_CONTAINER = "CONTAINER";
    public static String SHIPPINGUNIT_HANGING = "HANGEND";
    public static String SHIPPINGUNIT_LETTERBOX = "BP";

    public  static  Boolean shippingAgentServiceShippingUnitsAvailableBln;

    //End Region Public Properties


    //Region Constructor
    cShippingAgentServiceShippingUnit(JSONObject pvJsonObject) {
        this.shippingAgentServiceShippingUnitEntity = new cShippingAgentServiceShippingUnitEntity(pvJsonObject);
        this.ShippingAgentStr = this.shippingAgentServiceShippingUnitEntity.getShippingAgentStr()  ;
        this.ShippingAgentServiceStr = this.shippingAgentServiceShippingUnitEntity.getServiceStr();
        this.ShippingUnitStr = this.shippingAgentServiceShippingUnitEntity.getShippingunitStr();
        this.DescriptionStr = this.shippingAgentServiceShippingUnitEntity.getDescriptionStr();
        this.DefaultWeightInGramDbl = cText.stringToDouble(this.shippingAgentServiceShippingUnitEntity.getDefaultWeightInGramStr());
        this.ContainerTypeStr = this.shippingAgentServiceShippingUnitEntity.getContainertype();
        this.ShippingUnitQuantityUsedInt = 0;
    }
    //End Region Constructor

    //Region Public Methods


    public boolean pInsertInDatabaseBln() {


        cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitViewModel().insert(this.shippingAgentServiceShippingUnitEntity);
        this.indatabaseBln = true;

        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null){
            cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl= new ArrayList<>();
        }
        cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitViewModel().deleteAll();
        return true;
    }

    public static boolean pGetShippingAgentServicesShippingUnitsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl = null;
            cShippingAgentServiceShippingUnit.pTruncateTableBln();
        }

        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl  != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitViewModel().pGetShippingAgentServiceShippingUnitsFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit = new cShippingAgentServiceShippingUnit(jsonObject);
                shippingAgentServiceShippingUnit.pInsertInDatabaseBln();
            }
            cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHPPINGUNITS);
            return  false;
        }
    }

    public static cShippingAgentServiceShippingUnit pGetShippingAgentServiceShippingUnitByStr(String pvShippingAgentStr, String pvShippingAgentServiceStr, String pvShippingUnitStr){
        if(cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null){
            return null;
        }

        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl)
        {
            if(shippingAgentServiceShippingUnit.getShippingAgentStr() == pvShippingAgentStr &&
               shippingAgentServiceShippingUnit.getShippingAgentServiceStr() == pvShippingAgentServiceStr &&
               shippingAgentServiceShippingUnit.getShippingUnitStr() == pvShippingUnitStr) {

                return  shippingAgentServiceShippingUnit;
            }
        }
        return null;
    }


    //End Region Public Methods
}
