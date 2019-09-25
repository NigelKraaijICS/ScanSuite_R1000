package SSU_WHS.Picken.WarehouseLocations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_WAREHOUSELOCATIONS)
public class cWarehouseLocationEntity {

    //Region Public Properties

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="WarehouseLocation")
    public String warehouselocation;
    public String getWarehouseLocationStr() {return this.warehouselocation;}

    @ColumnInfo(name="Zone")
    public String zone;
    public String getZoneStr() {return this.zone;}

    @ColumnInfo(name="BinType")
    public String bintype;
    public String getBintypeStr() {return this.bintype;}

    @ColumnInfo(name="UseForStorage")
    public String useforstorage;
    public String getUseForStorageStr() {return this.useforstorage;}

    @ColumnInfo(name="UseForReturnSales")
    public String useforreturnsales;
    public String getUseForReturnSalesStr() {return this.useforreturnsales;}

    @ColumnInfo(name="Description")
    public String description;
    public String getDescriptionStr() {return this.description;}

    //End Region Public Properties

    //Region Constructor

    public cWarehouseLocationEntity() {

    }
    public cWarehouseLocationEntity(JSONObject pvJsonObject) {
        try {
            this.warehouselocation = pvJsonObject.getString(cDatabase.WAREHOUSELOCATION_NAMESTR);
            this.zone = pvJsonObject.getString(cDatabase.ZONE_NAMESTR);
            this.bintype = pvJsonObject.getString(cDatabase.BINTYPE_NAMESTR);
            this.useforstorage = pvJsonObject.getString(cDatabase.USEFORSTORAGE_NAMESTR);
            this.useforreturnsales = pvJsonObject.getString(cDatabase.USEFORRETURNSALES_NAMESTR);
            //apparantly there is a setting: GENERIC_SHOW_BIN_DESCRIPTION, for whatever reason
            try {
                description = pvJsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            } catch (JSONException e) {
                description = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor
}
