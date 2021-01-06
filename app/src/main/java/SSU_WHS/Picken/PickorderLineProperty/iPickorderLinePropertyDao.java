package SSU_WHS.Picken.PickorderLineProperty;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;

@Dao
public interface iPickorderLinePropertyDao {

    //Standard SQL functions
    @Delete
    void deletePickorder(cPickorderLinePropertyEntity pvPickorderLinePropertyEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLinePropertyEntity pvPickorderLinePropertyEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEPROPERTY)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPROPERTY)
    List<cPickorderLineEntity> getAll();
    //Get picklines


}
