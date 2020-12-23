package SSU_WHS.PackAndShip.PackAndShipShippingMethod;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipOrderLineEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;

@Dao
public interface iPackAndShipShippingMethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipShippingMethodEntity pvPackAndShipShippingMethodEntity);

    @Delete
    void delete(cPackAndShipShippingMethodEntity pvPackAndShipShippingMethodEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPSHIPPINGMETHOD)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPSHIPPINGMETHOD)
    List<cPackAndShipShippingMethodEntity> getAll();

}
