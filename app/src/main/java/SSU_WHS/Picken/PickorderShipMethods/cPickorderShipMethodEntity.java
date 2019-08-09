package SSU_WHS.Picken.PickorderShipMethods;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERSHIPMETHODS, primaryKeys = {cDatabase.SOURCENO_NAMESTR})
public class cPickorderShipMethodEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    @ColumnInfo(name = cDatabase.SHIPPINGMETHODCODE_NAMESTR)
    public String shipmethodcode;
    @ColumnInfo(name = cDatabase.SHIPPINGMETHODVALUE_NAMESTR)
    public String shipmethodvalue;

    @NonNull
    public String getSourceno() {
        return sourceno;
    }

    public void setSourceno(@NonNull String sourceno) {
        this.sourceno = sourceno;
    }

    public String getShipmethodcode() {
        return shipmethodcode;
    }

    public void setShipmethodcode(String shipmethodcode) {
        this.shipmethodcode = shipmethodcode;
    }

    public String getShipmethodvalue() {
        return shipmethodvalue;
    }

    public void setShipmethodvalue(String shipmethodvalue) {
        this.shipmethodvalue = shipmethodvalue;
    }

    //empty constructor
    public cPickorderShipMethodEntity() {

    }

    public cPickorderShipMethodEntity(JSONObject jsonObject) {
        try {
            sourceno = jsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            shipmethodcode = jsonObject.getString(cDatabase.SHIPPINGMETHODCODE_NAMESTR);
            shipmethodvalue = jsonObject.getString(cDatabase.SHIPPINGMETHODVALUE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
