package SSU_WHS.Basics.PropertyGroupProperty;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PROPERTYGROUPPROPERTY, primaryKeys = {cDatabase.PROPERTYDUTCH_NAMESTR})

public class cPropertyGroupPropertyEntity {

    //Region Public Properties

    @ColumnInfo(name = cDatabase.ORIGINDUTCH_NAMESTR)
    public String origin = "";
    public String getOriginStr() { return origin;}

    @ColumnInfo(name = cDatabase.ORIGINKEYDUTCH_NAMESTR)
    public String originKey = "";
    public String getOriginKeyStr() { return originKey;}

    @NonNull
    @ColumnInfo(name = cDatabase.PROPERTYDUTCH_NAMESTR)
    public String property = "";
    public String getPropertyStr() { return property;}

    @ColumnInfo(name = cDatabase.ORDERDUTCH_NAMESTR)
    public int order;
    public int getOrderInt() { return order;}


    //End Region Public Properties

    //Region Constructor
    public cPropertyGroupPropertyEntity() {

    }

    public cPropertyGroupPropertyEntity(JSONObject jsonObject) {
        try {
            this.origin = jsonObject.getString(cDatabase.ORIGINDUTCH_NAMESTR);
            this.originKey = jsonObject.getString(cDatabase.ORIGINKEYDUTCH_NAMESTR);
            this.property = jsonObject.getString(cDatabase.PROPERTYDUTCH_NAMESTR);
            this.order = jsonObject.getInt(cDatabase.ORDERDUTCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}
