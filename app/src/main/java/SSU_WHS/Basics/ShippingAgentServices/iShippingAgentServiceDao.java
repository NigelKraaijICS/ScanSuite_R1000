package SSU_WHS.Basics.ShippingAgentServices;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

import static SSU_WHS.General.cDatabase.SERVICE_NAMESTR;

@Dao
public interface iShippingAgentServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentServiceEntity shippingAgentServiceEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICES)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTSERVICES)
    List<cShippingAgentServiceEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICES +
            " WHERE " + SERVICE_NAMESTR + " =:servicecode" +
            " LIMIT 1")
    cShippingAgentServiceEntity getShippingAgentServiceEntityByServiceCode(String servicecode);
}
