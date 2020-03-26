package SSU_WHS.ScannerLogon;

import android.widget.FrameLayout;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUpdate;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Webservice.cWebresult;
import nl.icsvertex.scansuite.BuildConfig;

public class cScannerLogon {

    //region Public Properties

    private String requiredScannerversionStr;
    private String getRequiredScannerversionStr() {
        return requiredScannerversionStr;
    }


    private cScannerLogonViewModel getScannerLoginViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cScannerLogonViewModel.class);
    }

    private cScannerLogonEntity scannerLogonEntity;
    private static List<cScannerLogon> allScannerLogonObl;
    public static boolean scannerLoggedOnBln;
    public static cScannerLogon currentScannerLogon;

    //end region Public Properties

    //Region Constructor
    private cScannerLogon(List<String> pvScannerStringObl) {
        this.scannerLogonEntity = new cScannerLogonEntity(pvScannerStringObl);
        this.requiredScannerversionStr = this.scannerLogonEntity.getRequiredScannerVersionStr();
        cScannerLogon.currentScannerLogon = this;
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getScannerLoginViewModel().insert(this.scannerLogonEntity);

        if (cScannerLogon.allScannerLogonObl == null){
            cScannerLogon.allScannerLogonObl = new ArrayList<>();
        }
        cScannerLogon.allScannerLogonObl.add(this);
        return  true;
    }



    public static boolean pTruncateTableBln(){

        cScannerLogonViewModel scannerLogonViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cScannerLogonViewModel.class);
        scannerLogonViewModel.deleteAll();
        return true;
    }


    public static boolean pScannerLogonViaWebserviceBln() {

        cScannerLogon.allScannerLogonObl = null;
        cScannerLogon.pTruncateTableBln();

        cWebresult WebResult;
        cScannerLogonViewModel scannerLogonViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cScannerLogonViewModel.class);
        WebResult =  scannerLogonViewModel.pScannerLogonWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln() && WebResult.getResultObl() != null ){
            cScannerLogon scannerLogon = new cScannerLogon(WebResult.getResultObl());
            scannerLogon.pInsertInDatabaseBln();
            return  true;
        }
        else {
            return false;
        }
    }

    public boolean pScannerVersionCheckBln(FrameLayout pvFrameLayout) {
        //don't update in debug mode
        if (BuildConfig.DEBUG) {
            cScannerLogon.scannerLoggedOnBln = true;
            return  true;
        }

        if (cDeviceInfo.getAppVersion().equalsIgnoreCase(this.getRequiredScannerversionStr())) {
            cScannerLogon.scannerLoggedOnBln = true;
            return  true;
        } else {
            String subDirectoryStr =this.getRequiredScannerversionStr().replaceAll("\\.", "");
            String URLStr = cPublicDefinitions.UPDATE_BASE_URL + "/" + subDirectoryStr + "/" + cPublicDefinitions.UPDATE_PACKAGE_NAME;
            cUpdate.mUpdateBln(pvFrameLayout, URLStr);
            return false;
//            cScannerLogon.scannerLoggedOnBln = true;
//            return true;
        }
    }

    //End Region Public Methods
}
