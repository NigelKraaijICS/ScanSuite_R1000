package SSU_WHS.Basics.Workplaces;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cWorkplace {

    //region Public Properties
    private final String workplaceStr;
    public String getWorkplaceStr() {
        return workplaceStr;
    }

    private final String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private final cWorkplaceEntity workplaceEntity;


    public static ArrayList<cWorkplace> allWorkplacesObl;
    public  static cWorkplace currentWorkplace;

    private cWorkplaceViewModel getWorkplaceViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWorkplaceViewModel.class);
    }

    //end region Public Propties

     //Region Constructor
     public cWorkplace(JSONObject pvJsonObject) {
        this.workplaceEntity = new cWorkplaceEntity(pvJsonObject);
        this.workplaceStr = workplaceEntity.getWorkplaceStr();
        this.descriptionStr = workplaceEntity.getDescriptionStr();
    }

    public cWorkplace(String pvWorkplaceStr, String pvDescriptionStr) {
        this.workplaceEntity = null;
        this.workplaceStr = pvWorkplaceStr;
        this.descriptionStr = pvDescriptionStr;
    }

    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
         this.getWorkplaceViewModel().insert(this.workplaceEntity);

        if (cWorkplace.allWorkplacesObl == null){
            cWorkplace.allWorkplacesObl = new ArrayList<>();
        }
        cWorkplace.allWorkplacesObl.add(this);
        return true;
    }

    public static cWorkplace pGetWorkplaceByName(String pvWorkplace){
        if(cWorkplace.allWorkplacesObl == null){
            return null;
        }

        for (cWorkplace workplace : cWorkplace.allWorkplacesObl)
            if (workplace.workplaceStr.equalsIgnoreCase(pvWorkplace)) {
                return workplace;
            }
        return null;
    }

    public static boolean pTruncateTableBln(){

        cWorkplaceViewModel workplaceViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cWorkplaceViewModel.class);
        workplaceViewModel.deleteAll();
        return true;
    }

    public static boolean pGetWorkplacesViaWebserviceBln() {

        cWorkplace.allWorkplacesObl = null;
        cWorkplace.pTruncateTableBln();

        cWebresult WebResult;

        cWorkplaceViewModel workplaceViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cWorkplaceViewModel.class);

        WebResult =  workplaceViewModel.pGetWorkplacesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject :  WebResult.getResultDtt()) {
                cWorkplace Workplace = new cWorkplace(jsonObject);
                Workplace.pInsertInDatabaseBln();
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETWORKPLACES);
            return  false;
        }
    }

    //End Region Public Methods
}
