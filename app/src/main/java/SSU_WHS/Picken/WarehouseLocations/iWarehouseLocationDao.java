package SSU_WHS.Picken.WarehouseLocations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import static SSU_WHS.General.cDatabase.TABLENAME_WAREHOUSELOCATIONS;

@Dao
public interface iWarehouseLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cWarehouseLocationEntity warehouseLocationEntity);

    @Query("SELECT * FROM " + TABLENAME_WAREHOUSELOCATIONS)
    List<cWarehouseLocationEntity> getAll();

    @Query("DELETE FROM " + TABLENAME_WAREHOUSELOCATIONS)
    void deleteAll();
}
