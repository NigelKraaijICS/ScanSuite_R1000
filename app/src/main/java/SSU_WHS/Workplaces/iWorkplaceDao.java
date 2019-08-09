package SSU_WHS.Workplaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iWorkplaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cWorkplaceEntity workplaceEntity);

    @Delete
    void deleteWorkplace(cWorkplaceEntity workplaceEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_WORKPLACE)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_WORKPLACE)
    List<cWorkplaceEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_WORKPLACE + " WHERE Workplace =:workplace LIMIT 1")
    cWorkplaceEntity getWorkplaceByCode(String workplace);
}
