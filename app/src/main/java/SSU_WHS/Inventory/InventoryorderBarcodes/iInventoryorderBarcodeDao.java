package SSU_WHS.Inventory.InventoryorderBarcodes;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iInventoryorderBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cInventoryorderBarcodeEntity inventoryorderBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDERBARCODE)
    void deleteAll();

    @Insert
    void insertAll(List<cInventoryorderBarcodeEntity> inventoryorderBarcodeEntities);


    }


