package SSU_WHS.Basics.BinItem;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_BINITEM)
public class cBinItemEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.ITEMNO_NAMESTR)
    public String itemNoStr = "";
    public String getItemNoStr() {return this.itemNoStr;}

    @ColumnInfo(name=cDatabase.VARIANTCODE_NAMESTR)
    public String variantCodeStr;
    public String getVariantCodeStr() {return this.variantCodeStr;}

    //empty constructor
    public cBinItemEntity() {

    }
    public cBinItemEntity(JSONObject jsonObject) {
        try {
            this.itemNoStr = jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantCodeStr = jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
