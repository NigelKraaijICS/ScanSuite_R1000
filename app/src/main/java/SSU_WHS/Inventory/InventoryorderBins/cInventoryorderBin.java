package SSU_WHS.Inventory.InventoryorderBins;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorderViewModel;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cInventoryorderBin {

    public cInventoryorderBinEntity inventoryorderBinEntity;

    private cInventoryorderBinViewModel getInventoryorderBinViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderBinViewModel.class);
    }

    public static List<cInventoryorderBin> allInventoryorderBinsObl;
    public static cInventoryorderBin currentInventoryOrderBin;

    //Region Public Properties

    private String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private int linesInt;
    public int getLinesInt() {
        return linesInt;
    }

    private String handledTimeStampStr;
    public String getHandledTimeStampStr() {
        return handledTimeStampStr;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    //Region Constructor

    public cInventoryorderBin(JSONObject pvJsonObject) {
        this.inventoryorderBinEntity = new cInventoryorderBinEntity(pvJsonObject);
        this.binCodeStr = this.inventoryorderBinEntity.getBinCodeStr();
        this.linesInt = this.inventoryorderBinEntity.getLinesInt();
        this.handledTimeStampStr = this.inventoryorderBinEntity.getHandledTimeStampStr();
        this.statusInt = this.inventoryorderBinEntity.getStatusInt();

    }

    public cInventoryorderBin(cInventoryorderBinEntity pvInventoryorderBinEntity){
        this.inventoryorderBinEntity = pvInventoryorderBinEntity;
        this.binCodeStr = this.inventoryorderBinEntity.getBinCodeStr();
        this.linesInt = this.inventoryorderBinEntity.getLinesInt();
        this.handledTimeStampStr = this.inventoryorderBinEntity.getHandledTimeStampStr();
        this.statusInt = this.inventoryorderBinEntity.getStatusInt();

    }

    public cInventoryorderBin(cBranchBin pvBranchBin){
        this.inventoryorderBinEntity = null;
        this.binCodeStr = pvBranchBin.getBinCodeStr();
        this.linesInt = 0;
        this.handledTimeStampStr = "";
        this.statusInt = cWarehouseorder.InventoryBinStatusEnu.New;
    }


    //End Region Constructor

    public static boolean pTruncateTableBln(){
        cInventoryorderBinViewModel inventoryorderBinViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderBinViewModel.class);
        inventoryorderBinViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        getInventoryorderBinViewModel().insert(this.inventoryorderBinEntity);

        if (cInventoryorderBin.allInventoryorderBinsObl == null){
            cInventoryorderBin.allInventoryorderBinsObl = new ArrayList<>();
        }
        cInventoryorderBin.allInventoryorderBinsObl.add(this);
        return  true;
    }

    public static  void pInsertAllInDatabase(List<cInventoryorderBinEntity> pvInventoryorderBinEntities ) {
        cInventoryorderBinViewModel inventoryorderBinViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderBinViewModel.class);
        inventoryorderBinViewModel.insertAll (pvInventoryorderBinEntities);
        inventoryorderBinViewModel.allNew();
    }

    public void pUpdateStatusAndTimeStampInDatabase(){
        cInventoryorderBin.currentInventoryOrderBin = this;
        getInventoryorderBinViewModel().pUpdateStatusAndTimeStamp();
        cInventoryorderBin.currentInventoryOrderBin = null;
    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;


        if (!cInventoryorder.currentInventoryOrder.isGeneratedBln()) {

            WebResult =  getInventoryorderBinViewModel().pReopenBinViaWebserviceWrs();
            if (!WebResult.getResultBln() || !WebResult.getSuccessBln()){
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINOPEN);
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_bin_via_webservice_failed));
                return  result;
            }
        }

        WebResult =  getInventoryorderBinViewModel().pResetBinViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            // Reset all lines and details via webservice
            List<cInventoryorderLine> hulpObl =cInventoryorder.currentInventoryOrder.pGetLinesForBinObl(this.getBinCodeStr());
            for (cInventoryorderLine inventoryorderLine : hulpObl) {

                cInventoryorderLine.currentInventoryOrderLine = inventoryorderLine;
                inventoryorderLine.pResetRst();
                cInventoryorderLine.currentInventoryOrderLine = null;
            }

            //Reset statusInt
            this.statusInt = cWarehouseorder.InventoryBinStatusEnu.New;
            this.pUpdateStatusAndTimeStampInDatabase();

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_bin_via_webservice_failed));
            return  result;
        }

    }

    public boolean pCloseBln(Boolean pvCloseViaWebserviceBln) {

        if (!pvCloseViaWebserviceBln) {
            this.statusInt = cWarehouseorder.InventoryBinStatusEnu.InventoryDone;
            this.handledTimeStampStr =  cDateAndTime.pGetCurrentDateTimeForWebserviceStr();
            this.pUpdateStatusAndTimeStampInDatabase();
            return true;
        }

        cWebresult webresult;

        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        webresult = inventoryorderViewModel.pCloseBinViaWebserviceWrs(this.getBinCodeStr());

        if (webresult.getResultBln()&& webresult.getSuccessBln()) {

            this.statusInt = cWarehouseorder.InventoryBinStatusEnu.InventoryDone;
            this.handledTimeStampStr =  cDateAndTime.pGetCurrentDateTimeForWebserviceStr();
            this.pUpdateStatusAndTimeStampInDatabase();
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINCLOSE);
            return false;
        }
    }

  }
