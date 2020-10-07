package SSU_WHS.Picken.FinishSinglePieceLine;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.Pickorders.cPickorder;

@Entity(tableName=cDatabase.TABLENAME_PICKORDERLINEFINISHSINGLEPIECE)
public class cPickorderLineFinishSinglePieceEntity {

    //Region Public Properties

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    public String getSourceNoStr() { return this.sourceno; }

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    public String getItemNoStr() { return this.itemno; }

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    public String getVariantCodeStr() { return this.variantcode; }

    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    public String getDescriptionStr() { return this.description; }

    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    public String getDescription2Str() { return this.description2; }

    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    public String getVendorItemNoStr() { return this.vendoritemno; }

    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemdescription;
    public String getVendorItemDescriptionStr() { return this.vendoritemdescription; }

    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    public Double getQuantityDbl() { return this.quantity; }

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    public Double getQuantityHandledDbl() { return this.quantityhandled; }

    @ColumnInfo(name = cDatabase.VERZENDLABEL_STATUS_NAMESTR)
    public int status;
    public int getStatusInt() { return this.status; }

    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public Integer localstatus;
    public int getLocalStatusInt() { return this.localstatus; }

    //End Region Public Properties


    //Region Constructor
    public cPickorderLineFinishSinglePieceEntity() {

    }

    public cPickorderLineFinishSinglePieceEntity(JSONObject pvJsonObject) {
        try {

            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);


            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);

            this.quantity = pvJsonObject.getDouble(cDatabase.QUANTITY_NAMESTR);
            this.quantityhandled = pvJsonObject.getDouble(cDatabase.QUANTITYHANDLED_NAMESTR);
            this.status = pvJsonObject.getInt(cDatabase.VERZENDLABEL_STATUS_NAMESTR);

            if (this.status == 49) {
                this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW;
            }
            else {
                this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}
