package SSU_WHS.Users;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.Users.cUserEntity;
import SSU_WHS.cDatabase;

@Dao
public interface iUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cUserEntity userEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_USERS)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_USERS)
    List<cUserEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_USERS)
    List<cUserEntity> getLocalUsers();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_USERS + " WHERE Username =:username LIMIT 1")
    cUserEntity getUserByCode(String username);
}
