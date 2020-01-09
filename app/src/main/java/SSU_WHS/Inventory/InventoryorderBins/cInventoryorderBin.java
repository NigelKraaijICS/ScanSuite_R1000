package SSU_WHS.Inventory.InventoryorderBins;

import androidx.lifecycle.ViewModelProviders;

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
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cInventoryorderBin {

    public cInventoryorderBinEntity inventoryorderBinEntity;
    public boolean indatabaseBln;

    private static cInventoryorderBinViewModel gInventoryorderBinViewModel;
    private static cInventoryorderBinViewModel getInventoryorderBinViewModel() {
        if (gInventoryorderBinViewModel == null) {
            gInventoryorderBinViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cInventoryorderBinViewModel.class);
        }
        return gInventoryorderBinViewModel;
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

    private int sortingSequenceInt;
    public int getSortingSequenceInt() { return sortingSequenceInt; }

    //Region Constructor

    public cInventoryorderBin(JSONObject pvJsonObject) {
        this.inventoryorderBinEntity = new cInventoryorderBinEntity(pvJsonObject);
        this.binCodeStr = this.inventoryorderBinEntity.getBinCodeStr();
        this.linesInt = this.inventoryorderBinEntity.getLinesInt();
        this.handledTimeStampStr = this.inventoryorderBinEntity.getHandledTimeStampStr();
        this.statusInt = this.inventoryorderBinEntity.getStatusInt();
        this.sortingSequenceInt = this.inventoryorderBinEntity.getSortingSequenceNoInt();
    }

    public cInventoryorderBin(cInventoryorderBinEntity pvInventoryorderBinEntity){
        this.inventoryorderBinEntity = pvInventoryorderBinEntity;
        this.binCodeStr = this.inventoryorderBinEntity.getBinCodeStr();
        this.linesInt = this.inventoryorderBinEntity.getLinesInt();
        this.handledTimeStampStr = this.inventoryorderBinEntity.getHandledTimeStampStr();
        this.statusInt = this.inventoryorderBinEntity.getStatusInt();
        this.sortingSequenceInt = this.inventoryorderBinEntity.getSortingSequenceNoInt();
    }

    public cInventoryorderBin(cBranchBin pvBranchBin){
        this.inventoryorderBinEntity = null;
        this.binCodeStr = pvBranchBin.getBinCodeStr();
        this.linesInt = 0;
        this.handledTimeStampStr = "";
        this.statusInt = cWarehouseorder.InventoryBinStatusEnu.New;
        this.sortingSequenceInt = 0;
    }


    //End Region Constructor

    public static boolean pTruncateTableBln(){
        cInventoryorderBin.getInventoryorderBinViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        cInventoryorderBin.getInventoryorderBinViewModel().insert(this.inventoryorderBinEntity);
        this.indatabaseBln = true;

        if (cInventoryorderBin.allInventoryorderBinsObl == null){
            cInventoryorderBin.allInventoryorderBinsObl = new ArrayList<>();
        }
        cInventoryorderBin.allInventoryorderBinsObl.add(this);
        return  true;
    }

    public static  void pInsertAllInDatabase(List<cInventoryorderBinEntity> pvInventoryorderBinEntities ) {
        cInventoryorderBin.getInventoryorderBinViewModel().insertAll (pvInventoryorderBinEntities);
    }

    public boolean pUpdateStatusAndTimeStampInDatabaseBln(){

        boolean resultBln;

        cInventoryorderBin.currentInventoryOrderBin = this;

        resultBln =   cInventoryorderBin.getInventoryorderBinViewModel().pUpdateStatusAndTimeStampBln();

        if (!resultBln) {
            cInventoryorderBin.currentInventoryOrderBin = null;
            return  false;
        }
        cInventoryorderBin.currentInventoryOrderBin = null;

        return true;

    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;


        if (!cInventoryorder.currentInventoryOrder.isGeneratedBln()) {

            WebResult =  cInventoryorderBin.getInventoryorderBinViewModel().pReopenBinViaWebserviceWrs();
            if (!WebResult.getResultBln() || !WebResult.getSuccessBln()){
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINOPEN);
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_bin_via_webservice_failed));
                return  result;
            }
        }

            WebResult =  cInventoryorderBin.getInventoryorderBinViewModel().pResetBinViaWebserviceWrs();
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
                this.pUpdateStatusAndTimeStampInDatabaseBln();

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
            this.pUpdateStatusAndTimeStampInDatabaseBln();
            return true;
        }

        cWebresult webresult;
        webresult = cInventoryorder.getInventoryorderViewModel().pCloseBinViaWebserviceWrs(this.getBinCodeStr());
        if (webresult.getResultBln()&& webresult.getSuccessBln()) {

            this.statusInt = cWarehouseorder.InventoryBinStatusEnu.InventoryDone;
            this.handledTimeStampStr =  cDateAndTime.pGetCurrentDateTimeForWebserviceStr();
            this.pUpdateStatusAndTimeStampInDatabaseBln();
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINCLOSE);
            return false;
        }
    }

    private static cInventoryorderBinAdapter gInventoryorderBinAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinAdapter() {
        if (gInventoryorderBinAdapter == null) {
            gInventoryorderBinAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinAdapter;
    }

    private static cInventoryorderBinAdapter gInventoryorderBinDoneAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinDoneAdapter() {
        if (gInventoryorderBinDoneAdapter == null) {
            gInventoryorderBinDoneAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinDoneAdapter;
    }

    private static cInventoryorderBinAdapter gInventoryorderBinNotDoneAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinNotDoneAdapter() {
        if (gInventoryorderBinNotDoneAdapter == null) {
            gInventoryorderBinNotDoneAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinNotDoneAdapter;
    }

    private static cInventoryorderBinAdapter gInventoryorderBinTotalAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinTotalAdapter() {
        if (gInventoryorderBinTotalAdapter == null) {
            gInventoryorderBinTotalAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinTotalAdapter;
    }

}
