package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iShippingAgentServiceShippingUnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS)
    List<cShippingAgentServiceShippingUnitEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS +
            " WHERE " + cDatabase.SHIPPINGAGENT_NAMESTR + " = :shippingAgent" +
            " AND " + cDatabase.SERVICE_NAMESTR + " = :shippingService")
    List<cShippingAgentServiceShippingUnitEntity> getShippingUnitsByAgentAndService(String shippingAgent, String shippingService);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS +
            " WHERE " + cDatabase.SHIPPINGAGENT_NAMESTR + " = :shippingAgent" +
            " AND " + cDatabase.SERVICE_NAMESTR + " = :shippingService")
    LiveData<List<cShippingAgentServiceShippingUnitEntity>> getShippingUnitsByAgentAndServiceLive(String shippingAgent, String shippingService);

    @Query("UPDATE " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS +
            " SET " + cDatabase.SHIPPINGUNITQUANTITYUSED_NAMESTR + " = :newquantity" +
            " WHERE " + cDatabase.SHIPPINGAGENT_NAMESTR + " = :shippingagent " +
            " AND " + cDatabase.SERVICE_NAMESTR + " =:shippingagentservice" +
            " AND " + cDatabase.SHIPPINGUNIT_NAMESTR + " =:shippingunit")
    void updateShippingUnitQuantityUsed(Integer newquantity, String shippingagent, String shippingagentservice, String shippingunit);


    @Query("UPDATE " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS +
            " SET " + cDatabase.SHIPPINGUNITQUANTITYUSED_NAMESTR + " = 0")
    void resetShippingUnitQuantityUsed();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS +
            " WHERE " + cDatabase.SHIPPINGAGENT_NAMESTR + " = :shippingAgent" +
            " AND " + cDatabase.SERVICE_NAMESTR + " = :shippingService" +
            " AND " + cDatabase.SHIPPINGUNIT_NAMESTR + " = :shippingUnit LIMIT 1")
    cShippingAgentServiceShippingUnitEntity getShippingUnitsByAgentAndServiceAndShippingUnit(String shippingAgent, String shippingService , String shippingUnit);

}
