package SSU_WHS.Basics.Translations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_TRANSLATION, primaryKeys = {cDatabase.TEXTDUTCH_NAMESTR, cDatabase.LANGUAGEDUTCH_NAMESTR})
public class cTranslationEntity {

    @NonNull
    @ColumnInfo(name=cDatabase.TEXTDUTCH_NAMESTR)
    public String text = "";
    public String getTextStr() {return this.text;}

    @NonNull
    @ColumnInfo(name=cDatabase.LANGUAGEDUTCH_NAMESTR)
    public String language = "";
    public String getLanguageStr() {return this.language;}

    @ColumnInfo(name=cDatabase.TRANSLATIONDUTCH_NAMESTR)
    public String translation = "";
    public String getTranslationStr() {return this.translation;}

    //empty constructor
    public cTranslationEntity() {

    }
    public cTranslationEntity(JSONObject jsonObject) {
        try {
            this.text = jsonObject.getString(cDatabase.TEXTDUTCH_NAMESTR);
            this.language = jsonObject.getString(cDatabase.LANGUAGEDUTCH_NAMESTR);
            this.translation = jsonObject.getString(cDatabase.TRANSLATIONDUTCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
