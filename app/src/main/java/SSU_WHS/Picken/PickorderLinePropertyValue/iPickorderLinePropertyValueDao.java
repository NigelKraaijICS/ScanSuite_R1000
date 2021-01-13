package SSU_WHS.Picken.PickorderLinePropertyValue;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyEntity;

@Dao
public interface iPickorderLinePropertyValueDao {

    //Standard SQL functions
    @Delete
    void deletePickorder(cPickorderLinePropertyValueEntity pvPickorderLinePropertyValueEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLinePropertyValueEntity pvPickorderLinePropertyValueEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEPROPERTYVALUE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPROPERTYVALUE)
    List<cPickorderLinePropertyValueEntity> getAll();
    //Get picklines


}
