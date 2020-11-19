package SSU_WHS.Basics.PropertyGroup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.Basics.ItemProperty.cItemPropertyEntity;
import SSU_WHS.General.cDatabase;

@Dao
public interface iPropertyGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPropertyGroupEntity propertyGroupEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PROPERTYGROUP)
    void deleteAll();

}

