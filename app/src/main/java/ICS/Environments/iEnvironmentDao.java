package ICS.Environments;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iEnvironmentDao {
    @Delete
    void delete(cEnvironmentEntity environmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cEnvironmentEntity environmentEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_ENVIRONMENTS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_ENVIRONMENTS + " ORDER BY " + cDatabase.ENVIRONMENT_DESCRIPTION)
    List<cEnvironmentEntity> getAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_ENVIRONMENTS + " ORDER BY " + cDatabase.ENVIRONMENT_DESCRIPTION + " LIMIT 1")
    cEnvironmentEntity getFirst();


    @Query("SELECT * FROM " + cDatabase.TABLENAME_ENVIRONMENTS +
            " WHERE " + cDatabase.ENVIRONMENT_NAME + " =:name" +
            " LIMIT 1")
    cEnvironmentEntity getEnvironmentByName(String name);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_ENVIRONMENTS +
            " WHERE " + cDatabase.ENVIRONMENT_DEFAULT + " = 1" +
            " LIMIT 1")
    cEnvironmentEntity getDefaultEnvironment();

    @Query("SELECT COUNT(DISTINCT " + cDatabase.ENVIRONMENT_NAME + ") FROM  " + cDatabase.TABLENAME_ENVIRONMENTS)
    int getNumberOfEnvironments();
}
