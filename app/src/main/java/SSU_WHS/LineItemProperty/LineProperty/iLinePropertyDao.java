package SSU_WHS.LineItemProperty.LineProperty;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
@Dao
public interface iLinePropertyDao {
    //Standard SQL functions
    @Delete
    void delete(cLinePropertyEntity linePropertyEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cLinePropertyEntity linePropertyEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_LINEPROPERTY)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_LINEPROPERTY)
    List<cLinePropertyEntity> getAll();

}
