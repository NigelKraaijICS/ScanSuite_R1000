package ICS.Weberror;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ICS.cICSDatabaseDefinitions;

@Dao
public interface iWeberrorDao {
    @Delete
    void deleteWeberror(cWeberrorEntity weberrorEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cWeberrorEntity weberrorEntity);

    @Query("DELETE FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
    void deleteAll();

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
    List<cWeberrorEntity> getAll();

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
    LiveData<List<cWeberrorEntity>> getAllLive();

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:activity")
    LiveData<List<cWeberrorEntity>> getAllForActivityLive(String activity);

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:activity")
    List<cWeberrorEntity> getAllForActivity(String activity);

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_LOCALSTATUS + " =:status")
    LiveData<List<cWeberrorEntity>> getAllByStatusLive(String status);

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:activity"  + " AND " + cICSDatabaseDefinitions.WEBERROR_LOCALSTATUS + " =:status" )
    LiveData<List<cWeberrorEntity>> getAllForActivityAndStatusLive(String activity, String status);

    @Query("DELETE FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:activity")
    void deleteAllForActivity(String activity);

}
