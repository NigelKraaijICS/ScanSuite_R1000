package SSU_WHS.Inventory.InventoryorderBins;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iInventoryorderBinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cInventoryorderBinEntity inventoryorderBinEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN)
    List<cInventoryorderBinEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN +
           " WHERE " + cDatabase.STATUS_NAMESTR + " = " + cWarehouseorder.InventoryBinStatusEnu.New)
    List<cInventoryorderBinEntity> getInventoryorderBinNotDone();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN +
            " WHERE " + cDatabase.STATUS_NAMESTR + " = " + cWarehouseorder.InventoryBinStatusEnu.InventoryPause)
    List<cInventoryorderBinEntity> getInventoryorderBinDone();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN)
    List<cInventoryorderBinEntity> getInventoryorderBinTotal();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN +
            " WHERE " + cDatabase.BINCODE_NAMESTR + " =:pvBincode" +
            " AND "+ cDatabase.STATUS_NAMESTR + " =:pvStatus")
    cInventoryorderBinEntity checkBin(String pvBincode, Integer pvStatus);
}
