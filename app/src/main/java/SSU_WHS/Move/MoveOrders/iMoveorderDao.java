package SSU_WHS.Move.MoveOrders;

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

    @Query("SELECT * FROM " +  cDatabase.TABLENAME_MOVEORDER)
    List<cMoveorderEntity> getMoveordersFromDatabase();

    @RawQuery(observedEntities = cInventoryorderEntity.class)
    List<cMoveorderEntity> getFilteredMoveOrders(SupportSQLiteQuery query);

    @Query("UPDATE "+ cDatabase.TABLENAME_MOVEORDER + " SET "+ cDatabase.CURRENTLOCATION_NAMESTR +" =:CurrentLocationStr WHERE " + cDatabase.ORDERNUMBER_NAMESTR + "=:pvOrderNumberStr")
    int updateMoveorderCurrentLocation(String pvOrderNumberStr, String CurrentLocationStr);


}
