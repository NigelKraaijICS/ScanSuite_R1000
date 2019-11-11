package SSU_WHS.General.Licenses;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cResult;
import ICS.cAppExtension;
import SSU_WHS.General.Comments.cCommentAdapter;
import SSU_WHS.General.Comments.cCommentEntity;
import SSU_WHS.General.Comments.cCommentViewModel;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;

public class cLicense {


    public  enum LicenseEnu{

        Unknown,
        Pick,
        Intake,
        Return,
        Move,
        Inventory,
        Pack_and_ship,
        Consolidation,
        Production,
        Other
    }


    public static LicenseEnu currentLicenseEnu = LicenseEnu.Unknown;


    public static cLicenseViewModel licenseViewModel;
    public static cLicenseViewModel getLicenseViewModel() {
        if (licenseViewModel == null) {
            licenseViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cLicenseViewModel.class);
        } return licenseViewModel;
    }

    public cLicense() {

    }

    public static Boolean pGetLicenseViaWebserviceBln() {

        Boolean resultBln = true;

        cWebresult WebResult;
        WebResult =  cLicense.getLicenseViewModel().pLicenseGetViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            resultBln= true;
            return  resultBln;
        }
        else {

            resultBln = false;
            return resultBln;
        }
    }

    public static Boolean pReleaseLicenseViaWebserviceBln() {

        Boolean resultBln = true;

        cWebresult WebResult;
        WebResult =  cLicense.getLicenseViewModel().pLicenseReleaseViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            resultBln = true;
            return  resultBln;
        }
        else {
            resultBln = false;
            return resultBln;
        }
    }

}


