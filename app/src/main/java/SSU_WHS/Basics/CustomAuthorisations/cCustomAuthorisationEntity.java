package SSU_WHS.Basics.CustomAuthorisations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_CUSTOMAUTHORISATIONS)

//Region Public Properties
public class cCustomAuthorisationEntity {

    @PrimaryKey @NonNull
    @ColumnInfo(name=cDatabase.AUTHORISATION_NAMESTR)
    public String authorisation = "";
    public String getAuthorisationStr() {return this.authorisation;}

    @ColumnInfo(name=cDatabase.DESCRIPTION_DUTCH_NAMESTR)
    public String description;
    public String getDescriptionStr() {return this.description;}

    @ColumnInfo(name=cDatabase.AUTHORISATIONBASE_NAMESTR)
    public String authorisationbase;
    public String getAuthorisationBaseStr() {return this.authorisationbase;}

    @ColumnInfo(name=cDatabase.FILTERFIELD_NAMESTR)
    public String filterfield;
    public String getFilterFieldStr() {return this.filterfield;}

    @ColumnInfo(name=cDatabase.FILTERVALUE_NAMESTR)
    public String filtervalue;
    public String getFilterValueStr() {return this.filtervalue;}

    @ColumnInfo(name=cDatabase.IMAGEBASE64_NAMESTR)
    public String imagebase64;
    public String getImageBase64Str() {return this.imagebase64;}

    //End Region Public Properies

    //Region Constructor
    public cCustomAuthorisationEntity() {

    }

    public cCustomAuthorisationEntity(JSONObject jsonObject) {
        try {
            this.authorisation = jsonObject.getString(cDatabase.AUTHORISATION_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            this.authorisationbase = jsonObject.getString(cDatabase.AUTHORISATIONBASE_NAMESTR);
            this.filterfield = jsonObject.getString(cDatabase.FILTERFIELD_NAMESTR);
            this.filtervalue = jsonObject.getString(cDatabase.FILTERVALUE_NAMESTR);
            this.imagebase64 = jsonObject.getString(cDatabase.IMAGEBASE64_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}

