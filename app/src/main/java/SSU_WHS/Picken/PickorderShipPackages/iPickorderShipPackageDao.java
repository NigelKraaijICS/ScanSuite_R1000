package SSU_WHS.Picken.PickorderShipPackages;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderShipPackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderShipPackageEntity pickorderShipPackageEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERSHIPPACKAGES)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERSHIPPACKAGES)
    List<cPickorderShipPackageEntity> getAll();
}
