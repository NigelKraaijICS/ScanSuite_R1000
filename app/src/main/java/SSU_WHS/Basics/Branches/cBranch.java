package SSU_WHS.Basics.Branches;

import androidx.lifecycle.ViewModelProviders;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cBranch {

    //Region Public Properties
    public String branchStr;
    public String getBranchStr() {
        return branchStr;
    }

    public String branchTypeStr;
    public String getBranchTypeStr() {
        return branchTypeStr;
    }

    public String branchNameStr;
    public String getBranchNameStr() {
        return branchNameStr;
    }

    public boolean binMandatoryBln;
    public boolean getBinMandatoryBln() {
        return binMandatoryBln;
    }

    public String pickDefaultRejectReasonStr;
    public String getPickDefaultRejectReasonStr() {
        return pickDefaultRejectReasonStr;
    }

    public String pickDefaultStorageBinStr;
    public String getPickDefaultStorageBinStr() {
        return pickDefaultStorageBinStr;
    }

    public String receiveDefaultBinStr;
    public String getReceiveDefaultBinStr() {
        return receiveDefaultBinStr;
    }

    public String returnDefaultBinStr;
    public String getReturnDefaultBinStr() {
        return returnDefaultBinStr;
    }

    public String moveDefaultBinStr;
    public String getMoveDefaultBinStr() {
        return moveDefaultBinStr;
    }

    public String shipDefaultBinStr;
    public String getShipDefaultBinStr() {
        return shipDefaultBinStr;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessagesObl() {
        return errorMessagesObl;
    }

    public cBranchEntity branchEntity;
    public boolean inDatabaseBln;
    public ArrayList<cWorkplace>  workplacesObl() {
        return  cWorkplace.allWorkplacesObl;

    }
    public ArrayList<cBranchBin>  binsObl;

    public static cBranchViewModel gBranchViewModel;
    public static cBranchViewModel getBranchViewModel() {
        if (gBranchViewModel == null) {
            gBranchViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cBranchViewModel.class);
        }
        return gBranchViewModel;
    }

    public static cBranchAdapter gBranchAdapter;
    public static cBranchAdapter getBranchAdapter() {
        if (gBranchAdapter == null) {
            gBranchAdapter = new cBranchAdapter();
        }
        return gBranchAdapter;
    }

       public enum brachTypeEnum {
        INTRANSIT,
        STORE,
        UNKNOWN,
        WAREHOUSE
    }
    //end region Public Propties

    //Region Constructor
    public cBranch(JSONObject pvJsonObject) {
        this.branchEntity = new cBranchEntity(pvJsonObject);
        this.branchStr = branchEntity.getBranchStr();
        this.branchTypeStr = branchEntity.getBranchtypeStr();
        this.branchNameStr = branchEntity.getBranchnameStr();
        this.binMandatoryBln = cText.pStringToBooleanBln(branchEntity.getBinmandatoryStr(),true);
        this.pickDefaultRejectReasonStr = branchEntity.getPickdefaultrejectreasonStr();
        this.pickDefaultStorageBinStr = branchEntity.getPickdefaultstoragebinStr();
        this.receiveDefaultBinStr = branchEntity.getReceivedefaultbinStr();
        this.returnDefaultBinStr = branchEntity.getReturndefaultbinStr();
        this.moveDefaultBinStr = branchEntity.getMovedefaultbinStr();
        this.shipDefaultBinStr = branchEntity.getShipdefaultbinStr();
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cBranch.getBranchViewModel().insert(this.branchEntity);
        this.inDatabaseBln = true;
        return true;
    }

    public static cBranch pGetBranchByCode(String pvBranchStr){
        if(cUser.currentUser == null || cUser.currentUser.branchesObl == null){
            return null;
        }

        for (cBranch branch :  cUser.currentUser.branchesObl)
        {
            if(branch.branchStr.equalsIgnoreCase(pvBranchStr)){
                return  branch;
            }
        }

        return null;
    }

    public cBranchBin pGetBinByCode(String pvBinCodeStr){

        //Search for the BIN in the cache
        if (this.binsObl != null && this.binsObl.size() > 0 ) {
            for (cBranchBin branchBin : this.binsObl) {

                if (branchBin.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr)) {
                    return  branchBin;
                }
            }
        }

        cBranchBin branchBin = this.pGetBinViaWebservice(pvBinCodeStr);
        return  branchBin;
    }

    public static boolean pTruncateTableBln(){
        cBranch.getBranchViewModel().deleteAll();
        return true;
    }

    public boolean pGetWorkplacesBln() {

        if (this.workplacesObl() != null){
            return  true;
        }

        cWorkplace.pTruncateTableBln();
        cWorkplace.allWorkplacesObl = null;

        cWebresult WebResult;
        WebResult =  cWorkplace.getWorkplaceViewModel().pGetWorkplacesFromWebserviceWrs();

        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            if( cWorkplace.allWorkplacesObl == null) {
                cWorkplace.allWorkplacesObl =  new ArrayList<>();
            }

            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cWorkplace workplace = new cWorkplace(jsonObject);
                workplace.pInsertInDatabaseBln();
            }

            return  true;

        }
        return false;
    }

    public cBranchBin pGetBinViaWebservice(String pvBinCodeStr) {

      cWebresult WebResult;
        WebResult =  cBranch.getBranchViewModel().pGetBinFromWebserviceWrs(pvBinCodeStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cBranchBin branchBin = new cBranchBin(jsonObject);

                if (this.binsObl == null) {
                    this.binsObl = new ArrayList<>();
                }

                this.binsObl.add(branchBin);
                return  branchBin;
            }
            return  null;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSELOCATIONS);
            return  null;
        }
    }

    //End Region Public Methods
}
