package SSU_WHS.Inventory.InventoryorderBins;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderBin {
    public cInventoryorderBinEntity inventoryorderBinEntity;
    public boolean indatabaseBln;

    public static cInventoryorderBinViewModel gInventoryorderBinViewModel;

    public static cInventoryorderBinViewModel getInventoryorderBinViewModel() {
        if (gInventoryorderBinViewModel == null) {
            gInventoryorderBinViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cInventoryorderBinViewModel.class);
        }
        return gInventoryorderBinViewModel;
    }

    public static List<cInventoryorderBin> allInventoryorderBinsObl;
    public static cInventoryorderBin currentInventoryOrderBin;

    //Region Public Properties

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    public int linesInt;
    public int getLinesInt() {
        return linesInt;
    }

    public String handledTimeStampStr;
    public String getHhandledTimeStampStr() {
        return handledTimeStampStr;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    public int sortingSequenceInt;
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

    public static cInventoryorderBinAdapter gInventoryorderBinAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinAdapter() {
        if (gInventoryorderBinAdapter == null) {
            gInventoryorderBinAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinAdapter;
    }

    public static cInventoryorderBinAdapter gInventoryorderBinDoneAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinDoneAdapter() {
        if (gInventoryorderBinDoneAdapter == null) {
            gInventoryorderBinDoneAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinDoneAdapter;
    }
    public static cInventoryorderBinAdapter gInventoryorderBinNotDoneAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinNotDoneAdapter() {
        if (gInventoryorderBinNotDoneAdapter == null) {
            gInventoryorderBinNotDoneAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinNotDoneAdapter;
    }
    public static cInventoryorderBinAdapter gInventoryorderBinTotalAdapter;
    public static cInventoryorderBinAdapter getInventoryorderBinTotalAdapter() {
        if (gInventoryorderBinTotalAdapter == null) {
            gInventoryorderBinTotalAdapter = new cInventoryorderBinAdapter();
        }
        return gInventoryorderBinTotalAdapter;
    }
}
