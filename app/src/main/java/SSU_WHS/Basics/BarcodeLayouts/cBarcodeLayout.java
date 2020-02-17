package SSU_WHS.Basics.BarcodeLayouts;

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

public class cBarcodeLayout {

    //region Public Properties
    public String barcodeLayoutStr;
    public String getBarcodeLayoutStr() {
        return barcodeLayoutStr;
    }
    public barcodeLayoutEnu getBarcodeLayoutEnu(){

        switch(getBarcodeLayoutStr().toUpperCase()) {
            case "ARTICLE":
                return  barcodeLayoutEnu.ARTICLE;

            case "BIN":
                return  barcodeLayoutEnu.BIN;

            case "CLIENTID":
                return  barcodeLayoutEnu.CLIENTID;

            case "COMMAND":
                return  barcodeLayoutEnu.COMMAND;

            case "CONTAINER":
                return  barcodeLayoutEnu.CONTAINER;

            case "DOCUMENT":
                return  barcodeLayoutEnu.DOCUMENT;

            case "LOCATION":
                return  barcodeLayoutEnu.LOCATION;

            case "PACKINGTABLEBIN":
                return  barcodeLayoutEnu.PACKINGTABLEBIN;

            case "PICKCARTBOX":
                return  barcodeLayoutEnu.PICKCARTBOX;

            case "PINCODE":
                return  barcodeLayoutEnu.PINCODE;

            case "REASON":
                return  barcodeLayoutEnu.REASON;

            case "SALESORDER":
                return  barcodeLayoutEnu.SALESORDER;

            case "SHIPPINGAGENT":
                return  barcodeLayoutEnu.SHIPPINGAGENT;

            case "SHIPPINGOPTION":
                return  barcodeLayoutEnu.SHIPPINGOPTION;

            case "SHIPPINGPACKAGE":
                return  barcodeLayoutEnu.SHIPPINGPACKAGE;

            case "SHIPPINGSERVICE":
                return  barcodeLayoutEnu.SHIPPINGSERVICE;

            case "STOCKOWNER":
                return  barcodeLayoutEnu.STOCKOWNER;

            case "STORAGEGROUP":
                return  barcodeLayoutEnu.STORAGEGROUP;

            case "WORKPLACE":
                return  barcodeLayoutEnu.WORKPLACE;

            default:
                return barcodeLayoutEnu.UNKNOWN;
        }
    }

    public String layoutValueStr;
    public String getLayoutValueStr() {
        return layoutValueStr;
    }

    public Integer sortorderInt;
    public Integer getSortOrderInt() {
        return sortorderInt;
    }

    public Integer importFileInt;
    public Integer getimportFileInt() {
        return importFileInt;
    }

    public boolean indatabaseBln;

