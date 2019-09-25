package SSU_WHS.Picken.PickorderAddresses;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

}
