package SSU_WHS.Picken.PickorderLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName="Pickorderlines")
public class cPickorderLineEntity {


    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    public Integer getRecordidInt() {return this.recordid;}

    @ColumnInfo(name = "LineNo")
    public Integer lineno;
    public Integer getLineNoInt() {return this.lineno;}

    @ColumnInfo(name = "ItemNo")
    public String itemno;
    public String getItemNoStr() {return this.itemno;}

    @ColumnInfo(name = "VariantCode")
    public String variantcode;
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name = "Description")
    public String description;
    public String getDescriptionStr() {return this.description;}

    @ColumnInfo(name = "Description2")
    public String description2;
    public String getDescription2Str() {return this.description2;}

    @ColumnInfo(name = "BinCode")
    public String bincode;
    public String getBincodeStr() {return this.bincode;}

    @ColumnInfo(name = "Quantity")
    public Double quantity;
    public Double getQuantityDbl() {return this.quantity;}

    @ColumnInfo(name = "QuantityHandled")
    public Double quantityhandled;
    public Double getQuantityHandledDbl() {return this.quantityhandled;}

    @ColumnInfo(name = "QuantityRejected")
    public Double quantityRejected;
    public Double getQuantityRejected() {return this.quantityRejected;}

    @ColumnInfo(name = "QuantityChecked")
    public Double quantityChecked;
    public Double getQuantityChecked() {return this.quantityChecked;}

    @ColumnInfo(name = "SourceNo")
    public String sourceno;
    public String getSourceNoStr() {return this.sourceno;}

    @ColumnInfo(name = "DestinationNo")
    public String destinationno;
    public String getDestinationNoStr() {return this.destinationno;}

    @ColumnInfo(name = "ProcessingSequence")
    public String processingsequence;
    public String getProcessingSequenceStr() {return this.processingsequence;}

    @ColumnInfo(name = "VendorItemNo")
    public String vendoritemno;
    public String getVendorItemNoStr() {return this.vendoritemno;}

    @ColumnInfo(name = "VendorItemDescription")
    public String vendoritemdescription;
    public String getVendorItemDescriptionStr() {return this.vendoritemdescription;}

    @ColumnInfo(name = "DeliveryAddressCode")
    public String deliveryAdressCode;
    public String getDeliveryAdressCode() {return this.deliveryAdressCode;}

    @ColumnInfo(name = "ShippingAgentCode")
    public String shippingAgentCode;
    public String getShippingAgentCodeStr() {return this.shippingAgentCode;}

    @ColumnInfo(name = "ShippingAgentServiceCode")
    public String shippingAgentServiceCode;
    public String getShippingAgentServiceCodeStr() {return this.shippingAgentServiceCode;}


    @ColumnInfo(name = "Status")
    public Integer status;
    public int getStatusInt() {return this.status;}

    @ColumnInfo(name = "StatusShipping")
    public Integer statusShipping;
    public int getStatusShippingInt() {return this.statusShipping;}

    @ColumnInfo(name = "StatusPacking")
    public Integer statusPacking;
    public int getStatusPackingInt() {return this.statusPacking;}

    @ColumnInfo(name = "QuantityTaken")
    public double quantitytaken;
    public Double getQuantityTakenDbl() {return this.quantitytaken;}

    @ColumnInfo(name = "TakenTimeStamp")
    public String takenTimeStamp;
    public String getTakenTimeStampStr() {return this.takenTimeStamp;}

    @ColumnInfo(name = "Localstatus")
    public int localstatus;
    public int getLocalstatusInt() {return this.localstatus;}

    @ColumnInfo(name = "ExtraField1")
    public String extrafield1;
    public String getExtraField1Str() {return this.extrafield1;}

    @ColumnInfo(name = "ExtraField2")
    public String extrafield2;
    public String getExtraField2Str() {return this.extrafield2;}

    @ColumnInfo(name = "ExtraField3")
    public String extrafield3;
    public String getExtraField3Str() {return this.extrafield3;}

    @ColumnInfo(name = "ExtraField4")
    public String extrafield4;
    public String getExtraField4Str() {return this.extrafield4;}

    @ColumnInfo(name = "ExtraField5")
    public String extrafield5;
    public String getExtraField5Str() {return this.extrafield5;}

    @ColumnInfo(name = "ExtraField6")
    public String extrafield6;
    public String getExtraField6Str() {return this.extrafield6;}

    @ColumnInfo(name = "ExtraField7")
    public String extrafield7;
    public String getExtraField7Str() {return this.extrafield7;}

    @ColumnInfo(name = "ExtraField8")
    public String extrafield8;
    public String getExtraField8Str() {return this.extrafield8;}

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

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.PICK.toString()) || pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.QC.toString())  ) {
                this.quantitytaken =  0;
            } else {
                this.quantitytaken =  pvJsonObject.getDouble(cDatabase.QUANTITYTAKEN_NAMESTR);
            }

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.QC.toString())  ) {
                this.quantityChecked =  pvJsonObject.getDouble(cDatabase.QUANTITYCHECKED_NAMESTR);
                this.deliveryAdressCode = pvJsonObject.getString(cDatabase.DELIVERYADDRESSCODE_NAMESTR);
                this.shippingAgentCode = pvJsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
                this.shippingAgentServiceCode = pvJsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
            } else {
                this.quantitytaken =  0;
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

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.QC.toString())) {
                if (this.getQuantityChecked().equals(this.getQuantityDbl())) {
                        this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
                }
            }

            //region extraField1Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD1().trim().isEmpty()) {
                try {
                    this.extrafield1 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD1());
                }
                catch (JSONException e) {
                    this.extrafield1 = "";
                }
            }
            else {
                this.extrafield1 = "";
            }
            //endregion extraField1Str

            //region extraField2Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD2().trim().isEmpty()) {
                try {
                    this.extrafield2 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD2());
                }
                catch (JSONException e) {
                    this.extrafield2 = "";
                }
            }
            else {
                this.extrafield2 = "";
            }
            //endregion extraField2Str

            //region extraField3Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD3().trim().isEmpty()) {
                try {
                    this.extrafield3 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD3());
                }
                catch (JSONException e) {
                    this.extrafield3 = "";
                }
            }
            else {
                this.extrafield3 = "";
            }
            //endregion extraField3Str

            //region extraField4Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD4().trim().isEmpty()) {
                try {
                    this.extrafield4 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD4());
                }
                catch (JSONException e) {
                    this.extrafield4 = "";
                }
            }
            else {
                this.extrafield4 = "";
            }
            //endregion extraField4Str

            //region extraField5Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD5().trim().isEmpty()) {
                try {
                    this.extrafield5 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD5());
                }
                catch (JSONException e) {
                    this.extrafield5 = "";
                }
            }
            else {
                this.extrafield5 = "";
            }
            //endregion extraField5Str

            //region extraField6Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD6().trim().isEmpty()) {
                try {
                    this.extrafield6 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD6());
                }
                catch (JSONException e) {
                    this.extrafield6 = "";
                }
            }
            else {
                this.extrafield6 = "";
            }
            //endregion extraField6Str

            //region extraField7Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD7().trim().isEmpty()) {
                try {
                    this.extrafield7 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD7());
                }
                catch (JSONException e) {
                    this.extrafield7 = "";
                }
            }
            else {
                this.extrafield7 = "";
            }
            //endregion extraField7Str

            //region extraField8Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD8().trim().isEmpty()) {
                try {
                    this.extrafield8 = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD8());
                }
                catch (JSONException e) {
                    this.extrafield8 = "";
                }
            }
            else {
                this.extrafield8 = "";
            }
            //endregion extraField8Str



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
