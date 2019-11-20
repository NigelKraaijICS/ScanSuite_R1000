package SSU_WHS.Picken.SalesOrderPackingTable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iSalesOrderPackingTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cSalesOrderPackingTableEntity salesOrderPackingTableEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE + " WHERE Packingtable = :pvPackingTableStr")
    void delete(String pvPackingTableStr);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SALESORDERPACKINGTABLE)
    List<cSalesOrderPackingTableEntity> getAll();

}
