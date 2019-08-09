package SSU_WHS.Basics.Workplaces;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_WORKPLACE)
public class cWorkplaceEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="Workplace")
    public String workplace;
    @ColumnInfo(name="Description")
    public String description;

    //empty constructor
    public cWorkplaceEntity() {

    }
    public cWorkplaceEntity(JSONObject jsonObject) {
        try {
            workplace = jsonObject.getString(cDatabase.WORKPLACE_DUTCH_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getWorkplace() {return this.workplace;}
    public void setWorkplace(String pv_workplace) {this.workplace = pv_workplace;}
    public String getDescription() {return this.description;}
    public void setDescription(String pv_description) {this.workplace = pv_description;}

}
