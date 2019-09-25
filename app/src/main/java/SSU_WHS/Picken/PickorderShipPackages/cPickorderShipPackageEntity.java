package SSU_WHS.Picken.PickorderShipPackages;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERSHIPPACKAGES, primaryKeys = {cDatabase.SOURCENO_NAMESTR})
public class cPickorderShipPackageEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    @ColumnInfo(name = cDatabase.SHIPPINGAGENTCODE_NAMESTR)
    public String shipagentcode;
    @ColumnInfo(name = cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR)
    public String shipagentservicecode;
    @ColumnInfo(name = cDatabase.PACKAGETYPE_NAMESTR)
    public String packagetype;
    @ColumnInfo(name = cDatabase.PACKAGESEQUENCENUMBER_NAMESTR)
    public String packagesequencenumber;

    @NonNull
    public String getSourceno() {
        return sourceno;
    }

    public void setSourceno(@NonNull String sourceno) {
        this.sourceno = sourceno;
    }

    public String getShipagentcode() {
        return shipagentcode;
    }

    public void setShipagentcode(String shipagentcode) {
        this.shipagentcode = shipagentcode;
    }

    public String getShipagentservicecode() {
        return shipagentservicecode;
    }

    public void setShipagentservicecode(String shipagentservicecode) {
        this.shipagentservicecode = shipagentservicecode;
    }

    public String getPackagetype() {
        return packagetype;
    }

    public void setPackagetype(String packagetype) {
        this.packagetype = packagetype;
    }

    public String getPackagesequencenumber() {
        return packagesequencenumber;
    }

    public void setPackagesequencenumber(String packagesequencenumber) {
        this.packagesequencenumber = packagesequencenumber;
    }

    //empty constructor
    public cPickorderShipPackageEntity() {

    }

    public cPickorderShipPackageEntity(JSONObject jsonObject) {
        try {
            sourceno = jsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            shipagentcode = jsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
            shipagentservicecode = jsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
            packagetype = jsonObject.getString(cDatabase.PACKAGETYPE_NAMESTR);
            packagesequencenumber = jsonObject.getString(cDatabase.PACKAGESEQUENCENUMBER_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
