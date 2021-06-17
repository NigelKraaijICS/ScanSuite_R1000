package SSU_WHS.PackAndShip.PackAndShipShippingMethod;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPSHIPPINGMETHOD)
public class cPackAndShipShippingMethodEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceNoStr;
    public String getSourceNoStr() {return this.sourceNoStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGMETHODCODE_NAMESTR)
    public String shippingMethodCodeStr;
    public String getShippingMethodCodeStr() {return this.shippingMethodCodeStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGMETHODVALUE_NAMESTR)
    public String shippingMethodValueStr;
    public String getShippingMethodValueStr() {return this.shippingMethodValueStr;}

    //empty constructor
    public cPackAndShipShippingMethodEntity() {

    }

    public cPackAndShipShippingMethodEntity(JSONObject pvJsonObject,  String pvModusStr) {
        try {

            if (pvModusStr.toUpperCase().equals("PICKEN")) {
                this.sourceNoStr =  pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            }
            else
            {
                this.sourceNoStr = cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr();
            }

            this.shippingMethodCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGMETHODCODE_NAMESTR);
            this.shippingMethodValueStr = pvJsonObject.getString(cDatabase.SHIPPINGMETHODVALUE_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
