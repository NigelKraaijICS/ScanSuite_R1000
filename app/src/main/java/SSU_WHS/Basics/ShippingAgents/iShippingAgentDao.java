package SSU_WHS.Basics.ShippingAgents;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

import static SSU_WHS.General.cDatabase.SHIPPINGAGENT_NAMESTR;

@Dao
public interface iShippingAgentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cShippingAgentEntity shippingAgentEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SHIPPINGAGENTS)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_SHIPPINGAGENTS)
    List<cShippingAgentEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_SHIPPINGAGENTS +
            " WHERE " + SHIPPINGAGENT_NAMESTR + " =:agentname" +
            " LIMIT 1")
    cShippingAgentEntity getShippingAgentEntityByAgentName(String agentname);
}
