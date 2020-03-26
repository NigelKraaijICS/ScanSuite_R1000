package SSU_WHS.General.Licenses;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import ICS.cAppExtension;
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


    public cLicense() {

    }

    public static Boolean pGetLicenseViaWebserviceBln() {

        cWebresult WebResult;

        cLicenseViewModel licenseViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cLicenseViewModel.class);

        WebResult =  licenseViewModel.pLicenseGetViaWebserviceWrs();
        return WebResult.getResultBln() && WebResult.getSuccessBln();
    }

    public static Boolean pReleaseLicenseViaWebserviceBln() {

        cWebresult WebResult;
        cLicenseViewModel licenseViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cLicenseViewModel.class);

        WebResult =  licenseViewModel.pLicenseReleaseViaWebserviceWrs();
        return WebResult.getResultBln() && WebResult.getSuccessBln();
    }

}



