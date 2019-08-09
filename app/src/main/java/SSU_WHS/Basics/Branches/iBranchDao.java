package SSU_WHS.Basics.Branches;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iBranchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cBranchEntity branchEntity);

    @Delete
    void deleteBranch(cBranchEntity branchEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_BRANCH)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_BRANCH)
    List<cBranchEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_BRANCH + " WHERE Branch =:branch LIMIT 1")
    cBranchEntity getBranchByCode(String branch);
}
