package SSU_WHS.Basics.ShippingAgentServices;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iShippingAgentServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentServiceEntity shippingAgentServiceEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTSERVICES)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTSERVICES)
    List<cShippingAgentServiceEntity> getAll();

}
