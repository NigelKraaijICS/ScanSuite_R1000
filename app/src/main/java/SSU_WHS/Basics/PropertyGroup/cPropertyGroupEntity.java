package SSU_WHS.Basics.PropertyGroup;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PROPERTYGROUP, primaryKeys = {cDatabase.PROPERTYGROUP_NAMESTR})

public class cPropertyGroupEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.PROPERTYGROUP_NAMESTR)
    public String propertyGroup = "";
    public String getPropertyGroupStr() { return propertyGroup;}

    @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    public String getDescriptionStr() { return description;}

    @ColumnInfo(name = cDatabase.NAMESHORT_NAMESTR)
    public String shortName;
    public String getShortName() { return shortName;}

    @ColumnInfo(name = cDatabase.IMAGEBASE64_NAMESTR)
    public String imageBase64;
    public String getImageBase64Str() { return imageBase64;}



    //End Region Public Properties

    //Region Constructor
    public cPropertyGroupEntity() {

    }

    public cPropertyGroupEntity(JSONObject jsonObject) {
        try {
            this.propertyGroup = jsonObject.getString(cDatabase.PROPERTYGROUP_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            this.shortName = jsonObject.getString(cDatabase.NAMESHORT_NAMESTR);
            this.imageBase64 = jsonObject.getString(cDatabase.IMAGEBASE64_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}
