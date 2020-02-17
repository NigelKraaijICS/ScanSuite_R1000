package SSU_WHS.General.Licenses;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cLicenseViewModel extends AndroidViewModel {

    public cLicenseRepository licenseRepository;

    public cLicenseViewModel(Application pvApplication) {
        super(pvApplication);
        this.licenseRepository = new cLicenseRepository();
    }

    public cWebresult pLicenseGetViaWebserviceWrs() {return this.licenseRepository.pLicenseGetWrs();}
    public cWebresult pLicenseReleaseViaWebserviceWrs() {return this.licenseRepository.pLicenseReleaseWrs();}

}


