package SSU_WHS.Picken.SalesOrderPackingTable;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;


public class cSalesOrderPackingTable {

    //Region Public Properties

    private String salesorderStr;
    public String getSalesorderStr() {
        return salesorderStr;
    }

    private String packingtableStr;
    public String getPackingtableStr() {
        return packingtableStr;
    }

    private cSalesOrderPackingTableEntity salesOrderPackingTableEntity;

    public static List<cSalesOrderPackingTable> allSalesOrderPackingTabelsObl;

    private cSalesOrderPackingTableViewModel getSalesOrderPackingTableViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cSalesOrderPackingTableViewModel.class);
    }


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

        cSalesOrderPackingTableViewModel salesOrderPackingTableViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cSalesOrderPackingTableViewModel.class);
        salesOrderPackingTableViewModel.deleteAll();
        cSalesOrderPackingTable.allSalesOrderPackingTabelsObl = null;
        //todo: this isn't used
//        cSalesOrderPackingTable.currentSalesOrderPackingTable = null;
    }

    public boolean pInsertInDatabaseBln() {
        this.getSalesOrderPackingTableViewModel().insert(this.salesOrderPackingTableEntity);

        if (cSalesOrderPackingTable.allSalesOrderPackingTabelsObl == null){
            cSalesOrderPackingTable.allSalesOrderPackingTabelsObl = new ArrayList<>();
        }
        cSalesOrderPackingTable.allSalesOrderPackingTabelsObl.add(this);
        return  true;
    }

    public static boolean pDeleteFromDatabaseBln(String pvProcessingSequenceStr) {

        cSalesOrderPackingTableViewModel salesOrderPackingTableViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cSalesOrderPackingTableViewModel.class);
        salesOrderPackingTableViewModel.delete(pvProcessingSequenceStr);

        if (cSalesOrderPackingTable.allSalesOrderPackingTabelsObl == null) {
            return true;
        }

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
