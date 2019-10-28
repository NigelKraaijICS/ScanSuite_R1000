package SSU_WHS.Basics.Users;

import androidx.lifecycle.ViewModelProviders;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.Settings.cSetting;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cUser {

    //region Public Properties
    public String usernameStr;
    public String getUsernameStr() {
        return usernameStr;
    }

    public String nameStr;
    public String getNameStr() {
        return nameStr;
    }

    public Integer importfileInt;
    public Integer getImportfileInt() {
        return importfileInt;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessageObl() {
        return errorMessagesObl;
    }

    public cUserEntity userEntity;
    public ArrayList<cBranch> branchesObl;
    public ArrayList<cAuthorisation> autorisationObl;
    public  cBranch currentBranch;
    public  cAuthorisation currentAuthorisation;
    public boolean loggedInBln;
    public boolean indatabaseBln;

    public static cUserViewModel gUserViewModel;

    public static cUserViewModel getUserViewModel() {
        if (gUserViewModel == null) {
            gUserViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cUserViewModel.class);
        }
        return gUserViewModel;
    }

    public static cUserAdapter gUserAdapter;

    public static cUserAdapter getUserAdapter() {
        if (gUserAdapter == null) {
            gUserAdapter = new cUserAdapter();
        }
        return gUserAdapter;
    }

    public static List<cUser> allUsersObl;
    public static cUser currentUser;
    public  static Boolean usersAvailableBln;

    //end region Public Properties

     //Region Constructor
    cUser(JSONObject pvJsonObject) {
        this.userEntity = new cUserEntity(pvJsonObject);
        this.usernameStr = userEntity.getUsernameStr();
        this.nameStr = userEntity.getNameStr();
        this.importfileInt = cText.pStringToIntegerInt(userEntity.getImportfileStr());
    }
    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        cUser.getUserViewModel().insert(this.userEntity);
        this.indatabaseBln = true;

        if (cUser.allUsersObl == null){
            cUser.allUsersObl = new ArrayList<>();
        }
        cUser.allUsersObl.add(this);
        return  true;
    }

    public boolean pLoginBln(String pvPassword) {
        cWebresult WebResult;
        WebResult =  cUser.getUserViewModel().pUserLoginViaWebserviceWrs(pvPassword);

        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            this.loggedInBln = true;
            return  true;
        }
        else {
            if  (WebResult.getSuccessBln() == false ){
                this.errorMessagesObl = WebResult.getResultObl();
                this.loggedInBln = false;
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_USERLOGIN);
                return  false;
            }

            if  (WebResult.getResultBln() == false ){
                this.loggedInBln = false;
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_USERLOGIN);
                return  false;
            }
            return false;
        }
                }


    public boolean pGetBranchesBln() {

        if (this.branchesObl != null){
            return  true;
        }

        cBranch.pTruncateTableBln();
        this.branchesObl = null;

        cWebresult WebResult;
        WebResult =  cBranch.getBranchViewModel().pGetBranchesFromWebserviceWrs();

        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cBranch branch = new cBranch(jsonObject);
                branch.pInsertInDatabaseBln();
                if(this.branchesObl == null) {
                    this.branchesObl =  new ArrayList<>();
                }

                this.branchesObl.add((branch));
            }

            return  true;

        }
        else {


            if  (WebResult.getSuccessBln() == false ){
                this.errorMessagesObl = WebResult.getResultObl();
                this.loggedInBln = false;
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER);
                return  false;
            }

            if  (WebResult.getResultBln() == false ){
                this.loggedInBln = false;
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER);
                return  false;
            }
                   }
        return false;
    }

    public boolean pGetAutorisationsBln() {

        if (this.autorisationObl != null){
            return  true;
        }

        cAuthorisation.pTruncateTableBln();
        this.autorisationObl = null;

        cWebresult WebResult;
        WebResult =  cAuthorisation.getAutorisationViewModel().pGetAutorisationsFromWebserviceWrs();

        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cAuthorisation authorisation = new cAuthorisation(jsonObject);

//                if (authorisation.getAutorisationEnu() != cAuthorisation.AutorisationEnu.PICK) {
//                    continue;
//                }

                authorisation.pInsertInDatabaseBln();
                if(this.autorisationObl == null) {
                    this.autorisationObl =  new ArrayList<>();
                }

                this.autorisationObl.add((authorisation));

                if (authorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK) {
                    if (cSetting.PICK_SORT_FASE_AVAILABLE()) {
                        cAuthorisation authorisationSorting = new cAuthorisation(cAuthorisation.AutorisationEnu.SORTING.toString(), this.autorisationObl.size() *10 + 10 );
                        this.autorisationObl.add(authorisationSorting);
                    }

                    if (cSetting.PICK_PACK_AND_SHIP_FASE_AVAILABLE()) {
                        cAuthorisation authorisationShipping = new cAuthorisation(cAuthorisation.AutorisationEnu.SHIPPING.toString(), this.autorisationObl.size() *10 + 10 );
                        this.autorisationObl.add(authorisationShipping);
                    }
                }

            }


            return  true;

        }
        else {


            if  (WebResult.getSuccessBln() == false ){
                this.errorMessagesObl = WebResult.getResultObl();
                this.loggedInBln = false;
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETAUTHORISATIONSFORUSERINLOCATION);
                return  false;
            }

            if  (WebResult.getResultBln() == false ){
                this.loggedInBln = false;
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETAUTHORISATIONSFORUSERINLOCATION);
                return  false;
            }
        }
        return false;
    }

    public static cUser pGetUserByName(String pvUserName){
        if(cUser.allUsersObl == null){
            return null;
        }

        for (cUser user : cUser.allUsersObl)
        {
            if(user.usernameStr.equalsIgnoreCase(pvUserName)){
                return  user;
            }
        }

        return null;
    }

    public static boolean pTruncateTableBln(){
        cUser.getUserViewModel().deleteAll();
        return true;
            }

    public static boolean pGetUsersViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cUser.allUsersObl = null;
            cUser.pTruncateTableBln();
        }

        if ( cUser.allUsersObl  != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cUser.getUserViewModel().pGetUsersFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cUser User = new cUser(jsonObject);
                User.pInsertInDatabaseBln();
            }
            cUser.usersAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETUSERS);
            return  false;
        }
    }
    //End Region Public Methods
}
