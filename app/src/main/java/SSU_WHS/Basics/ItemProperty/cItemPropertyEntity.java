package SSU_WHS.Basics.ItemProperty;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_ITEMPROPERTY, primaryKeys = {cDatabase.PROPERTY_NAMESTR})

public class cItemPropertyEntity {
    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.PROPERTY_NAMESTR)
    public String property;
    public String getPropertyStr() { return property;}

    @ColumnInfo(name = cDatabase.LAYOUTVALUE_NAMESTR)
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
    //todo:Check of dit werkt (boolean)
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
            this.layout = jsonObject.getString(cDatabase.LAYOUTVALUE_NAMESTR);
            this.omschrijving = jsonObject.getString(cDatabase.OMSCHRIJVING_NAMESTR);
            this.isUnique = jsonObject.getBoolean(cDatabase.ISUNIQUE_NAMESTR);
            this.uniqueness = jsonObject.getString(cDatabase.UNIQUNESS_NAMESTR);
            this.rememberValue = jsonObject.getBoolean(cDatabase.REMEMBERVALUE_NAMESTR);
            this.valueType = jsonObject.getString(cDatabase.VALUETYPE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}
