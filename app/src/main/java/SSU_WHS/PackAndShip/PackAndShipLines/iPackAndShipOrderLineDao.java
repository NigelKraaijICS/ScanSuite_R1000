package SSU_WHS.PackAndShip.PackAndShipLines;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPackAndShipOrderLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipOrderLineEntity pvPackAndShipOrderLineEntity);

    @Delete
    void delete(cPackAndShipOrderLineEntity pvPackAndShipOrderLineEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPORDERLINE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPORDERLINE)
    List<cPackAndShipOrderLineEntity> getAll();

}
