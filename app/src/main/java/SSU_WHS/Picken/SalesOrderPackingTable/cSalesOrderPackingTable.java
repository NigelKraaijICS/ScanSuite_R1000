package SSU_WHS.Picken.SalesOrderPackingTable;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;


public class cSalesOrderPackingTable {

    //Region Public Properties

    public String salesorderStr;
    public String getSalesorderStr() {
        return salesorderStr;
    }

    public String packingtableStr;

    public String getPackingtableStr() {
        return packingtableStr;
    }

    public cSalesOrderPackingTableEntity salesOrderPackingTableEntity;
    public boolean indatabaseBln;

    public static List<cSalesOrderPackingTable> allSalesOrderPackingTabelsObl;

    public static cSalesOrderPackingTableViewModel gSalesOrderPackingTableViewModel;

    public static cSalesOrderPackingTableViewModel getSalesOrderPackingTableViewModel() {
        if (gSalesOrderPackingTableViewModel == null) {
            gSalesOrderPackingTableViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cSalesOrderPackingTableViewModel.class);
        }
        return gSalesOrderPackingTableViewModel;
    }

    public static cSalesOrderPackingTable currentSalesOrderPackingTable;

    //End Region Public Properties

    //Region Constructor
    public cSalesOrderPackingTable(String pvSalesOrderStr, String pvPackingTableStr){
        this.salesOrderPackingTableEntity = new cSalesOrderPackingTableEntity(pvSalesOrderStr,pvPackingTableStr);
        this.salesorderStr = this.salesOrderPackingTableEntity.getSalesOrderStr();
        this.packingtableStr = this.salesOrderPackingTableEntity.getPackingTableStr();
    }
    //End Region Constructor

    //Region Public Methods

    public static void pTruncateTable() {
        cSalesOrderPackingTable.getSalesOrderPackingTableViewModel().deleteAll();
        cSalesOrderPackingTable.allSalesOrderPackingTabelsObl = null;
        cSalesOrderPackingTable.currentSalesOrderPackingTable = null;
    }

    public boolean pInsertInDatabaseBln() {
        cSalesOrderPackingTable.getSalesOrderPackingTableViewModel().insert(this.salesOrderPackingTableEntity);
        this.indatabaseBln = true;

        if (cSalesOrderPackingTable.allSalesOrderPackingTabelsObl == null){
            cSalesOrderPackingTable.allSalesOrderPackingTabelsObl = new ArrayList<>();
        }
        cSalesOrderPackingTable.allSalesOrderPackingTabelsObl.add(this);
        return  true;
    }

    public static boolean pDeleteFromDatabaseBln(String pvProcessingSequenceStr) {

        cSalesOrderPackingTable.getSalesOrderPackingTableViewModel().delete(pvProcessingSequenceStr);

        for (cSalesOrderPackingTable salesOrderPackingTable : cSalesOrderPackingTable.allSalesOrderPackingTabelsObl) {

            if (salesOrderPackingTable.getPackingtableStr().equalsIgnoreCase(pvProcessingSequenceStr)) {
                cSalesOrderPackingTable.allSalesOrderPackingTabelsObl.remove(salesOrderPackingTable);
                return  true;
            }
        }

        return  false;

    }







    //End Region Public Methods

}
