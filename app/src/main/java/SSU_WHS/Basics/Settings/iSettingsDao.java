package SSU_WHS.Basics.Settings;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cSettingsEntity settingsEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SETTINGS)
    void deleteAll();

    @Query("SELECT Value FROM " + cDatabase.TABLENAME_SETTINGS + " WHERE Name = :pv_nameStr")
    String getSetting(String pv_nameStr);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SETTINGS + " ORDER BY Name")
    LiveData<List<cSettingsEntity>> getSettings();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SETTINGS + " ORDER BY Name")
    List<cSettingsEntity> getLocalSettings();

}
