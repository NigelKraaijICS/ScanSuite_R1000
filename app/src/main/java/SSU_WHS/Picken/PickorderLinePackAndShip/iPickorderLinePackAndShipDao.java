package SSU_WHS.Picken.PickorderLinePackAndShip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderLinePackAndShipDao {

    //Standard SQL functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    void deleteAll();

    @Query("SELECT SUM(QuantityHandled) FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE " + cDatabase.STATUSSHIPPING_NAMESTR + " = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT)
    Double getQuantityHandledDbl();

}
