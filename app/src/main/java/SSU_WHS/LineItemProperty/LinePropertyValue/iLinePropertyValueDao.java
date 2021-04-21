package SSU_WHS.LineItemProperty.LinePropertyValue;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iLinePropertyValueDao {
    //Standard SQL functions
    @Delete
    void delete(cLinePropertyValueEntity linePropertyValueEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cLinePropertyValueEntity linePropertyValueEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_LINEPROPERTYVALUE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_LINEPROPERTYVALUE)
    List<cLinePropertyValueEntity> getAll();

}
