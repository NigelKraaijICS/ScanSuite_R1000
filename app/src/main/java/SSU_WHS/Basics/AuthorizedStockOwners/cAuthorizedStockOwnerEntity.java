package SSU_WHS.Basics.AuthorizedStockOwners;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_AUTHORIZEDSTOCKOWNER)
public class cAuthorizedStockOwnerEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.STOCKOWNER_NAMESTR)
    public String stockownerStr = "";
    @NonNull
    public String getStockownerStr() {return this.stockownerStr;}

    @ColumnInfo(name=cDatabase.BRANCH_NAMESTR)
    public String vestigingStr;
    public String getVestigingStr() {return this.vestigingStr;}

    //End Region Public Properies

    //Region Constructor
    public cAuthorizedStockOwnerEntity() {

    }

    public cAuthorizedStockOwnerEntity(JSONObject jsonObject) {
        try {
            this.stockownerStr = jsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);
            this.vestigingStr = jsonObject.getString(cDatabase.BRANCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}
