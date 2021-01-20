package SSU_WHS.Basics.Branches;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.Basics.Workplaces.cWorkplaceViewModel;
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
    public  boolean isBinMandatoryBln() {return  binMandatoryBln;}

    private String returnDefaultBinStr;
    public String getReturnDefaultBinStr(){
        return   returnDefaultBinStr;
    }

    private String receiveDefaultBinStr;
    public String getReceiveDefaultBinStr(){
        return   receiveDefaultBinStr;
    }

    private String pickDefaultStorageBinStr;
    public String getPickDefaultStorageBinStr(){
        return pickDefaultStorageBinStr;
    }

    private String moveDefaultBinStr;
    public String getMoveDefaultBinStr(){
        return   moveDefaultBinStr;
    }

    private cBranchEntity branchEntity;

    public ArrayList<cWorkplace>  workplacesObl() {
        return  cWorkplace.allWorkplacesObl;

    }
    public ArrayList<cBranchBin>  binsObl;
    public ArrayList<cBranchReason>  returnReasonObl;


    private cBranchViewModel getBranchViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cBranchViewModel.class);
    }

    private static List<cBranch> allBranchesObl;
    public  List<cBranchBin> receiveBinsObl;

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
        this.returnDefaultBinStr = branchEntity.getReturndefaultbinStr();
        this.receiveDefaultBinStr = branchEntity.getReceivedefaultbinStr();
        this.moveDefaultBinStr = branchEntity.getMovedefaultbinStr();
        this.pickDefaultStorageBinStr = branchEntity.getPickDefaultStorageBinStr();
        this.binMandatoryBln = cText.pStringToBooleanBln(branchEntity.getBinmandatoryStr(),false) ;

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

        cBranchViewModel branchViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cBranchViewModel.class);
        WebResult =  branchViewModel.pGetBranchesFromWebserviceWrs();
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


    public boolean pGetReceiveBinsViaWebserviceBln() {

        if (this.receiveBinsObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  this.getBranchViewModel().pGetReceiveBinsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            this.receiveBinsObl = new ArrayList<>();
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cBranchBin branchBin = new cBranchBin(jsonObject);
                this.receiveBinsObl.add(branchBin);
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBRANCHES);
            return  false;
        }
    }


    public boolean pInsertInDatabaseBln() {
        this.getBranchViewModel().insert(this.branchEntity);

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

        cBranchViewModel branchViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cBranchViewModel.class);
        branchViewModel.deleteAll();
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

        cWorkplaceViewModel  workplaceViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cWorkplaceViewModel.class);
        WebResult =  workplaceViewModel.pGetWorkplacesFromWebserviceWrs();

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

    public boolean pGetReasonBln(Boolean pvRefreshBln) {

        if (pvRefreshBln){
            this.returnReasonObl = null;
            this.returnReasonObl = new ArrayList<>();
        }

        cWebresult WebResult;
        WebResult =  this.getBranchViewModel().pGetReasonFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            if (this.returnReasonObl == null){
                this.returnReasonObl = new ArrayList<>();
            }


            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                cBranchReason branchReason = new cBranchReason(jsonObject);
                if(branchReason.isReturn()) {
                    this.returnReasonObl.add(branchReason);
                }
            }
            return  true;

        }
        return false;
    }

    public cBranchReason pGetReasonByName(String pvReasonStr){
        if(cUser.currentUser.currentBranch.returnReasonObl == null){
            return null;
        }

        for (cBranchReason branchReason : cUser.currentUser.currentBranch.returnReasonObl)
        {
            if(branchReason.getReasonStr().equalsIgnoreCase(pvReasonStr)){
                return  branchReason;
            }
        }
        return null;
    }

    //End Region Public Methods

    //Region Private Methods
    private cBranchBin mGetBinViaWebservice(String pvBinCodeStr) {

        cWebresult WebResult;
        WebResult =  this.getBranchViewModel().pGetBinFromWebserviceWrs(pvBinCodeStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            if (WebResult.getResultDtt() != null && WebResult.getResultDtt().size() == 1) {
                cBranchBin branchBin = new cBranchBin(WebResult.getResultDtt().get(0));
                if (this.binsObl == null) {
                    this.binsObl = new ArrayList<>();
                }

                this.binsObl.add(branchBin);
                return  branchBin;
            }

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSELOCATIONS);
        }
        return  null;
    }
    //End Region Private Methods
}
