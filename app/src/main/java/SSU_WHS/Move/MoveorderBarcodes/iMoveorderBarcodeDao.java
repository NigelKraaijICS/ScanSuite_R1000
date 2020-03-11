package SSU_WHS.Move.MoveorderBarcodes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iMoveorderBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cMoveorderBarcodeEntity moveorderBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDERBARCODE)
    void deleteAll();
}
