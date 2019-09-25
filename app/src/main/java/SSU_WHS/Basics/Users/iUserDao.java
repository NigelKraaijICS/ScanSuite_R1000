package SSU_WHS.Basics.Users;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cUserEntity userEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_USERS)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_USERS)
    List<cUserEntity> getAll();
}
