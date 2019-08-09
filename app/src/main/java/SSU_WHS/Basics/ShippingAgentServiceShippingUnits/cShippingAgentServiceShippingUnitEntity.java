package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR, cDatabase.SERVICE_NAMESTR, cDatabase.SHIPPINGUNIT_NAMESTR})
public class cShippingAgentServiceShippingUnitEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @NonNull
    @ColumnInfo(name = cDatabase.SERVICE_NAMESTR)
    public String service;
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGUNIT_NAMESTR)
    public String shippingunit;
    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.DEFAULTWEIGHTINGRAMS_NAMESTR)
    public String defaultweightingrams;
    @ColumnInfo(name = cDatabase.CONTAINERTYPE_DUTCH_NAMESTR)
    public String containertype;
    @ColumnInfo(name = cDatabase.SHIPPINGUNITQUANTITYUSED_NAMESTR)
    public Integer shippingunitquantityused;

    @NonNull
    public String getShippingagent() {
        return shippingagent;
    }

    public void setShippingagent(@NonNull String shippingagent) {
        this.shippingagent = shippingagent;
    }

    @NonNull
    public String getService() {
        return service;
    }

    public void setService(@NonNull String service) {
        this.service = service;
    }

    @NonNull
    public String getShippingunit() {
        return shippingunit;
    }

    public void setShippingunit(@NonNull String shippingunit) {
        this.shippingunit = shippingunit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultweightingrams() {
        return defaultweightingrams;
    }

    public void setDefaultweightingrams(String defaultweightingrams) {
        this.defaultweightingrams = defaultweightingrams;
    }

    public String getContainertype() {
        return containertype;
    }

    public Integer getShippingunitquantityused() {
        return shippingunitquantityused;
    }

    public void setShippingunitquantityused(Integer shippingunitquantityused) {
        this.shippingunitquantityused = shippingunitquantityused;
    }

    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }

    //empty constructor
    public cShippingAgentServiceShippingUnitEntity() {

    }
    public cShippingAgentServiceShippingUnitEntity(JSONObject jsonObject) {
        try {
            shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            service = jsonObject.getString(cDatabase.SERVICE_NAMESTR);
            shippingunit = jsonObject.getString(cDatabase.SHIPPINGUNIT_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            defaultweightingrams = jsonObject.getString(cDatabase.DEFAULTWEIGHTINGRAMS_NAMESTR);
            containertype = jsonObject.getString(cDatabase.CONTAINERTYPE_DUTCH_NAMESTR);
            shippingunitquantityused = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
