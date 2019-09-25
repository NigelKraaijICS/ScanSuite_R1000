package ICS.Weberror;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ICS.cICSDatabaseDefinitions;

@Dao
public interface iWeberrorDao {

    @Delete
    void deleteWeberror(cWeberrorEntity weberrorEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cWeberrorEntity weberrorEntity);

    @Delete
    void delete(cWeberrorEntity weberrorEntity);

    @Query("DELETE FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
    void deleteAll();

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
    List<cWeberrorEntity> getAll();

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
    LiveData<List<cWeberrorEntity>> getAllLive();

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:pvActivityStr")
    List<cWeberrorEntity> getAllForActivity(String pvActivityStr);

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_WEBMETHOD + " =:pvMethodSStr")
    List<cWeberrorEntity> getAllForWebMethod(String pvMethodSStr);

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_LOCALSTATUS + " =:pvStatusStr")
    List<cWeberrorEntity> getAllByStatus(String pvStatusStr);

    @Query("SELECT * FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:pvActivityStr"  + " AND " + cICSDatabaseDefinitions.WEBERROR_LOCALSTATUS + " =:pvStatusStr" )
    List<cWeberrorEntity> getAllForActivityAndStatus(String pvActivityStr, String pvStatusStr);

    @Query("DELETE FROM " + cICSDatabaseDefinitions.TABLENAME_WEBERRRORS + " WHERE " + cICSDatabaseDefinitions.WEBERROR_ACTIVITY + " =:pvActivityStr")
    void deleteAllForActivity(String pvActivityStr);

}
