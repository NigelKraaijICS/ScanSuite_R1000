package ICS.Environments;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iEnvironmentDao {

    @Delete
    void delete(cEnvironmentEntity environmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cEnvironmentEntity environmentEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_ENVIRONMENTS)
    void deleteAll();

    @Query("SELECT * FROM Environments")
    List<cEnvironmentEntity> getAll();

    @Query("UPDATE Environments SET isdefault = :pvDefaultBln WHERE name = :pvNameStr")
    int updateDefault (Boolean pvDefaultBln, String pvNameStr);
}
