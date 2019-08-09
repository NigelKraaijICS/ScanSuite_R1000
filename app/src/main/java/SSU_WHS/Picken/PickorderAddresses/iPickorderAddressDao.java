package SSU_WHS.Picken.PickorderAddresses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderAddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderAddressEntity pickorderAddressEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERADDRESS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERADDRESS)
    List<cPickorderAddressEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERADDRESS +
        " WHERE " + cDatabase.ADDRESSCODE_NAMESTR + " =:addresscode" +
        " LIMIT 1")
    cPickorderAddressEntity getAddressByAddresCode(String addresscode);
}
