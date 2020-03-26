package SSU_WHS.Basics.Workplaces;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_WORKPLACE)
public class cWorkplaceEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.WORKPLACE_DUTCH_NAMESTR)
    public String workplace = "";
    public String getWorkplaceStr() {return this.workplace;}

    @ColumnInfo(name=cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    public String getDescriptionStr() {return this.description;}

    //empty constructor
    public cWorkplaceEntity() {

    }
    public cWorkplaceEntity(JSONObject jsonObject) {
        try {
            this.workplace = jsonObject.getString(cDatabase.WORKPLACE_DUTCH_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
