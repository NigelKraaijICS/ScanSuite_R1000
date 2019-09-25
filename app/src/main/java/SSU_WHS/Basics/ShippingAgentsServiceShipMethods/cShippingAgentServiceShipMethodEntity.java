package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR, cDatabase.SERVICE_NAMESTR, cDatabase.SHIPPINGMETHOD_NAMESTR})
public class cShippingAgentServiceShipMethodEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    public String getShippingagentStr() {
        return shippingagent;
    }
    @NonNull
    @ColumnInfo(name = cDatabase.SERVICE_NAMESTR)
    public String service;
    public String getServiceStr() {
        return service;
    }
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGMETHOD_NAMESTR)
    public String shippingmethod;
    public String getShippingmethodStr() {
        return shippingmethod;
    }
    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    public String getDescriptionStr() {
        return description;
    }
    @ColumnInfo(name = cDatabase.VALUETYPE_NAMESTR)
    public String valuetype;
    public String getValuetypeStr() {
        return valuetype;
    }
    @ColumnInfo(name = cDatabase.DEFAULTVALUE_NAMESTR)
    public String defaultvalue;
    public String getDefaultvalue() {
        return defaultvalue;
    }
    @ColumnInfo(name = cDatabase.ENUMERATIONVALUES_NAMESTR)
    public String enumerationvalues;
    public String getEnumerationValuesStr() {
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
