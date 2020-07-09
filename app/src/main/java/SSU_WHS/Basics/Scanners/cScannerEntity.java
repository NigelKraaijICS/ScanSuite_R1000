package SSU_WHS.Basics.Scanners;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_SCANNERS)
public class cScannerEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.WORKPLACE_DUTCH_NAMESTR)
    public String scanner = "";
    public String getScannerStr() {return this.scanner;}

    //empty constructor
    public cScannerEntity() {

    }
    public cScannerEntity(JSONObject jsonObject) {
        try {
            this.scanner = jsonObject.getString(cDatabase.SCANNER_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
