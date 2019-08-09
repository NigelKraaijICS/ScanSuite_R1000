package SSU_WHS.Basics.ShippingAgents;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SHIPPINGAGENTS, primaryKeys = {cDatabase.SHIPPINGAGENT_NAMESTR})
public class cShippingAgentEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SHIPPINGAGENT_NAMESTR)
    public String shippingagent;
    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.IMPORTFILE_NAMESTR)
    public String importfile;


    //empty constructor
    public cShippingAgentEntity() {

    }

    public cShippingAgentEntity(JSONObject jsonObject) {
        try {
            shippingagent = jsonObject.getString(cDatabase.SHIPPINGAGENT_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            importfile = jsonObject.getString(cDatabase.IMPORTFILE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public String getShippingagent() {
        return shippingagent;
    }

    public void setShippingagent(@NonNull String shippingagent) {
        this.shippingagent = shippingagent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImportfile() {
        return importfile;
    }

    public void setImportfile(String importfile) {
        this.importfile = importfile;
    }

}
