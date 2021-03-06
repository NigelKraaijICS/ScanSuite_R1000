package SSU_WHS.Picken.PickorderLinePackAndShip;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.Pickorders.cPickorder;

@Entity(tableName=cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
public class cPickorderLinePackAndShipEntity {

    //Region Public Properties

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public Integer lineno;
    public Integer getLineNoInt() { return this.lineno; }

    @ColumnInfo(name = cDatabase.LINENOTAKE_NAMESTR)
    public Integer linenoTake;
    public Integer getLineNoTakeInt() { return this.linenoTake; }

    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    public String getSourceNoStr() { return this.sourceno; }

    @ColumnInfo(name = cDatabase.DESTINATIONNO_NAMESTR)
    public String destinationno;
    public String getDestinationNoStr() { return this.destinationno; }

    @ColumnInfo(name = cDatabase.DELIVERYADDRESSCODE_NAMESTR)
    public String deliveryaddresscode;
    public String getDeliveryAdressCodeStr() { return this.deliveryaddresscode; }

    @ColumnInfo(name = cDatabase.PROCESSINGSEQUENCE_NAMESTR)
    public String processingsequence;
    public String getProcessingSequenceStr() { return this.processingsequence; }

    @ColumnInfo(name = cDatabase.STATUSSHIPPING_NAMESTR)
    public int statusshipping;
    public int getStatusShippingInt() { return this.statusshipping; }

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTCODE_NAMESTR)
    public String shippingagentcode;
    public String getShippinAgentCodeStr() { return this.shippingagentcode; }

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR)
    public String shippingagentservicecode;
    public String getShippinAgentServiceCodeStr() { return this.shippingagentservicecode; }

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

    @ColumnInfo(name = cDatabase.QUANTITYCHECKED_NAMESTR)
    public Double quantityChecked;
    public Double getQuantityChecked() {return this.quantityChecked;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    public Double getQuantityHandledDbl() { return this.quantityhandled; }

    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    public int getStatusInt() { return this.status; }

    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public Integer localstatus;
    public int getLocalStatusInt() { return this.localstatus; }

    @ColumnInfo(name = cDatabase.STORESOURCEORDER_NAMESTR)
    public String storeSourceOrder;
    public String getStoreSourceOrder() {return this.storeSourceOrder;}

    //End Region Public Properties


    //Region Constructor
    public cPickorderLinePackAndShipEntity() {

    }

    public cPickorderLinePackAndShipEntity(JSONObject pvJsonObject) {
        try {
            this.lineno = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);

            if (cPickorder.currentPickOrder.PICK_SHIPPING_QC_CHECK_COUNT()) {
                this.linenoTake = pvJsonObject.getInt(cDatabase.LINENOTAKE_NAMESTR);
            }
            else
            {
                this.linenoTake = 0;
            }
            this.status = pvJsonObject.getInt(cDatabase.STATUS_NAMESTR);
            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.destinationno = pvJsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);

            this.deliveryaddresscode = pvJsonObject.getString(cDatabase.DELIVERYADDRESSCODE_NAMESTR);
            this.processingsequence = pvJsonObject.getString(cDatabase.PROCESSINGSEQUENCE_NAMESTR);
            this.statusshipping = pvJsonObject.getInt(cDatabase.STATUSSHIPPING_NAMESTR);

            this.shippingagentcode = pvJsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
            this.shippingagentservicecode = pvJsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);

            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);

            this.quantity = pvJsonObject.getDouble(cDatabase.QUANTITY_NAMESTR);
            this.quantityhandled = pvJsonObject.getDouble(cDatabase.QUANTITYHANDLED_NAMESTR);

            if (cPickorder.currentPickOrder.PICK_SHIPPING_QC_CHECK_COUNT()) {
                this.quantityChecked = pvJsonObject.getDouble(cDatabase.QUANTITYCHECKED_NAMESTR);
            }
            else{
                this.quantityChecked = Double.valueOf(0);
            }

            this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW;

            if (this.statusshipping > cWarehouseorder.PackingAndShippingStatusEnu.Needed) {
                this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
            }

            this.storeSourceOrder = pvJsonObject.getString(cDatabase.STORESOURCEORDER_NAMESTR);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}
