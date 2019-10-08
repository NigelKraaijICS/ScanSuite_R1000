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
//    @PrimaryKey
//    @NonNull
    @ColumnInfo(name = "LineNo")
    public Integer lineno;
    @ColumnInfo(name = "ItemNo")
    public String itemno;
    @ColumnInfo(name = "VariantCode")
    public String variantcode;
    @ColumnInfo(name = "Description")
    public String description;
    @ColumnInfo(name = "Description2")
    public String description2;
    @ColumnInfo(name = "BinCode")
    public String bincode;
    @ColumnInfo(name = "Container")
    public String container;
    @ColumnInfo(name = "ContainerType")
    public String containertype;
    @ColumnInfo(name = "ContainerInput")
    public String containerinput;
    @ColumnInfo(name = "ContainerHandled")
    public String containerhandled;
    @ColumnInfo(name = "Quantity")
    public Double quantity;
    @ColumnInfo(name = "QuantityHandled")
    public Double quantityhandled;
    @ColumnInfo(name = "QuantityRejected")
    public Double quantityrejected;
    @ColumnInfo(name = "SourceNo")
    public String sourceno;
    @ColumnInfo(name = "DestinationNo")
    public String destinationno;
    @ColumnInfo(name = "IsPartOfMultiLineOrder")
    public String ispartofmultilineorder;
    @ColumnInfo(name = "ShippingAdvice")
    public String shippingadvice;
    @ColumnInfo(name = "ProcessingSequence")
    public String processingsequence;
    @ColumnInfo(name = "StoreSourceOpdracht")
    public String storesourceopdracht;
    @ColumnInfo(name = "StorageBinCode")
    public String storagebincode;
    @ColumnInfo(name = "VendorItemNo")
    public String vendoritemno;
    @ColumnInfo(name = "VendorItemDescription")
    public String vendoritemdescription;
    @ColumnInfo(name = "Component10")
    public String component10;
    @ColumnInfo(name = "Brand")
    public String brand;
    @ColumnInfo(name = "PrintDocuments")
    public String printdocuments;
    @ColumnInfo(name = "Status")
    public Integer status;
    @ColumnInfo(name = "StatusShipping")
    public Integer statusShipping;
    @ColumnInfo(name = "StatusPacking")
    public Integer statusPacking;
    @ColumnInfo(name = "LineNoTake")
    public int linenotake;
    @ColumnInfo(name = "QuantityTaken")
    public double quantitytaken;
    @ColumnInfo(name = "TakenTimestamp")
    public String takentimestamp;
    @ColumnInfo(name = "Localstatus")
    public int localstatus;
    @ColumnInfo(name = "LocalSortLocation")
    public String localsortlocation;
    @ColumnInfo(name = "ExtraField1")
    public String extrafield1;
    @ColumnInfo(name = "ExtraField2")
    public String extrafield2;
    @ColumnInfo(name = "ExtraField3")
    public String extrafield3;
    @ColumnInfo(name = "ExtraField4")
    public String extrafield4;
    @ColumnInfo(name = "ExtraField5")
    public String extrafield5;
    @ColumnInfo(name = "ExtraField6")
    public String extrafield6;
    @ColumnInfo(name = "ExtraField7")
    public String extrafield7;
    @ColumnInfo(name = "ExtraField8")
    public String extrafield8;

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
            this.container = pvJsonObject.getString(cDatabase.CONTAINER_NAMESTR);
            this.containertype = pvJsonObject.getString(cDatabase.CONTAINERTYPE_NAMESTR);
            this.containerinput = pvJsonObject.getString(cDatabase.CONTAINERINPUT_NAMESTR);
            this.containerhandled = pvJsonObject.getString(cDatabase.CONTAINERHANDLED_NAMESTR);
            this.quantity = pvJsonObject.getDouble(cDatabase.QUANTITY_NAMESTR);
            this.quantityhandled = pvJsonObject.getDouble(cDatabase.QUANTITYHANDLED_NAMESTR);
            this.quantityrejected = pvJsonObject.getDouble(cDatabase.QUANTITYREJECTED_NAMESTR);

            if (pvPickOrderTypeStr.equalsIgnoreCase(cWarehouseorder.PickOrderTypeEnu.PICK.toString())) {
                this.quantitytaken =  0;
            } else {
                this.quantitytaken =  pvJsonObject.getDouble(cDatabase.QUANTITYTAKEN_NAMESTR);
            }

            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.destinationno = pvJsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);
            this.ispartofmultilineorder = pvJsonObject.getString(cDatabase.ISPARTOFMULTILINEORDER_NAMESTR);
            this.shippingadvice = pvJsonObject.getString(cDatabase.SHIPPINGADVICE_NAMESTR);
            this.processingsequence = pvJsonObject.getString(cDatabase.PROCESSINGSEQUENCE_NAMESTR);
            this.storesourceopdracht = pvJsonObject.getString(cDatabase.STORESOURCEORDER_NAMESTR);
            this.storagebincode = pvJsonObject.getString(cDatabase.STORAGEBINCODE_NAMESTR);
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.component10 = pvJsonObject.getString(cDatabase.COMPONENT10_NAMESTR);

            this.printdocuments = pvJsonObject.getString(cDatabase.PRINTDOCUMENTS_NAMESTR);
            this.status = pvJsonObject.getInt(cDatabase.STATUS_NAMESTR);
            this.statusShipping = pvJsonObject.getInt(cDatabase.STATUSSHIPPING_NAMESTR);
            this.statusPacking = pvJsonObject.getInt(cDatabase.STATUSPACKING_NAMESTR);

            this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW;

            if (this.status != cWarehouseorder.PicklineStatusEnu.Needed) {
                this.localstatus = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
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

    public Integer getRecordidInt() {return this.recordid;}
    public Integer getLineNoInt() {return this.lineno;}
    public String getItemNoStr() {return this.itemno;}
    public String getVariantCodeStr() {return this.variantcode;}
    public String getDescriptionStr() {return this.description;}
    public String getDescription2Str() {return this.description2;}
    public String getBincodeStr() {return this.bincode;}
    public String getContainerStr() {return this.container;}
    public String getContainerTypeStr() {return this.containertype;}
    public String getContainerinputsStr() {return this.containerinput;}
    public String getContainerHandledStr() {return this.containerhandled;}
    public Double getQuantityDbl() {return this.quantity;}
    public Double getQuantityHandledDbl() {return this.quantityhandled;}
    public Double getQuantityRejectedDbl() {return this.quantityrejected;}
    public String getSourceNoStr() {return this.sourceno;}
    public String getDestinationNoStr() {return this.destinationno;}
    public String getIspartOfMultilLneOrderStr() {return this.ispartofmultilineorder;}
    public String getShippingAdviceStr() {return this.shippingadvice;}
    public String getProcessingSequenceStr() {return this.processingsequence;}
    public String getStoreSourceOpdrachtStr() {return this.storesourceopdracht;}
    public String getStorageBinCodeStr() {return this.storagebincode;}
    public String getVendorItemNoStr() {return this.vendoritemno;}
    public String getVendorItemDescriptionStr() {return this.vendoritemdescription;}
    public String getComponent10Str() {return this.component10;}
    public String getBrandStr(){return  this.brand;}
    public String getPrintdocumentsStr() {return this.printdocuments;}
    public int getStatusInt() {return this.status;}
    public int getStatusShippingInt() {return this.statusShipping;}
    public int getStatusPackingInt() {return this.statusPacking;}
    public int getLinenoTakeInt() {return this.linenotake;}
    public Double getQuantityTakenDbl() {return this.quantitytaken;}
    public String getTakenTimestampStr() {return this.takentimestamp;}
    public int getLocalstatusInt() {return this.localstatus;}
    public String getLocalSortLocationStr() {return this.localsortlocation;}
    public String getExtraField1Str() {return this.extrafield1;}
    public String getExtraField2Str() {return this.extrafield2;}
    public String getExtraField3Str() {return this.extrafield3;}
    public String getExtraField4Str() {return this.extrafield4;}
    public String getExtraField5Str() {return this.extrafield5;}
    public String getExtraField6Str() {return this.extrafield6;}
    public String getExtraField7Str() {return this.extrafield7;}
    public String getExtraField8Str() {return this.extrafield8;}
}
