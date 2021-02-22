package SSU_WHS.Basics.StockOwner;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cStockOwner {
    //region Public Properties
    private String stockownerStr;
    public String getStockownerStr() {
        return stockownerStr;
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private cStockOwnerEntity stockOwnerEntity;
    

    private  cStockOwnerViewModel getStockOwnerViewModel(){
        return  new ViewModelProvider(cAppExtension.fragmentActivity).get(cStockOwnerViewModel.class);
    }

    public static List<cStockOwner> allStockOwnerObl;
    //End region Public Properties
    //Region Constructor
    private cStockOwner(JSONObject pvJsonObject) {
        this.stockOwnerEntity = new cStockOwnerEntity(pvJsonObject);
        this.stockownerStr = this.stockOwnerEntity.getStockownerStr();
        this.descriptionStr = this.stockOwnerEntity.getDescriptionStr();
    }
    //End Region Constructor

    //Region Public Methods

    public static cStockOwner pGetStockOwnerByCodeStr(String pvStockOwnerCodeStr){

        if (cStockOwner.allStockOwnerObl == null || cStockOwner.allStockOwnerObl.size() == 0 ) {
            return  null;
        }

        for (cStockOwner stockOwner : cStockOwner.allStockOwnerObl) {

            if (stockOwner.getStockownerStr().equalsIgnoreCase(pvStockOwnerCodeStr)) {
                return stockOwner;
            }
        }

        return  null;

    }

    public static cStockOwner pGetStockOwnerByDescriptionStr(String pvStockOwnerDescriptionStr){

        if (cStockOwner.allStockOwnerObl == null || cStockOwner.allStockOwnerObl.size() == 0 ) {
            return  null;
        }

        for (cStockOwner stockOwner : cStockOwner.allStockOwnerObl) {

            if (stockOwner.getDescriptionStr().equalsIgnoreCase(pvStockOwnerDescriptionStr)) {
                return stockOwner;
            }
        }

        return  null;
    }

    public boolean pInsertInDatabaseBln() {
        this.getStockOwnerViewModel().insert(this.stockOwnerEntity);

        if (cStockOwner.allStockOwnerObl == null){
            cStockOwner.allStockOwnerObl = new ArrayList<>();
        }
        cStockOwner.allStockOwnerObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cStockOwnerViewModel stockOwnerViewModel =    new ViewModelProvider(cAppExtension.fragmentActivity).get(cStockOwnerViewModel.class);
        stockOwnerViewModel.deleteAll();
        return true;
    }

    public static boolean pStockOwnerViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cStockOwner.allStockOwnerObl = null;
            cStockOwner.pTruncateTableBln();
        }

        if ( cStockOwner.allStockOwnerObl != null) {
            return false;
        }

        cWebresult WebResult;
        cStockOwnerViewModel stockOwnerViewModel =    new ViewModelProvider(cAppExtension.fragmentActivity).get(cStockOwnerViewModel.class);
        WebResult =  stockOwnerViewModel.pStockOwnerFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cStockOwner stockOwner = new cStockOwner(jsonObject);
                stockOwner.pInsertInDatabaseBln();
            }
            return  true;  }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_STOCKOWNERGET);
            return  false;}
    }


    //End Region Public Methods
}
