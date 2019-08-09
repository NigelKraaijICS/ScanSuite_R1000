package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR, cDatabase.SERVICE_NAMESTR, cDatabase.SHIPPINGMETHOD_NAMESTR})
public class cShippingAgentServiceShipMethodEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @NonNull
    @ColumnInfo(name = cDatabase.SERVICE_NAMESTR)
    public String service;
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGMETHOD_NAMESTR)
    public String shippingmethod;
    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.VALUETYPE_NAMESTR)
    public String valuetype;
    @ColumnInfo(name = cDatabase.DEFAULTVALUE_NAMESTR)
    public String defaultvalue;
    @ColumnInfo(name = cDatabase.ENUMERATIONVALUES_NAMESTR)
    public String enumerationvalues;

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
    public String getShippingmethod() {
        return shippingmethod;
    }

    public void setShippingmethod(@NonNull String shippingmethod) {
        this.shippingmethod = shippingmethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getEnumerationvalues() {
        return enumerationvalues;
    }

    public void setEnumerationvalues(String enumerationvalues) {
        this.enumerationvalues = enumerationvalues;
    }

    //empty constructor
    public cShippingAgentServiceShipMethodEntity() {

    }
    public cShippingAgentServiceShipMethodEntity(JSONObject jsonObject) {
        try {
            shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            service = jsonObject.getString(cDatabase.SERVICE_NAMESTR);
            shippingmethod = jsonObject.getString(cDatabase.SHIPPINGMETHOD_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            valuetype = jsonObject.getString(cDatabase.VALUETYPE_NAMESTR);
            defaultvalue = jsonObject.getString(cDatabase.DEFAULTVALUE_NAMESTR);
            enumerationvalues = jsonObject.getString(cDatabase.ENUMERATIONVALUES_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
