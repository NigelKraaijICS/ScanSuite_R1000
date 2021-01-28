package SSU_WHS.Basics.ItemProperty;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_ITEMPROPERTY, primaryKeys = {cDatabase.PROPERTY_NAMESTR})

public class cItemPropertyEntity {
    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.PROPERTY_NAMESTR)
    public String property = "";
    public String getPropertyStr() { return property;}

    @ColumnInfo(name = cDatabase.LAYOUT_NAMESTR)
    public String layout;
    public String getLayoutStr() { return layout;}

    @ColumnInfo(name = cDatabase.OMSCHRIJVING_NAMESTR)
    public String omschrijving;
    public String getOmschrijvingStr() { return omschrijving;}
    //todo:Check of dit werkt (boolean)
    @ColumnInfo(name = cDatabase.ISUNIQUE_NAMESTR)
    public Boolean isUnique;
    public Boolean getIsUniqueBln() { return isUnique;}

    @ColumnInfo(name = cDatabase.UNIQUNESS_NAMESTR)
    public String uniqueness;
    public String getUniquenessStr() { return uniqueness; }

    @ColumnInfo(name = cDatabase.REMEMBERVALUE_NAMESTR)
    public Boolean rememberValue;
    public Boolean getRememberValueBln() { return rememberValue; }

    @ColumnInfo(name = cDatabase.VALUETYPE_NAMESTR)
    public String valueType;
    public String getValueTypeStr() { return valueType; }

    //End Region Public Properties

    //Region Constructor
    public cItemPropertyEntity() {

    }

    public cItemPropertyEntity(JSONObject jsonObject) {
        try {
            this.property = jsonObject.getString(cDatabase.PROPERTY_NAMESTR);
            this.layout = jsonObject.getString(cDatabase.LAYOUT_NAMESTR);
            this.omschrijving = jsonObject.getString(cDatabase.OMSCHRIJVING_NAMESTR);
            this.isUnique = cText.pStringToBooleanBln(jsonObject.getString(cDatabase.ISUNIQUE_NAMESTR), false) ;
            this.uniqueness =  jsonObject.getString(cDatabase.UNIQUNESS_NAMESTR);
            this.rememberValue = cText.pStringToBooleanBln(jsonObject.getString(cDatabase.REMEMBERVALUE_NAMESTR), false);
            this.valueType = jsonObject.getString(cDatabase.VALUETYPE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}
