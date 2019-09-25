package SSU_WHS.Basics.Branches;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iBranchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cBranchEntity branchEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_BRANCH)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_BRANCH)
    List<cBranchEntity> getAll();

}
