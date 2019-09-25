package SSU_WHS.Basics.ItemProperty;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

    @Dao
    public interface iItemPropertyDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(cItemPropertyEntity itemPropertyEntity);

        @Query("DELETE FROM " + cDatabase.TABLENAME_ITEMPROPERTY)
        void deleteAll();

    }

