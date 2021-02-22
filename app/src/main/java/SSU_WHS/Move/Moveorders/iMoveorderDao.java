package SSU_WHS.Move.Moveorders;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorderEntity;

@Dao
public interface iMoveorderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cMoveorderEntity moveorderEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_MOVEORDER)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDER)
    List<cMoveorderEntity> getAll();

    @RawQuery(observedEntities = cMoveorderEntity.class)
    List<cMoveorderEntity> getFilteredMoveOrders(SupportSQLiteQuery query);

}
