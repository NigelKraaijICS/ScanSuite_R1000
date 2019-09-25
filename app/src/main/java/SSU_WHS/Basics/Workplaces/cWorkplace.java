package SSU_WHS.Basics.Workplaces;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cWorkplace {

    //region Public Properties
    public String workplaceStr;
    public String getWorkplaceStr() {
        return workplaceStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessageObl() {
        return errorMessagesObl;
    }

    public cWorkplaceEntity workplaceEntity;
    public boolean inDatabaseBln;


    public static ArrayList<cWorkplace> allWorkplacesObl;
    public  static cWorkplace currentWorkplace;

    public static cWorkplaceViewModel gWorkplaceViewModel;
    public static cWorkplaceViewModel getWorkplaceViewModel() {
        if (gWorkplaceViewModel == null) {
            gWorkplaceViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cWorkplaceViewModel.class);
        }
        return gWorkplaceViewModel;
    }

    public static cWorkplaceAdapter gWorkplaceAdapter;
    public static cWorkplaceAdapter getWorkplaceAdapter() {
        if (gWorkplaceAdapter == null) {
            gWorkplaceAdapter = new cWorkplaceAdapter();
        }
        return gWorkplaceAdapter;
    }

    //end region Public Propties

     //Region Constructor
     public cWorkplace(JSONObject pvJsonObject) {
        this.workplaceEntity = new cWorkplaceEntity(pvJsonObject);
        this.workplaceStr = workplaceEntity.getWorkplaceStr();
        this.descriptionStr = workplaceEntity.getDescriptionStr();
    }
    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        cWorkplace.getWorkplaceViewModel().insert(this.workplaceEntity);
        this.inDatabaseBln = true;

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
        {
            if(workplace.workplaceStr.equalsIgnoreCase(pvWorkplace) == true ){
                return  workplace;
            }
        }
        return null;
    }

    public static boolean pTruncateTableBln(){
        cWorkplace.getWorkplaceViewModel().deleteAll();
        return true;
            }

    public static boolean pGetWorkplacesViaWebserviceBln() {

        cWorkplace.allWorkplacesObl = null;
        cWorkplace.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  cWorkplace.getWorkplaceViewModel().pGetWorkplacesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


           List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

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
