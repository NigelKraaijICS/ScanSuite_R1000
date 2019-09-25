package SSU_WHS.Basics.Settings;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cSettingsEntity settingsEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SETTINGS)
    void deleteAll();


}
