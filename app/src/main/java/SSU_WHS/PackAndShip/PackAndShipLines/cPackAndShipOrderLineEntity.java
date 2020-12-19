package SSU_WHS.PackAndShip.PackAndShipLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPORDERLINE)
public class cPackAndShipOrderLineEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.ACTIONTYPECODE_NAMESTR)
    public String actiontypecode;
    public String getActiontypecodeStr() {return this.actiontypecode;}

    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String bincode;
    public String getBincodeStr() {return this.bincode;}

    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    public String getDescriptionStr() {return this.description;}

    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    public String getDescription2Str() {return this.description2;}

    @ColumnInfo(name = cDatabase.HANDLEDTIMESTAMP_NAMESTR)
    public String handledtimestamp;
    public String getHandledtimestampStr() {return this.handledtimestamp;}

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    public String getItemNoStr() {return this.itemno;}

    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public int lineno;
    public int getLineNoInt() {return this.lineno;}

    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public double quantity;
    public double getQuantityDbl() {return this.quantity;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    public Double getQuantityhandledDbl() {return this.quantityhandled;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingSequenceNo;
    public int getSortingSequenceNoInt() {return this.sortingSequenceNo;}

    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    public int getStatusInt() {return this.status;}

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    public String getVariantCodeStr() {return this.variantcode;}


    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public int localstatus;
    public int getLocalStatusInt() {return this.localstatus;}


    //empty constructor
    public cPackAndShipOrderLineEntity() {

    }

    public cPackAndShipOrderLineEntity(JSONObject pvJsonObject) {
        try {

            this.actiontypecode = pvJsonObject.getString(cDatabase.ACTIONTYPECODE_NAMESTR);
            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.handledtimestamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.lineno = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.quantity = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITY_NAMESTR));
            this.quantityhandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));
            this.sortingSequenceNo = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SORTINGSEQUENCENO_NAMESTR));
            this.status = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.STATUS_NAMESTR));
            this.localstatus = cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_NEW;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
