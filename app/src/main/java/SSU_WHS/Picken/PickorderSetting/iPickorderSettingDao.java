package SSU_WHS.Picken.PickorderSetting;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressEntity;

@Dao
public interface iPickorderSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderSettingEntity pickorderSettingEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERSETTING)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERSETTING)
    List<cPickorderSettingEntity> getAll();

}
