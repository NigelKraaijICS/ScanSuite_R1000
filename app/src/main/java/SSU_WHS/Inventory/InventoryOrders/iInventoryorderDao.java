package SSU_WHS.Inventory.InventoryOrders;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.Pickorders.cPickorderEntity;

@Dao
public interface iInventoryorderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cInventoryorderEntity inventoryorderEntity);

    @Insert
    void insertAll(List<cInventoryorderEntity> inventoryorderEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INVENTORYORDER)
    void deleteAll();


    @Query("SELECT * FROM InventoryOrder ORDER BY Priority, Opdrachtnummer ASC")
    List<cInventoryorderEntity> getAll();

    @Query("SELECT * FROM InventoryOrder ORDER BY Priority, Opdrachtnummer ASC")
    List<cInventoryorderEntity> getInventoryordersFromDatabase();

    @RawQuery(observedEntities = cInventoryorderEntity.class)
    List<cInventoryorderEntity> getFilteredInventoryOrders(SupportSQLiteQuery query);

}
