package SSU_WHS.Picken.PickorderLineProperty;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_PICKORDERLINEPROPERTY)
public class cPickorderLinePropertyEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    public Integer getRecordidInt() {return this.recordid;}

    @ColumnInfo(name = cDatabase.LINENO_NAMESTR )
    public Integer linenoInt;
    public Integer getLineNoInt() {return this.linenoInt;}

    @ColumnInfo(name = cDatabase.PROPERTYCODE_NAMESTR)
    public String propertyCodeStr;
    public String getPropertyCodeStr() {return this.propertyCodeStr;}

    @ColumnInfo(name = cDatabase.SEQUENCENOHANDLED_NAMESTR )
    public Integer sequenceNoHandledInt;
    public Integer getSequenceNoHandledInt() {return this.sequenceNoHandledInt;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR )
    public Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return this.sortingSequenceNoInt;}

    @ColumnInfo(name = cDatabase.LAYOUT_NAMESTR)
    public String layoutStr;
    public String getLayoutStr() {return this.layoutStr;}

    @ColumnInfo(name = cDatabase.ISINPUT_NAMESTR)
    public boolean isInputBln;
    public boolean getIsInputBln() {return this.isInputBln;}

    @ColumnInfo(name = cDatabase.ISREQUIRED_NAMESTR)
    public boolean isRequiredBln;
    public boolean getIsRequiredBln() {return this.isRequiredBln;}

    @ColumnInfo(name = cDatabase.VALUEHANDLED_NAMESTR)
    public String valueHandledStr;
    public String getValueHandledStr() {return this.valueHandledStr;}

    //empty constructor
    public cPickorderLinePropertyEntity() {

    }

    public cPickorderLinePropertyEntity(JSONObject pvJsonObject) {
        try {
            this.linenoInt = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.propertyCodeStr = pvJsonObject.getString(cDatabase.PROPERTYCODE_NAMESTR);
            this.sequenceNoHandledInt = pvJsonObject.getInt(cDatabase.SEQUENCENOHANDLED_NAMESTR);
            this.sortingSequenceNoInt = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
            this.layoutStr = pvJsonObject.getString(cDatabase.LAYOUT_NAMESTR);
            this.isInputBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.ISINPUT_NAMESTR), false) ;
            this.isRequiredBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.ISREQUIRED_NAMESTR), false) ;
            this.valueHandledStr = pvJsonObject.getString(cDatabase.VALUEHANDLED_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
