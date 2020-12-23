package SSU_WHS.PackAndShip.PackAndShipShippingPackage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPackAndShipShippingPackageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPackAndShipShippingPackageEntity pvPackAndShipShippingPackageEntity);

    @Delete
    void delete(cPackAndShipShippingPackageEntity pvPackAndShipShippingPackageEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PACKANDSHIPSHIPPINPACKAGE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PACKANDSHIPSHIPPINPACKAGE)
    List<cPackAndShipShippingPackageEntity> getAll();

}