    public static cBarcodeLayoutViewModel gBarcodeLayoutViewModel;
    public static cBarcodeLayoutViewModel getBarcodeLayoutViewViewModel() {
        if (gBarcodeLayoutViewModel == null) {
            gBarcodeLayoutViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cBarcodeLayoutViewModel.class);
        }
        return gBarcodeLayoutViewModel;
    }

    public cBarcodeLayoutEntity barcodeLayoutEntity;
    public static List<cBarcodeLayout> allBarcodeLayoutsObl;

    public enum barcodeLayoutEnu {
        UNKNOWN,
        ARTICLE,
        BIN,
        CLIENTID,
        COMMAND,
        CONTAINER,
        DOCUMENT,
        LOCATION,
        PACKINGTABLEBIN,
        PICKCARTBOX,
        PINCODE,
        REASON,
        SALESORDER,
        SHIPPINGAGENT,
        SHIPPINGOPTION,
        SHIPPINGPACKAGE,
        SHIPPINGSERVICE,
        STOCKOWNER,
        STORAGEGROUP,
        WORKPLACE
    }

    public  static Boolean barcodeLayoutsAvailableBln;

    //end region Public Properties

    ;
    //Region Constructor
    cBarcodeLayout(JSONObject pvJsonObject) {
        this.barcodeLayoutEntity = new cBarcodeLayoutEntity(pvJsonObject);
        this.barcodeLayoutStr =   this.barcodeLayoutEntity.getBarcodelayoutStr();
        this.layoutValueStr =   this.barcodeLayoutEntity.getLayoutValueStr();
        this.sortorderInt =  cText.pStringToIntegerInt(this.barcodeLayoutEntity.getSortOrderStr());
        this.importFileInt = cText.pStringToIntegerInt(this.barcodeLayoutEntity.getImportfileStr());
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cBarcodeLayout.getBarcodeLayoutViewViewModel().insert(this.barcodeLayoutEntity);
        this.indatabaseBln = true;

        if (cBarcodeLayout.allBarcodeLayoutsObl == null){
            cBarcodeLayout.allBarcodeLayoutsObl= new ArrayList<>();
        }
        cBarcodeLayout.allBarcodeLayoutsObl.add(this);
        return  true;
    }

    public static boolean pCheckBarcodeWithLayoutBln(String pvBarcodeStr, barcodeLayoutEnu pvBarcodeLayoutEnu){

        if(cBarcodeLayout.allBarcodeLayoutsObl == null){
            return false;
        }

        cBarcodeLayout barcodeLayoutToCheck = cBarcodeLayout.mGetBarcodeLayoutByEnumerate(pvBarcodeLayoutEnu);
        if (barcodeLayoutToCheck == null){
            return  false;
        }

        ArrayList<cBarcodeLayout> barcodeLayoutScannedObl;
        barcodeLayoutScannedObl = cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeStr);
        if ( barcodeLayoutScannedObl.size() == 0){
            return  false;
        }

        for (cBarcodeLayout barcodeLayout :  barcodeLayoutScannedObl)
        {
            if(barcodeLayout.getBarcodeLayoutEnu() == pvBarcodeLayoutEnu == true){
                return  true;
            }
        }


        return  false;

    }

    public static boolean pCheckBarcodeWithLayoutPrefixBln(String pvBarcodeStr, barcodeLayoutEnu pvBarcodeLayoutEnu){

        if(cBarcodeLayout.allBarcodeLayoutsObl == null){
            return false;
        }

        cBarcodeLayout barcodeLayoutToCheck = cBarcodeLayout.mGetBarcodeLayoutByEnumerate(pvBarcodeLayoutEnu);
        if (barcodeLayoutToCheck == null){
            return  false;
        }

        if (!cRegex.pHasPrefix(pvBarcodeStr)) {
            return  false;
        }

        //Check if prefix in barcode matches prefix in barcode layout
        if (cRegex.pGetPrefix(pvBarcodeStr).equalsIgnoreCase(cRegex.pGetPrefixFromLayout(barcodeLayoutToCheck.getLayoutValueStr()))) {
            return  true;
        }

        return  false;

    }

    public static ArrayList<cBarcodeLayout> pGetBarcodeLayoutByBarcodeObl(String pvBarcodeStr){
        if(cBarcodeLayout.allBarcodeLayoutsObl == null){
            return null;
        }

        ArrayList<cBarcodeLayout> resultObl;
        resultObl = new ArrayList<>();

        for (cBarcodeLayout barcodeLayout : cBarcodeLayout.allBarcodeLayoutsObl)
        {
            if (cRegex.pCheckRegexBln(barcodeLayout.getLayoutValueStr(),pvBarcodeStr) == true) {
               resultObl.add(barcodeLayout);
            }
        }
        return resultObl;
    }

    private static cBarcodeLayout mGetBarcodeLayoutByEnumerate(barcodeLayoutEnu pvBarcodeLayoutEnu){
        if(cBarcodeLayout.allBarcodeLayoutsObl == null){
            return null;
        }

        for (cBarcodeLayout barcodeLayout : cBarcodeLayout.allBarcodeLayoutsObl)
        {
            if (barcodeLayout.getBarcodeLayoutEnu() == pvBarcodeLayoutEnu) {
                return  barcodeLayout;
            }
        }
        return null;
    }

    public static boolean pTruncateTableBln(){
        cBarcodeLayout.getBarcodeLayoutViewViewModel().deleteAll();
        return true;
    }

    public static boolean pGetBarcodeLayoutsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cBarcodeLayout.allBarcodeLayoutsObl = null;
            cBarcodeLayout.pTruncateTableBln();
        }

        if (cBarcodeLayout.allBarcodeLayoutsObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cBarcodeLayout.getBarcodeLayoutViewViewModel().pGetBarcodeLayoutsFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cBarcodeLayout BarcodeLayout = new cBarcodeLayout(jsonObject);
                BarcodeLayout.pInsertInDatabaseBln();
            }
            cBarcodeLayout.barcodeLayoutsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBARCODELAYOUTS);
            return  false;
        }
    }

    //End Region Public Methods
}
