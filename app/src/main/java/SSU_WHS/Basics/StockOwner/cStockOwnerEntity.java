package SSU_WHS.Basics.StockOwner;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_STOCKOWNER)
public class cStockOwnerEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.STOCKOWNER_NAMESTR)
    public String stockownerStr = "";
    public String getStockownerStr() {return this.stockownerStr;}

    @ColumnInfo(name=cDatabase.OMSCHRIJVING_NAMESTR)
    public String descriptionStr;
    public String getDescriptionStr() {return this.descriptionStr;}



    //End Region Public Properies

    //Region Constructor
    public cStockOwnerEntity() {

    }

    public cStockOwnerEntity(JSONObject jsonObject) {
        try {
            this.stockownerStr = jsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);
            this.descriptionStr = jsonObject.getString(cDatabase.OMSCHRIJVING_NAMESTR);
                  } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}
