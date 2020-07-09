package SSU_WHS.Move.MoveorderLineBarcode;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;

@Dao
public interface iMoveorderLineBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cMoveorderLineBarcodeEntity moveorderBarcodeEntity);

    @Delete
    void delete(cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_MOVEORDERLINEBARCODE)
    void deleteAll();
}
