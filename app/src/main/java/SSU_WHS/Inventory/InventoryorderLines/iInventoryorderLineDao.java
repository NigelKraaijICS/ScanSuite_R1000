package SSU_WHS.Inventory.InventoryorderLines;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iInventoryorderLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cInventoryorderLineEntity inventoryorderLineEntity);

    @Insert
    void insertAll(List<cInventoryorderLineEntity> inventoryorderLineEntities);

    @Delete
    void delete(cInventoryorderLineEntity inventoryorderLineEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDERLINE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERLINE)
    List<cInventoryorderLineEntity> getAll();

    @Query("SELECT SUM (QuantityHandledAllScanners) FROM " + cDatabase.TABLENAME_INVENTORYORDERLINE + " WHERE " + cDatabase.BINCODE_NAMESTR + " =:pvBincode " )
    Double getCountForBincodeDbl(String pvBincode);

    @Query("SELECT SUM (QuantityHandledAllScanners) FROM " + cDatabase.TABLENAME_INVENTORYORDERLINE )
    Double getTotalCountDbl();

    @Query("UPDATE InventoryOrderLines SET QuantityHandled = :pvQuantityHandledDbl , Quantity = :pvQuantityHandledDbl , QuantityHandledAllScanners = :pvQuantityHandledDbl WHERE LineNo = :pvLineNoLng")
    int updateOrderLineQuantity(Long pvLineNoLng, Double pvQuantityHandledDbl);
}
