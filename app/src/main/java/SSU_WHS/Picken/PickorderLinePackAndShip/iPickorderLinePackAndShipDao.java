package SSU_WHS.Picken.PickorderLinePackAndShip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderLinePackAndShipDao {

    //Standard SQL functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    void deleteAll();

    //Quantity's
    @Query("SELECT SUM(QuantityHandled) FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE " + cDatabase.STATUSSHIPPING_NAMESTR + " = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)
    Double getQuantityNotHandledDbl();

    @Query("SELECT SUM(QuantityHandled) FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE " + cDatabase.STATUSSHIPPING_NAMESTR + " = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT)
    Double getQuantityHandledDbl();

    @Query("SELECT SUM(Quantity) FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    Double getTotalQuantityDbl();

    //Get shiplines
    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    List<cPickorderLinePackAndShipEntity> getAllPickorderLinePackAndShipEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)
    List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE " + cDatabase.LOCALSTATUS_NAMESTR + " > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)
    List<cPickorderLinePackAndShipEntity> getHandledPickorderLinePackAndShipEntities();

    //Update line(s)
    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " SET QuantityHandled = :pvQuantityHandledDbl WHERE recordid = :pvRecordidInt")
    int updateOrderLinePackAndShipQuantity(Integer pvRecordidInt, Double pvQuantityHandledDbl);

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " SET Localstatus = :pvNewStatusInt WHERE recordid = :pvRecordidInt")
    int updateOrderLinePackAndShipLocalStatus(Integer pvRecordidInt, Integer pvNewStatusInt);

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " SET Localstatus = :pvNewStatusInt WHERE  " + cDatabase.SOURCENO_NAMESTR + "= :pvSourceNoStr")
    int updateOrderLinePackAndShipLocalStatusBySourceNo(String pvSourceNoStr, Integer pvNewStatusInt);
}
