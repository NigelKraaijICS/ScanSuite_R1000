package SSU_WHS.SalesOrderPackingTable;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.acScanSuiteDatabase;

public class cSalesOrderPackingTableViewModel extends AndroidViewModel {
    private cSalesOrderPackingTableRepository mRepository;

    public cSalesOrderPackingTableViewModel(Application application) {
        super(application);
        mRepository = new cSalesOrderPackingTableRepository(application);
    }

    public void insert(cSalesOrderPackingTableEntity salesOrderPackingTableEntity) {mRepository.insert(salesOrderPackingTableEntity);}

    public void deleteAll() {mRepository.deleteAll();}

    public List<cSalesOrderPackingTableEntity> getAll() {return mRepository.getAll();}

    public cSalesOrderPackingTableEntity getPackingTableForSalesorder(String salesorder) { return mRepository.getPackingTableForSalesorder(salesorder);}

    public cSalesOrderPackingTableEntity getSalesorderForPackingTable(String packingtable) { return mRepository.getSalesorderForPackingTable(packingtable);}
}
