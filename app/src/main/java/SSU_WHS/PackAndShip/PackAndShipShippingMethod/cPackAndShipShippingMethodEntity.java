package SSU_WHS.PackAndShip.PackAndShipShippingMethod;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPSHIPPINGMETHOD)
public class cPackAndShipShippingMethodEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.SHIPPINGMETHODCODE_NAMESTR)
    public String shippingMethodCodeStr;
    public String getShippingMethodCodeStr() {return this.shippingMethodCodeStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGMETHODVALUE_NAMESTR)
    public String shippingMethodValueStr;
    public String getShippingMethodValueStr() {return this.shippingMethodValueStr;}

    //empty constructor
    public cPackAndShipShippingMethodEntity() {

    }

    public cPackAndShipShippingMethodEntity(JSONObject pvJsonObject) {
        try {

            this.shippingMethodCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGMETHODCODE_NAMESTR);
            this.shippingMethodValueStr = pvJsonObject.getString(cDatabase.SHIPPINGMETHODVALUE_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
