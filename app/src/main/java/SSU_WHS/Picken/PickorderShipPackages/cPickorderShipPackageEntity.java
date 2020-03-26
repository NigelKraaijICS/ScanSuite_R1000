package SSU_WHS.Picken.PickorderShipPackages;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERSHIPPACKAGES, primaryKeys = {cDatabase.SOURCENO_NAMESTR})
public class cPickorderShipPackageEntity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    @NonNull
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno = "";
    @NonNull
    public String getSourcenoStr() {
        return sourceno;
    }

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTCODE_NAMESTR)
    public String shipagentcode;
    public String getShippingAgentCodeStr() {
        return shipagentcode;
    }

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR)
    public String shipagentservicecode;
    public String getShippingAgentServiceCodeStr() {
        return shipagentservicecode;
    }

    @ColumnInfo(name = cDatabase.PACKAGETYPE_NAMESTR)
    public String packagetype;
    public String getPackageTypeStr() {
        return packagetype;
    }

    @ColumnInfo(name = cDatabase.PACKAGESEQUENCENUMBER_NAMESTR)
    public String packagesequencenumber;
    public String getPackageSequenceNumberStr() {
        return packagesequencenumber;
    }

    //Region constructor
    public cPickorderShipPackageEntity() {
        //empty constructor
    }

    public cPickorderShipPackageEntity(JSONObject pvJsonObject) {
        try {
            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.shipagentcode = pvJsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
            this.shipagentservicecode = pvJsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
            this.packagetype = pvJsonObject.getString(cDatabase.PACKAGETYPE_NAMESTR);
            this.packagesequencenumber = pvJsonObject.getString(cDatabase.PACKAGESEQUENCENUMBER_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor



}
