package SSU_WHS.Basics.ShippingAgentServices;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTSERVICES, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR, cDatabase.SERVICE_NAMESTR})
public class cShippingAgentServiceEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @NonNull
    public String getShippingagentStr() {
        return shippingagent;
    }

    @NonNull
    @ColumnInfo(name = cDatabase.SERVICE_NAMESTR)
    public String service;
    public String getServiceStr() {
        return service;
    }

    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    public String getDescriptionStr() {
        return description;
    }

    @ColumnInfo(name = cDatabase.SERVICECOUNTRIES_NAMESTR)
    public String servicecountries;
    public String getServiceCountriesStr() {
        return servicecountries;
    }

      //End Region Public Properties

    //Region Constructor

       public cShippingAgentServiceEntity() {

    }
    public cShippingAgentServiceEntity(JSONObject jsonObject) {
        try {
            this.shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            this.service = jsonObject.getString(cDatabase.SERVICE_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            this.servicecountries = jsonObject.getString(cDatabase.SERVICECOUNTRIES_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}
