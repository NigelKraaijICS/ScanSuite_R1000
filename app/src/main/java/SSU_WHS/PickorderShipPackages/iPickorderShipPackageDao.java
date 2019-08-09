package SSU_WHS.PickorderShipPackages;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iPickorderShipPackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderShipPackageEntity pickorderShipPackageEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERSHIPPACKAGES)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERSHIPPACKAGES)
    List<cPickorderShipPackageEntity> getAll();
}
