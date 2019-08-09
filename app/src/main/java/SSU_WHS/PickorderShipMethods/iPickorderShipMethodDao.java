package SSU_WHS.PickorderShipMethods;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.cDatabase;

@Dao
public interface iPickorderShipMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderShipMethodEntity pickorderShipMethodEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERSHIPMETHODS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERSHIPMETHODS)
    List<cPickorderShipMethodEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERSHIPMETHODS +
            " WHERE " + cDatabase.SOURCENO_NAMESTR + " =:sourceno" +
            " LIMIT 1")
    cPickorderShipMethodEntity getPickorderShipMethodBySourceNo(String sourceno);
}
