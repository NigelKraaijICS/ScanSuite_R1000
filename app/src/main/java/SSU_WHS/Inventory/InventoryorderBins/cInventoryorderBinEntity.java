package SSU_WHS.Inventory.InventoryorderBins;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INVENTORYORDERBIN)
public class cInventoryorderBinEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name= cDatabase.BINCODE_NAMESTR)
    public String bincode = "";
    public String getBinCodeStr() {return this.bincode;}

    @ColumnInfo(name= cDatabase.LINES_NAMESTR)
    public int lines;
    public int getLinesInt() {return this.lines;}

    @ColumnInfo(name= cDatabase.HANDLEDTIMESTAMP_NAMESTR)
    public String handledTimeStamp;
    public String getHandledTimeStampStr() {return this.handledTimeStamp;}

    @ColumnInfo(name= cDatabase.STATUS_NAMESTR)
    public int status;
    public int getStatusInt() {return this.status;}

    @ColumnInfo(name= cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingSequenceInt;
    public int getSortingSequenceInt() {return this.sortingSequenceInt;}

    //End Region Public Properties

    //Region Constructor
    public cInventoryorderBinEntity() {

    }

    public cInventoryorderBinEntity(JSONObject pvJsonObject) {
        try {
            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.lines = pvJsonObject.getInt(cDatabase.LINES_NAMESTR);
            this.handledTimeStamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);
            this.status = pvJsonObject.getInt(cDatabase.STATUS_NAMESTR);
            this.sortingSequenceInt = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}


