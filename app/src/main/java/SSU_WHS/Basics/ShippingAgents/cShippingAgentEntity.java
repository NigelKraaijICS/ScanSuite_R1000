package SSU_WHS.Basics.ShippingAgents;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTS, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR})
public class cShippingAgentEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @NonNull
    public String getShippingagentStr() {
        return shippingagent;
    }

    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    public String getDescriptionStr() {
        return description;
    }

    @ColumnInfo(name = cDatabase.IMPORTFILE_NAMESTR)
    public String importfile;
    public String getImportfileStr() {
        return importfile;
    }

    //End Region Public Properties

    //Region Constructor
    public cShippingAgentEntity() {

    }

    public cShippingAgentEntity(JSONObject jsonObject) {
        try {
            this.shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            this.importfile = jsonObject.getString(cDatabase.IMPORTFILE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor



}
