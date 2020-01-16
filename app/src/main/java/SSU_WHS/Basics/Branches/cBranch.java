package SSU_WHS.Basics.Branches;

import androidx.lifecycle.ViewModelProviders;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cBranch {

    //Region Public Properties
    private String branchStr;
    public String getBranchStr() {
        return branchStr;
    }

    private String branchTypeStr;
    public String getBranchTypeStr() {
        return branchTypeStr;
    }

    private String branchNameStr;
    public String getBranchNameStr() {
        return branchNameStr;
    }

    private boolean binMandatoryBln;
    public boolean getBinMandatoryBln() {
        return binMandatoryBln;
    }

    private String pickDefaultRejectReasonStr;
    public String getPickDefaultRejectReasonStr() {
        return pickDefaultRejectReasonStr;
    }

    private String pickDefaultStorageBinStr;
    public String getPickDefaultStorageBinStr() {
        return pickDefaultStorageBinStr;
    }

    private String receiveDefaultBinStr;
    public String getReceiveDefaultBinStr() {
        return receiveDefaultBinStr;
    }

    private String returnDefaultBinStr;
    public String getReturnDefaultBinStr() {
        return returnDefaultBinStr;
    }

    private String moveDefaultBinStr;
    public String getMoveDefaultBinStr() {
        return moveDefaultBinStr;
    }

    private String shipDefaultBinStr;
    public String getShipDefaultBinStr() {
        return shipDefaultBinStr;
    }

    private List<String> errorMessagesObl;
    public List<String> getErrorMessagesObl() {
        return errorMessagesObl;
    }

    private cBranchEntity branchEntity;

    public ArrayList<cWorkplace>  workplacesObl() {
        return  cWorkplace.allWorkplacesObl;

    }
    private ArrayList<cBranchBin>  binsObl;

    private static cBranchViewModel gBranchViewModel;
    public static cBranchViewModel getBranchViewModel() {
        if (gBranchViewModel == null) {
            gBranchViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cBranchViewModel.class);
        }
        return gBranchViewModel;
    }

    private static cBranchAdapter gBranchAdapter;
    public static cBranchAdapter getBranchAdapter() {
        if (gBranchAdapter == null) {
            gBranchAdapter = new cBranchAdapter();
        }
        return gBranchAdapter;
    }

    private static List<cBranch> allBranchesObl;

    public  static  boolean BranchesAvailableBln;

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

    public static boolean pGetBranchesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cBranch.allBranchesObl = null;
            cBranch.pTruncateTableBln();
        }

        if (cBranch.allBranchesObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cBranch.getBranchViewModel().pGetBranchesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cBranch branch = new cBranch(jsonObject);
                branch.pInsertInDatabaseBln();
            }

            cBranch.BranchesAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBRANCHES);
            return  false;
        }
    }

    public boolean pInsertInDatabaseBln() {
        cBranch.getBranchViewModel().insert(this.branchEntity);

        if (cBranch.allBranchesObl == null){
            cBranch.allBranchesObl = new ArrayList<>();
        }
        cBranch.allBranchesObl .add(this);

        return true;
    }

    public static cBranch pGetBranchByCode(String pvBranchStr){
        if(cBranch.allBranchesObl == null || cBranch.allBranchesObl.size() == 0){
            return null;
        }

        for (cBranch branch :  cBranch.allBranchesObl )
        {
            if(branch.branchStr.equalsIgnoreCase(pvBranchStr)){
                return  branch;
            }
        }

        return null;
    }

    public static cBranch pGetUserBranchByCode(String pvBranchStr){
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

        return   this.mGetBinViaWebservice(pvBinCodeStr);
    }

    public static boolean pTruncateTableBln(){
        cBranch.getBranchViewModel().deleteAll();
        return true;
    }

    public boolean pGetWorkplacesBln(boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cWorkplace.allWorkplacesObl = null;
        }

        if (this.workplacesObl() != null){
            return  true;
        }

        cWorkplace.pTruncateTableBln();
        cWorkplace.allWorkplacesObl = null;

        cWebresult WebResult;
        WebResult =  cWorkplace.getWorkplaceViewModel().pGetWorkplacesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

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

    //Region Private Methods
    private cBranchBin mGetBinViaWebservice(String pvBinCodeStr) {

        cWebresult WebResult;
        WebResult =  cBranch.getBranchViewModel().pGetBinFromWebserviceWrs(pvBinCodeStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            if (WebResult.getResultDtt() != null && WebResult.getResultDtt().size() == 1) {
                cBranchBin branchBin = new cBranchBin(WebResult.getResultDtt().get(0));
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
    //End Region Private Methods
}
