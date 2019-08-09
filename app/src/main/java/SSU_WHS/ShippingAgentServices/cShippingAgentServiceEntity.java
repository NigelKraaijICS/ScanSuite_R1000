package SSU_WHS.ShippingAgentServices;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTSERVICES, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR, cDatabase.SERVICE_NAMESTR})
public class cShippingAgentServiceEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @NonNull
    @ColumnInfo(name = cDatabase.SERVICE_NAMESTR)
    public String service;
    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.SERVICECOUNTRIES_NAMESTR)
    public String servicecounties;

    @NonNull
    public String getShippingagent() {
        return shippingagent;
    }

    public void setShippingagent(@NonNull String shippingagent) {
        this.shippingagent = shippingagent;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServicecounties() {
        return servicecounties;
    }

    public void setServicecounties(String servicecounties) {
        this.servicecounties = servicecounties;
    }

    //empty constructor
    public cShippingAgentServiceEntity() {

    }
    public cShippingAgentServiceEntity(JSONObject jsonObject) {
        try {
            shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            service = jsonObject.getString(cDatabase.SERVICE_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            servicecounties = jsonObject.getString(cDatabase.SERVICECOUNTRIES_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
