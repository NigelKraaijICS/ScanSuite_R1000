package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iInventoryorderLineBarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cInventoryorderLineBarcodeEntity inventoryorderLineBarcodeEntity);

    @Delete
    void delete(cInventoryorderLineBarcodeEntity inventoryorderLineBarcodeEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDERLINEBARCODE)
    void deleteAll();

    @Query("UPDATE " + cDatabase.TABLENAME_INVENTORYORDERLINEBARCODE + " SET " + cDatabase.QUANTITYHANDLED_NAMESTR + " = :pvAmountDbl WHERE " + cDatabase.BARCODE_NAMESTR + " = :pvBarcodeStr")
    void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl);
}



