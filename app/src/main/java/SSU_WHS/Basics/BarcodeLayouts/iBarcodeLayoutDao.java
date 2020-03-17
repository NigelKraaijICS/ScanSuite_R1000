package SSU_WHS.Basics.BarcodeLayouts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iBarcodeLayoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cBarcodeLayoutEntity barcodeLayoutEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_BARCODELAYOUTS)
    void deleteAll();


}