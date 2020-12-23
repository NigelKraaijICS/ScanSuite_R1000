package SSU_WHS.PackAndShip.PackAndShipBarcode;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipOrderLineEntity;

@Dao
public interface iPackAndShipBarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipBarcodeEntity pvPackAndShipBarcodeEntity);

    @Delete
    void delete(cPackAndShipBarcodeEntity pvPackAndShipBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPBARCODE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPBARCODE)
    List<cPackAndShipBarcodeEntity> getAll();

}
