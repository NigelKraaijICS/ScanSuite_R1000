package SSU_WHS.PackAndShip.PackAndShipAddress;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcodeEntity;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipOrderLineEntity;

@Dao
public interface iPackAndShipAddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipAddressEntity pvPackAndShipAddressEntity);

    @Delete
    void delete(cPackAndShipAddressEntity pvPackAndShipAddressEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPADDRESS)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPADDRESS)
    List<cPackAndShipAddressEntity> getAll();

}
