package SSU_WHS.ShippingAgentsServiceShipMethods;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iShippingAgentServiceShipMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS)
    List<cShippingAgentServiceShipMethodEntity> getAll();
}
