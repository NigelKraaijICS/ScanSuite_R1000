package SSU_WHS.PackAndShip.PackAndShipOrders;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrderEntity;

@Dao
public interface iPackAndShipOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipOrderEntity pvPackAndShipOrderEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPORDER)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPORDER)
    List<cPackAndShipOrderEntity> getAll();

    @RawQuery(observedEntities = cPackAndShipOrderEntity.class)
    List<cPackAndShipOrderEntity> getFilteredMoveOrders(SupportSQLiteQuery query);

}
