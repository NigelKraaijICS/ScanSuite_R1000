package SSU_WHS.Basics.StockOwner;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
@Dao
public interface iStockOwnerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cStockOwnerEntity stockOwnerEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_STOCKOWNER)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_STOCKOWNER)
    List<cStockOwnerEntity> getAll();
}
