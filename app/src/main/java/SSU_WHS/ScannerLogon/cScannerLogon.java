package SSU_WHS.ScannerLogon;

import androidx.lifecycle.ViewModelProviders;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDeviceInfo;
import ICS.Utils.cText;
import ICS.Utils.cUpdate;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Webservice.cWebresult;

public class cScannerLogon {

    //region Public Properties
    public String fixedScannerBranchStr;
    public String getFixedScannerBranchStr() {
        return fixedScannerBranchStr;
    }

    public String scannerDescriptionStr;
    public String getScannerDescriptionStr() {
        return scannerDescriptionStr;
    }

    public String requiredScannerversionStr;
    public String getRequiredScannerversionStr() {
        return requiredScannerversionStr;
    }

    public String applicationEnvironmentStr;
    public String getApplicationEnvironmentStr() {
        return applicationEnvironmentStr;
    }

    public String languagesStr;
    public String getLanguagesStr() {
        return languagesStr;
    }

    public String requiredScannerConfiguration;
    public String getRequiredScannerConfiguration() {
        return requiredScannerConfiguration;
    }

    public boolean reapplyScannerSonfigurationBln;
    public boolean isReapplyScannerSonfigurationBln() {
        return reapplyScannerSonfigurationBln;
    }

    public String versionConfigAppCurrentScannerStr;
    public String getVersionConfigAppCurrentScannerStr() {
        return versionConfigAppCurrentScannerStr;
    }

    public String versionConfigAppRequiredScannerStr;
    public String getVersionConfigAppRequiredScannerStr() {
        return versionConfigAppRequiredScannerStr;
    }

    public String colorsetStr;
    public String getColorsetStr() {
        return colorsetStr;
    }

    public boolean indatabaseBln;

    public static cScannerLogonViewModel gScannerLogonViewModel;
    public static cScannerLogonViewModel getsScannerLogonViewModel() {
        if (gScannerLogonViewModel == null) {
            gScannerLogonViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cScannerLogonViewModel.class);
        }
        return gScannerLogonViewModel;
    }

    public cScannerLogonEntity scannerLogonEntity;
    public static List<cScannerLogon> allScannerLogonObl;
    public  static boolean scannerLoggedOnBln;
    public static cScannerLogon currentScannerLogon;

    //end region Public Properties

    //Region Constructor
    cScannerLogon(List<String> pvScannerStringObl) {
        this.scannerLogonEntity = new cScannerLogonEntity(pvScannerStringObl);
        this.fixedScannerBranchStr = this.scannerLogonEntity.getFixedscannerBranchStr();
        this.scannerDescriptionStr = this.scannerLogonEntity.getScannerDescriptionStr();
        this.requiredScannerversionStr = this.scannerLogonEntity.getRequiredScannerVersionStr();
        this.applicationEnvironmentStr = this.scannerLogonEntity.getApplicationEnvironmentStr();
        this.languagesStr = this.scannerLogonEntity.getLanguagesStr();
        this.requiredScannerConfiguration = this.scannerLogonEntity.getRequiredScannerConfigurationStr();
        this.reapplyScannerSonfigurationBln = cText.pStringToBooleanBln(this.scannerLogonEntity.getReapplyScannerConfigurationStr(),false);
        this.versionConfigAppCurrentScannerStr = this.scannerLogonEntity.getVersionConfigAppCurrentScannerStr();
        this.versionConfigAppRequiredScannerStr = this.scannerLogonEntity.getVersionConfigAppRequiredScannerStr();
        cScannerLogon.currentScannerLogon = this;
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cScannerLogon.getsScannerLogonViewModel().insert(this.scannerLogonEntity);
        this.indatabaseBln = true;

        if (cScannerLogon.allScannerLogonObl == null){
            cScannerLogon.allScannerLogonObl = new ArrayList<>();
        }
        cScannerLogon.allScannerLogonObl.add(this);
        return  true;
    }



    public static boolean pTruncateTableBln(){
        cScannerLogon.getsScannerLogonViewModel().deleteAll();
        return true;
    }


    public static boolean pScannerLogonViaWebserviceBln() {

        cScannerLogon.allScannerLogonObl = null;
        cScannerLogon.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  cScannerLogon.getsScannerLogonViewModel().pScannerLogonWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true && WebResult.getResultObl() != null ){
            cScannerLogon scannerLogon = new cScannerLogon(WebResult.getResultObl());
            scannerLogon.pInsertInDatabaseBln();
            return  true;
        }
        else {
            return false;
        }
    }

    public boolean pScannerVersionCheckBln(FrameLayout pvFrameLayout) {

        if (cDeviceInfo.getAppVersion().equalsIgnoreCase(this.getRequiredScannerversionStr())) {
            cScannerLogon.scannerLoggedOnBln = true;
            return  true;
        } else {
            String subDirectoryStr =this.getRequiredScannerversionStr().replaceAll("\\.", "");
            String URLStr = cPublicDefinitions.UPDATE_BASE_URL + "/" + subDirectoryStr + "/" + cPublicDefinitions.UPDATE_PACKAGE_NAME;
            cUpdate.mUpdateBln(pvFrameLayout, URLStr);
            return false;

        }


    }

    //End Region Public Methods
}
