package SSU_WHS.Basics.PropertyGroupProperty;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.General.cDatabase;

@Dao
public interface iPropertyGroupPropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPropertyGroupPropertyEntity propertyGroupPropertyEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PROPERTYGROUPPROPERTY)
    void deleteAll();

}

