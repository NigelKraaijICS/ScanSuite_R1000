package SSU_WHS.Basics.Users;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cProductFlavor;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.Authorisations.cAuthorisationViewModel;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.Branches.cBranchViewModel;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.BuildConfig;

public class cUser {

    //region Public Properties
    private String usernameStr;
    public String getUsernameStr() {
        return usernameStr;
    }

    private String nameStr;
    public String getNameStr() {
        return nameStr;
    }

    public String getInitialsStr() {

        String firstInitialStr;
        String lastInitialStr;
        String resultStr;

        if (this.getNameStr() == null || this.getNameStr().isEmpty()) {
            return "??";
        }
        String[] elementArray = this.getNameStr().trim().split(" ");

        ArrayList<String> elementObl = new ArrayList<>();
        for (String s: elementArray) {
            String c = s.replaceAll("\\s", "");
            if (!c.isEmpty()) {
                elementObl.add(c);
            }
        }

        if (elementObl.size() == 1) {
            firstInitialStr = elementObl.get(0).substring(0,1);
            lastInitialStr = elementObl.get(0).substring(elementObl.get(0).length() -1);
            resultStr = firstInitialStr + lastInitialStr;
        } else {
            firstInitialStr = elementObl.get(0).substring(0,1);
            lastInitialStr= elementObl.get(elementObl.size() -1).substring(0,1);
            resultStr= firstInitialStr + lastInitialStr;
        }

        return resultStr.toUpperCase();

    }

    private cUserEntity userEntity;
    public ArrayList<cBranch> branchesObl;
    public ArrayList<cAuthorisation> autorisationObl;
    public  cBranch currentBranch;
    public  cAuthorisation currentAuthorisation;

    private cUserViewModel getUserViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cUserViewModel.class);
    }

    public static List<cUser> allUsersObl;
    public static cUser currentUser;
    public  static Boolean usersAvailableBln;

    //end region Public Properties

     //Region Constructor
     private cUser(JSONObject pvJsonObject) {
        this.userEntity = new cUserEntity(pvJsonObject);
        this.usernameStr = userEntity.getUsernameStr();
        this.nameStr = userEntity.getNameStr();
    }
    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getUserViewModel().insert(this.userEntity);

        if (cUser.allUsersObl == null){
            cUser.allUsersObl = new ArrayList<>();
        }
        cUser.allUsersObl.add(this);
        return  true;
    }

    public boolean pLoginBln(String pvPassword) {
        cWebresult WebResult;
        WebResult =  this.getUserViewModel().pUserLoginViaWebserviceWrs(pvPassword);

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return  true;
        }
        else {
            if  (!WebResult.getSuccessBln()){
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_USERLOGIN);
                return  false;
            }

            if  (!WebResult.getResultBln()){
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

        cBranchViewModel branchViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cBranchViewModel.class);
        WebResult =  branchViewModel.pGetUserBranchesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cBranch branch = new cBranch(jsonObject);
                if(this.branchesObl == null) {
                    this.branchesObl =  new ArrayList<>();
                }

                this.branchesObl.add((branch));
            }

            return  true;

        }
        else {
            if  (!WebResult.getSuccessBln()){
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER);
                return  false;
            }

            if  (!WebResult.getResultBln()){
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER);
                return  false;
            }
                   }
        return false;
    }

    public boolean pGetAutorisationsBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            this.autorisationObl = null;
        }

        if (this.autorisationObl != null){
            return  true;
        }

        cAuthorisation.pTruncateTableBln();
        this.autorisationObl = null;

        boolean shippingAddedBln = false;
        boolean sortingAddedBln = false;

        cWebresult WebResult;
        cAuthorisationViewModel authorisationViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cAuthorisationViewModel.class);

        WebResult =  authorisationViewModel.pGetAutorisationsFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cAuthorisation authorisation = new cAuthorisation(jsonObject);

                if(this.autorisationObl == null) {
                    this.autorisationObl =  new ArrayList<>();
                }

                if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.TCOG.toString())) {
                        if ((authorisation.getAutorisationEnu() != cAuthorisation.AutorisationEnu.PICK &&
                             authorisation.getAutorisationEnu() != cAuthorisation.AutorisationEnu.PICK_PV)) {
                            continue;
                    }
                }

                authorisation.pInsertInDatabaseBln();


                this.autorisationObl.add((authorisation));

                if (authorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK ||
                    authorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK_PV) {
                    if (cSetting.PICK_SORT_FASE_AVAILABLE()) {

                        cAuthorisation authorisationSorting = new cAuthorisation(cAuthorisation.AutorisationEnu.SORTING.toString(), this.autorisationObl.size() *10 + 10 );

                        if (!sortingAddedBln) {
                            this.autorisationObl.add(authorisationSorting);
                            sortingAddedBln = true;
                        }

                    }

                    if (cSetting.PICK_PACK_AND_SHIP_FASE_AVAILABLE()) {
                        cAuthorisation authorisationShipping = new cAuthorisation(cAuthorisation.AutorisationEnu.SHIPPING.toString(), this.autorisationObl.size() *10 + 10 );

                      if (!shippingAddedBln) {
                          this.autorisationObl.add(authorisationShipping);
                          shippingAddedBln = true;
                      }
                    }
                }

            }


            return  true;

        }
        else {


            if  (!WebResult.getSuccessBln()){
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETAUTHORISATIONSFORUSERINLOCATION);
                return  false;
            }

            if  (!WebResult.getResultBln()){
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

    public static String pUserToShowStr(String pvUserName){
        if(cUser.allUsersObl == null){
            return null;
        }

        for (cUser user : cUser.allUsersObl)
        {
            if(user.usernameStr.equalsIgnoreCase(pvUserName)){
                return  user.getNameStr();
            }
        }

        return pvUserName;
    }

    public static boolean pTruncateTableBln(){
         cUserViewModel userViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cUserViewModel.class);
        userViewModel.deleteAll();
        return true;
            }

    public static boolean pGetUsersViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cUser.allUsersObl = null;
            cUser.pTruncateTableBln();
        }

        if ( cUser.allUsersObl  != null) {
            return  true;
        }

        cWebresult WebResult;
        cUserViewModel userViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cUserViewModel.class);
        WebResult =  userViewModel.pGetUsersFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


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
