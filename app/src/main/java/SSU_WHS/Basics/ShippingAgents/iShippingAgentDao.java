package SSU_WHS.Basics.ShippingAgents;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iShippingAgentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentEntity shippingAgentEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTS)
    List<cShippingAgentEntity> getAll();
}
