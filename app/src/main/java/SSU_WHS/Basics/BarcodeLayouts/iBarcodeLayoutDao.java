package SSU_WHS.Basics.BarcodeLayouts;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iBarcodeLayoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cBarcodeLayoutEntity barcodeLayoutEntity);

    @Delete
    void deleteBarcodeLayout(cBarcodeLayoutEntity barcodeLayoutEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_BARCODELAYOUTS)
    void deleteAll();

    @Query("SELECT LayoutValue FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " WHERE BarcodeLayout = :pv_barcodeLayout")
    String getLayoutValue(String pv_barcodeLayout);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " ORDER BY BarcodeLayout, SortOrder")
    LiveData<List<cBarcodeLayoutEntity>> getBarcodeLayouts();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " ORDER BY BarcodeLayout, SortOrder")
    List<cBarcodeLayoutEntity> getLocalBarcodeLayouts();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " WHERE BarcodeLayout = :pv_barcodeLayout")
    List<cBarcodeLayoutEntity> getBarcodeLayoutsOfType(String pv_barcodeLayout);
}