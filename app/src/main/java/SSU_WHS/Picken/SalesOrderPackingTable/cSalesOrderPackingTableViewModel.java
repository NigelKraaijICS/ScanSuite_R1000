package SSU_WHS.Picken.SalesOrderPackingTable;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class cSalesOrderPackingTableViewModel extends AndroidViewModel {

    //Region Public Properties
    public cSalesOrderPackingTableRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cSalesOrderPackingTableViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cSalesOrderPackingTableRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cSalesOrderPackingTableEntity salesOrderPackingTableEntity) {this.Repository .insert(salesOrderPackingTableEntity);}
    public void delete(String pvPackingTableStr) {this.Repository.delete(pvPackingTableStr);}
    public void deleteAll() {this.Repository .pTruncateTable();}
    public List<cSalesOrderPackingTableEntity> getAll() {return this.Repository .pGetAllSalesOrderPackingTablesFromDatabaseObl();}

    //End Region Public Methods
}
