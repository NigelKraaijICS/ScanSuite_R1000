package SSU_WHS.Basics.Scanners;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iScannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cScannerEntity workplaceEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SCANNERS)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_SCANNERS)
    List<cScannerEntity> getAll();

}
