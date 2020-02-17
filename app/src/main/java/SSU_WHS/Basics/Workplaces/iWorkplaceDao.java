package SSU_WHS.Basics.Workplaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iWorkplaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cWorkplaceEntity workplaceEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_WORKPLACE)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_WORKPLACE)
    List<cWorkplaceEntity> getAll();

}
