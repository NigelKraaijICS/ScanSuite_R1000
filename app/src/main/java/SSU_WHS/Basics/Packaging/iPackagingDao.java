package SSU_WHS.Basics.Packaging;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPackagingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackagingEntity packagingEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKAGING)
    void deleteAll();

}