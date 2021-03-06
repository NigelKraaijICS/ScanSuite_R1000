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

    @Insert
    void insertAll(List<cInventoryorderBinEntity> pvInventoryorderBinEntities);

    @Query("UPDATE " + cDatabase.TABLENAME_INVENTORYORDERBIN + " SET Status = " + cWarehouseorder.InventoryBinStatusEnu.New)
    int allNew();

    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN)
    List<cInventoryorderBinEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN +
           " WHERE " + cDatabase.STATUS_NAMESTR + " = " + cWarehouseorder.InventoryBinStatusEnu.New + " ORDER BY " + cDatabase.SORTINGSEQUENCENO_NAMESTR + " ASC")
    List<cInventoryorderBinEntity> getInventoryorderBinNotDone();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN +
            " WHERE " + cDatabase.STATUS_NAMESTR + " = " + cWarehouseorder.InventoryBinStatusEnu.InventoryDone + " ORDER BY " + cDatabase.HANDLEDTIMESTAMP_NAMESTR + " DESC" )
    List<cInventoryorderBinEntity> getInventoryorderBinDone();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN + " ORDER BY " + cDatabase.SORTINGSEQUENCENO_NAMESTR + " ASC")
    List<cInventoryorderBinEntity> getInventoryorderBinTotal();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INVENTORYORDERBIN +
            " WHERE " + cDatabase.BINCODE_NAMESTR + " =:pvBincode" +
            " AND "+ cDatabase.STATUS_NAMESTR + " =:pvStatus")
    cInventoryorderBinEntity checkBin(String pvBincode, Integer pvStatus);


    @Query("UPDATE InventoryOrderBin SET Status = :pvStatusInt , HandledTimestamp = :pvTimeStampStr   WHERE  BinCode = :pvBinCodeStr")
    int updateBinStatus(String pvBinCodeStr, int pvStatusInt, String pvTimeStampStr);


}
