package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iShippingAgentServiceShipMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS)
    List<cShippingAgentServiceShipMethodEntity> getAll();
}
