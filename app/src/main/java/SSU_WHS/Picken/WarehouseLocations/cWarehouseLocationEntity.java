package SSU_WHS.Picken.WarehouseLocations;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_WAREHOUSELOCATIONS)
public class cWarehouseLocationEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="WarehouseLocation")
    public String warehouselocation;
    @ColumnInfo(name="Zone")
    public String zone;
    @ColumnInfo(name="BinType")
    public String bintype;
    @ColumnInfo(name="UseForStorage")
    public String useforstorage;
    @ColumnInfo(name="UseForReturnSales")
    public String useforreturnsales;
    @ColumnInfo(name="Description")
    public String description;

    //empty constructor
    public cWarehouseLocationEntity() {

    }
    public cWarehouseLocationEntity(JSONObject jsonObject) {
        try {
            warehouselocation = jsonObject.getString(cDatabase.WAREHOUSELOCATION_NAMESTR);
            zone = jsonObject.getString(cDatabase.ZONE_NAMESTR);
            bintype = jsonObject.getString(cDatabase.BINTYPE_NAMESTR);
            useforstorage = jsonObject.getString(cDatabase.USEFORSTORAGE_NAMESTR);
            useforreturnsales = jsonObject.getString(cDatabase.USEFORRETURNSALES_NAMESTR);
            //apparantly there is a setting: GENERIC_SHOW_BIN_DESCRIPTION, for whatever reason
            try {
                description = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
            } catch (JSONException e) {
                description = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getWarehouselocation() {return this.warehouselocation;}
    public void setWarehouselocation(String pv_warehouselocation) {this.warehouselocation = pv_warehouselocation;}
    public String getZone() {return this.zone;}
    public void setZone(String pv_zone) {this.zone = pv_zone;}
    public String getBintype() {return this.bintype;}
    public void setBintype(String pv_bintype) {this.bintype = pv_bintype;}
    public String getUseforstorage() {return this.useforstorage;}
    public void setUseforstorage(String pv_useforstorage) {this.useforstorage = pv_useforstorage;}
    public String getUseforreturnsales() {return this.useforreturnsales;}
    public void setUseforreturnsales(String pv_useforreturnsales) {this.useforreturnsales = pv_useforreturnsales;}
    public String getDescription() {return this.description;}
    public void setDescription(String pv_description) {this.description = pv_description;}

}
