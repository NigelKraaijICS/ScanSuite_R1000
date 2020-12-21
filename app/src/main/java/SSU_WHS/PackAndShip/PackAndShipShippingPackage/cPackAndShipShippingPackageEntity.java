package SSU_WHS.PackAndShip.PackAndShipShippingPackage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPSHIPPINPACKAGE)
public class cPackAndShipShippingPackageEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceNoStr = "";
    public String getSourceNoStr() {
        return this.sourceNoStr;
    }

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTCODE_NAMESTR)
    public String shippingAgentCodeStr;
    public String getShippingAgentCodeStr() { return this.shippingAgentCodeStr; }

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR)
    public String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() { return this.shippingAgentServiceCodeStr; }

    @ColumnInfo(name = cDatabase.PACKAGETYPE_NAMESTR)
    public String packageTypeStr;
    public String getPackageTypeStr() { return this.packageTypeStr; }

    @ColumnInfo(name = cDatabase.PACKAGESEQUENCENUMBER_NAMESTR)
    public int packageSequenceNumberInt;
    public int getPackageSequenceNumberInt() {
        return this.packageSequenceNumberInt;
    }

    @ColumnInfo(name = cDatabase.PACKAGEITEMCOUNT_NAMESTR)
    public int packageItemCountInt;
    public int getPackageItemCountInt() {
        return this.packageItemCountInt;
    }

    @ColumnInfo(name = cDatabase.PACKAGEITEMCOUNT_NAMESTR)
    public int packageWeightInGInt;
    public int getPackageWeightInGInt() {
        return this.packageWeightInGInt;
    }

    @ColumnInfo(name = cDatabase.PACKAGECONTAINERTYPE_NAMESTR)
    public String packageContainerTypeStr;
    public String getPackageContainerTypeStr() {
        return this.packageContainerTypeStr;
    }

    @ColumnInfo(name = cDatabase.PACKAGECONTAINER_NAMESTR)
    public String packageContainerStr;
    public String getPackageContainerStr() {
        return this.packageContainerStr;
    }

    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShippingPackageEntity() {

    }

    public cPackAndShipShippingPackageEntity(JSONObject pvJsonObject) {
        try {
            this.sourceNoStr = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.shippingAgentCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
            this.shippingAgentServiceCodeStr =  pvJsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
            this.packageTypeStr = pvJsonObject.getString(cDatabase.PACKAGETYPE_NAMESTR);
            this.packageSequenceNumberInt = pvJsonObject.getInt(cDatabase.PACKAGESEQUENCENUMBER_NAMESTR);
            this.packageWeightInGInt =pvJsonObject.getInt(cDatabase.PACKAGEWEIGHTING_NAMESTR);
            this.packageItemCountInt =pvJsonObject.getInt(cDatabase.PACKAGEITEMCOUNT_NAMESTR);
            this.packageContainerTypeStr = pvJsonObject.getString(cDatabase.PACKAGECONTAINERTYPE_NAMESTR);
            this.packageContainerStr = pvJsonObject.getString(cDatabase.PACKAGECONTAINER_NAMESTR);

            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//End Region Constructor
}