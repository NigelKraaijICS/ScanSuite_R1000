package SSU_WHS.Basics.Scanners;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cScanner {

    //region Public Properties
    private String scannerStr;
    public String getScannerStr() {
        return scannerStr;
    }

    public  static Boolean scannersAvailableBln;


    private cScannerEntity scannerEntity;

    public static ArrayList<cScanner> allScannerObl;

    private cScannerViewModel getScannerViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cScannerViewModel.class);
    }

    //end region Public Propties

     //Region Constructor
     public cScanner(JSONObject pvJsonObject) {
        this.scannerEntity = new cScannerEntity(pvJsonObject);
        this.scannerStr = scannerEntity.getScannerStr();
    }
    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
         this.getScannerViewModel().insert(this.scannerEntity);

        if (cScanner.allScannerObl == null){
            cScanner.allScannerObl = new ArrayList<>();
        }
        cScanner.allScannerObl.add(this);
        return true;
    }


    public static boolean pTruncateTableBln(){
        cScannerViewModel workplaceViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cScannerViewModel.class);
        workplaceViewModel.deleteAll();
        return true;
    }

    public static boolean pGetScannersViaWebserviceBln() {

        cScanner.allScannerObl = null;
        cScanner.pTruncateTableBln();

        cWebresult WebResult;

        cScannerViewModel workplaceViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cScannerViewModel.class);

        WebResult =  workplaceViewModel.pGetScannersFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject :  WebResult.getResultDtt()) {
                cScanner Workplace = new cScanner(jsonObject);
                Workplace.pInsertInDatabaseBln();
            }
            cScanner.scannersAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSCANNERS);
            return  false;
        }
    }

    //End Region Public Methods
}
