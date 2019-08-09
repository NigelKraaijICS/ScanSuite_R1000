package SSU_WHS.SalesOrderPackingTable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iSalesOrderPackingTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cSalesOrderPackingTableEntity salesOrderPackingTableEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE)
    List<cSalesOrderPackingTableEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE + " WHERE SalesOrder = :salesorder")
    cSalesOrderPackingTableEntity getPackingTableForSalesOrder(String salesorder);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE + " WHERE Packingtable = :packingtable")
    cSalesOrderPackingTableEntity getSalesorderForPackingTable(String packingtable);
}
