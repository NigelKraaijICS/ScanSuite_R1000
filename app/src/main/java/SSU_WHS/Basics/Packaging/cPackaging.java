package SSU_WHS.Basics.Packaging;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPackaging {

    //region Public Properties

    public String codeStr;
    public String getCodeStr() {
        return codeStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public static cPackagingViewModel gPackagingViewModel;
    public static cPackagingViewModel getPackagingViewModel() {
        if (gPackagingViewModel == null) {
            gPackagingViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cPackagingViewModel.class);
        }
        return gPackagingViewModel;
    }

    public cPackagingEntity packagingEntity;
    public static List<cPackaging> allPackaging;

    public  static Boolean packagingAvailableBln;

    //end region Public Properties

    ;
    //Region Constructor
    cPackaging(JSONObject pvJsonObject) {
        this.packagingEntity = new cPackagingEntity(pvJsonObject);
        this.codeStr =   this.packagingEntity.getCodeStr();
        this.descriptionStr =   this.packagingEntity.getDescriptionStr();
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cPackaging.getPackagingViewModel().insert(this.packagingEntity);

        if (cPackaging.allPackaging == null){
            cPackaging.allPackaging = new ArrayList<>();
        }
        cPackaging.allPackaging.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cPackaging.getPackagingViewModel().deleteAll();
        return true;
    }

    public static boolean pGetPackagingViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cPackaging.allPackaging = null;
            cPackaging.pTruncateTableBln();
        }

        if (cPackaging.allPackaging != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cPackaging.getPackagingViewModel().pGetPackagingFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackaging packaging = new cPackaging(jsonObject);
                packaging.pInsertInDatabaseBln();
            }
            cPackaging.packagingAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKAGING);
            return  false;
        }
    }

    //End Region Public Methods
}
