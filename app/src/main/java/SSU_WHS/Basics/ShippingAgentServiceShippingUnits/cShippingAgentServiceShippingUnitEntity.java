package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR, cDatabase.SERVICE_NAMESTR, cDatabase.SHIPPINGUNIT_NAMESTR})
public class cShippingAgentServiceShippingUnitEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @NonNull
    public String getShippingAgentStr() {
        return shippingagent;
    }

    @NonNull
    @ColumnInfo(name = cDatabase.SERVICE_NAMESTR)
    public String service;
    @NonNull
    public String getServiceStr() {
        return service;
    }


    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGUNIT_NAMESTR)
    public String shippingunit;
    @NonNull
    public String getShippingunitStr() {
        return shippingunit;
    }

    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;

    public String getDescriptionStr() {
        return description;
    }

    @ColumnInfo(name = cDatabase.DEFAULTWEIGHTINGRAMS_NAMESTR)
    public String defaultweightingrams;

    public String getDefaultWeightInGramStr() {
        return defaultweightingrams;
    }

    @ColumnInfo(name = cDatabase.CONTAINERTYPE_DUTCH_NAMESTR)
    public String containertype;
    public String getContainertype() {
        return containertype;
    }

    @ColumnInfo(name = cDatabase.SHIPPINGUNITQUANTITYUSED_NAMESTR)
    public Integer shippingunitquantityused;
    public Integer getShippingUnitQuantityusedInt() {
        return shippingunitquantityused;
    }

    //End Region Public Properties

    //Region Constructor
    public cShippingAgentServiceShippingUnitEntity() {

    }
    public cShippingAgentServiceShippingUnitEntity(JSONObject jsonObject) {
        try {
            this.shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            this.service = jsonObject.getString(cDatabase.SERVICE_NAMESTR);
            this.shippingunit = jsonObject.getString(cDatabase.SHIPPINGUNIT_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            this.defaultweightingrams = jsonObject.getString(cDatabase.DEFAULTWEIGHTINGRAMS_NAMESTR);
            this.containertype = jsonObject.getString(cDatabase.CONTAINERTYPE_DUTCH_NAMESTR);
            this.shippingunitquantityused = jsonObject.getInt(cDatabase.SHIPPINGUNITQUANTITYUSED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}
