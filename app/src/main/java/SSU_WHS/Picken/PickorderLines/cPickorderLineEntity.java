package SSU_WHS.Picken.PickorderLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_PICKORDERLINES)
public class cPickorderLineEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    public Integer getRecordidInt() {return this.recordid;}

    @ColumnInfo(name = cDatabase.LINENO_NAMESTR )
    public Integer lineno;
    public Integer getLineNoInt() {return this.lineno;}

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    public String getItemNoStr() {return this.itemno;}

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    public String getDescriptionStr() {return this.description;}

    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    public String getDescription2Str() {return this.description2;}

    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String bincode;
    public String getBincodeStr() {return this.bincode;}

    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    public Double getQuantityDbl() {return this.quantity;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    public Double getQuantityHandledDbl() {return this.quantityhandled;}

    @ColumnInfo(name = cDatabase.QUANTITYREJECTED_NAMESTR)
    public Double quantityRejected;
    public Double getQuantityRejected() {return this.quantityRejected;}

    @ColumnInfo(name =cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    public String getSourceNoStr() {return this.sourceno;}

    @ColumnInfo(name = cDatabase.DESTINATIONNO_NAMESTR)
    public String destinationno;
    public String getDestinationNoStr() {return this.destinationno;}

    @ColumnInfo(name = cDatabase.PROCESSINGSEQUENCE_NAMESTR)
    public String processingsequence;
    public String getProcessingSequenceStr() {return this.processingsequence;}

    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    public String getVendorItemNoStr() {return this.vendoritemno;}

    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemdescription;
    public String getVendorItemDescriptionStr() {return this.vendoritemdescription;}

    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public Integer status;
    public int getStatusInt() {return this.status;}

    @ColumnInfo(name = cDatabase.STATUSSHIPPING_NAMESTR)
    public Integer statusShipping;
    public int getStatusShippingInt() {return this.statusShipping;}

    @ColumnInfo(name = cDatabase.STATUSPACKING_NAMESTR)
    public Integer statusPacking;
    public int getStatusPackingInt() {return this.statusPacking;}

    @ColumnInfo(name = cDatabase.QUANTITYTAKEN_NAMESTR)
    public double quantitytaken;
    public Double getQuantityTakenDbl() {return this.quantitytaken;}

    @ColumnInfo(name = cDatabase.TAKENTIMESTAMP_NAMESTR)
    public String takenTimeStamp;
    public String getTakenTimeStampStr() {return this.takenTimeStamp;}

    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public int localstatus;
    public int getLocalstatusInt() {return this.localstatus;}

    //empty constructor
    public cPickorderLineEntity() {

    }

    public cPickorderLineEntity(JSONObject pvJsonObject,
                                String pvPickOrderTypeStr) {
        try {
            this.lineno = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);

            this.quantity = pvJsonObject.getDouble(cDatabase.QUANTITY_NAMESTR);
            this.quantityhandled = pvJsonObject.getDouble(cDatabase.QUANTITYHANDLED_NAMESTR);
            this.quantityRejected = pvJsonObject.getDouble(cDatabase.QUANTITYREJECTED_NAMESTR);

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.PICK.toString())) {
                this.quantitytaken =  0;
            } else {
                this.quantitytaken =  pvJsonObject.getDouble(cDatabase.QUANTITYTAKEN_NAMESTR);
            }

            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.destinationno = pvJsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);

            this.processingsequence = pvJsonObject.getString(cDatabase.PROCESSINGSEQUENCE_NAMESTR);

            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);

            this.status = pvJsonObject.getInt(cDatabase.STATUS_NAMESTR);
            this.statusShipping = pvJsonObject.getInt(cDatabase.STATUSSHIPPING_NAMESTR);
            this.statusPacking = pvJsonObject.getInt(cDatabase.STATUSPACKING_NAMESTR);

            this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW;

            this.takenTimeStamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.PICK.toString())) {
                if (this.status > cWarehouseorder.PicklineStatusEnu.Needed) {
                    this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
                }
            }

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.SORT.toString())) {
                if (this.status == cWarehouseorder.PicklineStatusEnu.DONE) {
                    if (Double.compare(this.getQuantityHandledDbl(),this.getQuantityDbl()) == 0) {
                        this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
                    }
                }
            }
            //endregion extraField8Str

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
