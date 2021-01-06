package SSU_WHS.Picken.PickorderLineProperty;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
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

    @ColumnInfo(name = cDatabase.VALUE_NAMESTR)
    public String valueStr;
    public String getValueStr() {return this.valueStr;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR )
    public Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return this.sortingSequenceNoInt;}

    //empty constructor
    public cPickorderLinePropertyEntity() {

    }

    public cPickorderLinePropertyEntity(JSONObject pvJsonObject) {
        try {
            this.linenoInt = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.propertyCodeStr = pvJsonObject.getString(cDatabase.PROPERTYCODE_NAMESTR);
            this.valueStr = pvJsonObject.getString(cDatabase.VALUE_NAMESTR);
            this.sortingSequenceNoInt = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
