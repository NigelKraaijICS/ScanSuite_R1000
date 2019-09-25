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

    @Delete
    void deleteBarcodeLayout(cBarcodeLayoutEntity barcodeLayoutEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_BARCODELAYOUTS)
    void deleteAll();

    @Query("SELECT " + cDatabase.LAYOUTVALUE_NAMESTR + " FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " WHERE BarcodeLayout = :pv_barcodeLayout")
    String getLayoutValue(String pv_barcodeLayout);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " ORDER BY " + cDatabase.BARCODELAYOUT_NAMESTR + "," + cDatabase.SORTORDER_NAMESTR)
    LiveData<List<cBarcodeLayoutEntity>> getBarcodeLayouts();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " ORDER BY " + cDatabase.BARCODELAYOUT_NAMESTR + "," + cDatabase.SORTORDER_NAMESTR)
    List<cBarcodeLayoutEntity> getLocalBarcodeLayouts();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BARCODELAYOUTS + " WHERE BarcodeLayout = :pv_barcodeLayout")
    List<cBarcodeLayoutEntity> getBarcodeLayoutsOfType(String pv_barcodeLayout);
}