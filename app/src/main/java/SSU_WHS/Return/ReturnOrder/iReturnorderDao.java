package SSU_WHS.Return.ReturnOrder;

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
public interface iReturnorderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cReturnorderEntity returnorderEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_RETURNORDER)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDER)
    List<cReturnorderEntity> getAll();

    @Query("SELECT * FROM " +  cDatabase.TABLENAME_RETURNORDER)
    List<cReturnorderEntity> getReturnordersFromDatabase();

    @RawQuery(observedEntities = cInventoryorderEntity.class)
    List<cReturnorderEntity> getFilteredReturnOrders(SupportSQLiteQuery query);

}
