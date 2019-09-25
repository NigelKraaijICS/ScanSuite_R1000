package SSU_WHS.Basics.Branches;

import androidx.lifecycle.ViewModelProviders;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Webservice.cWebresult;

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
        this.binMandatoryBln = cText.stringToBoolean(branchEntity.getBinmandatoryStr(),true);
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

    public static cBranch pGetBranchByCode(String pvBranch){
        if(cUser.currentUser == null || cUser.currentUser.branchesObl == null){
            return null;
        }

        for (cBranch branch :  cUser.currentUser.branchesObl)
        {
            if(branch.branchStr.equalsIgnoreCase(pvBranch)){
                return  branch;
            }
        }

        return null;
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

    //End Region Public Methods
}
