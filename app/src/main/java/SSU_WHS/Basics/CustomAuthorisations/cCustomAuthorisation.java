package SSU_WHS.Basics.CustomAuthorisations;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cCustomAuthorisation {
    //region Public Properties

    private String authorisationStr;
    public String getAuthorisationStr() {
        return authorisationStr;
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private String authorisationBaseStr;
    public String getAuthorisationBaseStr() {
        return authorisationBaseStr;
    }

    private String filterfieldStr;
    public String getFilterfieldStr() {
        return filterfieldStr;
    }

    private String filtervalueStr;
    public String getFiltervalueStr() {
        return filtervalueStr;
    }

    private String imagebase64Str;
    public String getImagebase64Str() {
        return imagebase64Str;
    }

    private cCustomAuthorisationEntity customAuthorisationEntity;

    private cCustomAuthorisationViewModel getCustomAuthorisationViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cCustomAuthorisationViewModel.class);
    }

    public static List<cCustomAuthorisation> allCustomAutorisations;
    public  static Boolean customAutorisationsAvailableBln;

    //end region Public Propties


    //Region Constructor
    public cCustomAuthorisation(JSONObject pvJsonObject) {
        this.customAuthorisationEntity = new cCustomAuthorisationEntity(pvJsonObject);
        this.authorisationStr = this.customAuthorisationEntity.getAuthorisationStr();
        this.descriptionStr = this.customAuthorisationEntity.getDescriptionStr();
        this.authorisationBaseStr = this.customAuthorisationEntity.getAuthorisationBaseStr();
        this.filterfieldStr = this.customAuthorisationEntity.getFilterFieldStr();
        this.filtervalueStr = this.customAuthorisationEntity.getFilterValueStr();
        this.imagebase64Str = this.customAuthorisationEntity.getImageBase64Str();
    }

    //End Region Constructor

//Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getCustomAuthorisationViewModel().insert(this.customAuthorisationEntity);

        if (cCustomAuthorisation.allCustomAutorisations == null){
            cCustomAuthorisation.allCustomAutorisations = new ArrayList<>();
        }
        cCustomAuthorisation.allCustomAutorisations.add(this);

        return true;
    }

    public static boolean pTruncateTableBln(){
        cCustomAuthorisationViewModel customAuthorisationViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cCustomAuthorisationViewModel.class);
        customAuthorisationViewModel.deleteAll();
        return true;
    }

    public static boolean pGetCustomAutorisationsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cCustomAuthorisation.allCustomAutorisations = null;
            cCustomAuthorisation.pTruncateTableBln();
        }

        if ( cCustomAuthorisation.allCustomAutorisations != null) {
            return  true;
        }

        cWebresult WebResult;
        cCustomAuthorisationViewModel customAuthorisationViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cCustomAuthorisationViewModel.class);
        WebResult =  customAuthorisationViewModel.pGetCustomAutorisationsFromWebserviceWrs();
        
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if (WebResult.getResultDtt().size() == 0) {
                cCustomAuthorisation.customAutorisationsAvailableBln = true;
                return  true;
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cCustomAuthorisation customAuthorisation = new cCustomAuthorisation(jsonObject);
                customAuthorisation.pInsertInDatabaseBln();
            }
            cCustomAuthorisation.customAutorisationsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETUSERS);
            return  false;
        }
    }

    //End Region Public Methods


}
