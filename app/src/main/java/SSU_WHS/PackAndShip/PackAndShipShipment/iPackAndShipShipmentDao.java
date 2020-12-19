package SSU_WHS.PackAndShip.PackAndShipShipment;

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
public interface iPackAndShipShipmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity);

    @Delete
    void delete(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPSHIPMENT)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPSHIPMENT)
    List<cPackAndShipOrderLineEntity> getAll();

}
