package SSU_WHS.PackAndShip.PackAndShipSetting;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipOrderLineEntity;

@Dao
public interface iPackAndShipSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipSettingEntity pvPackAndShipSettingEntity);

    @Delete
    void delete(cPackAndShipSettingEntity pvPackAndShipSettingEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPSETTING)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPSETTING)
    List<cPackAndShipSettingEntity> getAll();

}
