package SSU_WHS.Basics.ArticlePropertyValue;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_ARTICLEPROPERTYVALUE)
public class cArticlePropertyValueEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    public Integer getRecordidInt() {return this.recordid;}

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR )
    public String itemnoStr;
    public String getItemNoStr() {return this.itemnoStr;}

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR )
    public String variantCodeStr;
    public String getVariantCodeStr() {return this.variantCodeStr;}

    @ColumnInfo(name = cDatabase.PROPERTYCODE_NAMESTR )
    public String propertyCodeStr;
    public String getPropertyCodeStr() {return this.propertyCodeStr;}

    @ColumnInfo(name = cDatabase.VALUE_NAMESTR )
    public String valueStr;
    public String getValueStr() {return this.valueStr;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return this.sortingSequenceNoInt;}

    //empty constructor
    public cArticlePropertyValueEntity() {

    }

    public cArticlePropertyValueEntity(JSONObject pvJsonObject) {
        try {
            this.propertyCodeStr = pvJsonObject.getString(cDatabase.PROPERTYCODE_NAMESTR);
            this.valueStr = pvJsonObject.getString(cDatabase.VALUE_NAMESTR);
            this.sortingSequenceNoInt = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
