package SSU_WHS.Basics.Packaging;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPackaging {

    //region Public Properties

    private String codeStr;
    public String getCodeStr() {
        return codeStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public int quantityInUsedInt;
    public int getQuantityInUsedInt() {
        return quantityInUsedInt;
    }

    public int quantityOutUsedInt;
    public int getQuantityOutUsedInt() {
        return quantityOutUsedInt;
    }

    private cPackagingViewModel getPackagingViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackagingViewModel.class);
    }

    private cPackagingEntity packagingEntity;
    public static List<cPackaging> allPackaging;
    public static cPackaging currentPackaging;
    private static Boolean packagingAvailableBln;

    //end region Public Properties

    //Region Constructor
   public cPackaging(JSONObject pvJsonObject) {
        this.packagingEntity = new cPackagingEntity(pvJsonObject);
        this.codeStr =   this.packagingEntity.getCodeStr();
        this.descriptionStr =   this.packagingEntity.getDescriptionStr();
        this.quantityInUsedInt = 0;
        this.quantityOutUsedInt = 0;
    }

    public cPackaging(cPackaging pvPackaging, String pvModusStr) {
        this.packagingEntity = pvPackaging.packagingEntity;
        this.codeStr =  pvPackaging.getCodeStr();
        this.descriptionStr =   pvPackaging.getDescriptionStr();

        if (pvModusStr.equalsIgnoreCase("IN")) {
            this.quantityInUsedInt = pvPackaging.getQuantityInUsedInt();
        }

        if (pvModusStr.equalsIgnoreCase("OUT")) {
            this.quantityOutUsedInt =  pvPackaging.getQuantityOutUsedInt();
        }
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getPackagingViewModel().insert(this.packagingEntity);

        if (cPackaging.allPackaging == null){
            cPackaging.allPackaging = new ArrayList<>();
        }
        cPackaging.allPackaging.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cPackagingViewModel packagingViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackagingViewModel.class);
        packagingViewModel.deleteAll();
        return true;
    }

    public static boolean pGetPackagingViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPackaging.allPackaging = null;
            cPackaging.pTruncateTableBln();
        }

        if (cPackaging.allPackaging != null) {
            return  true;
        }

        cWebresult WebResult;

        cPackagingViewModel packagingViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackagingViewModel.class);
        WebResult =  packagingViewModel.pGetPackagingFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


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
