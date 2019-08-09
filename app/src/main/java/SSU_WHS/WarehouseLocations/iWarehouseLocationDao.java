package SSU_WHS.WarehouseLocations;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static SSU_WHS.cDatabase.TABLENAME_WAREHOUSELOCATIONS;

@Dao
public interface iWarehouseLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cWarehouseLocationEntity warehouseLocationEntity);

    @Query("SELECT * FROM " + TABLENAME_WAREHOUSELOCATIONS)
    List<cWarehouseLocationEntity> getAll();

    @Query("DELETE FROM " + TABLENAME_WAREHOUSELOCATIONS)
    void deleteAll();
}
