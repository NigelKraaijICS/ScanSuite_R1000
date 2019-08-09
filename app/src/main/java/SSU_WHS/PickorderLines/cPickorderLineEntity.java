package SSU_WHS.PickorderLines;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.cDatabase;
import SSU_WHS.cPublicDefinitions;

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
    public String quantityrejected;
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
    @ColumnInfo(name = "LineNoTake")
    public String linenotake;
    @ColumnInfo(name = "QuantityTaken")
    public String quantitytaken;
    @ColumnInfo(name = "TakenTimestamp")
    public String takentimestamp;
    @ColumnInfo(name = "Localstatus")
    public Integer localstatus;
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
    public enum pickOrderTypeEnu {
        PICK,
        SORT
    }

    public cPickorderLineEntity(JSONObject jsonObject,
                                String pickOrderType,
                                final String ExtraField1,
                                final String ExtraField2,
                                final String ExtraField3,
                                final String ExtraField4,
                                final String ExtraField5,
                                final String ExtraField6,
                                final String ExtraField7,
                                final String ExtraField8) {
        try {
            lineno = jsonObject.getInt(cDatabase.LINENO_NAMESTR);
            itemno = jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            variantcode = jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            description2 = jsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            bincode = jsonObject.getString(cDatabase.BINCODE_NAMESTR);
            container = jsonObject.getString(cDatabase.CONTAINER_NAMESTR);
            containertype = jsonObject.getString(cDatabase.CONTAINERTYPE_NAMESTR);
            containerinput = jsonObject.getString(cDatabase.CONTAINERINPUT_NAMESTR);
            containerhandled = jsonObject.getString(cDatabase.CONTAINERHANDLED_NAMESTR);
            quantity = jsonObject.getDouble(cDatabase.QUANTITY_NAMESTR);
            quantityhandled = jsonObject.getDouble(cDatabase.QUANTITYHANDLED_NAMESTR);
            quantityrejected = jsonObject.getString(cDatabase.QUANTITYREJECTED_NAMESTR);
            sourceno = jsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            destinationno = jsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);
            ispartofmultilineorder = jsonObject.getString(cDatabase.ISPARTOFMULTILINEORDER_NAMESTR);
            shippingadvice = jsonObject.getString(cDatabase.SHIPPINGADVICE_NAMESTR);
            processingsequence = jsonObject.getString(cDatabase.PROCESSINGSEQUENCE_NAMESTR);
            storesourceopdracht = jsonObject.getString(cDatabase.STORESOURCEORDER_NAMESTR);
            storagebincode = jsonObject.getString(cDatabase.STORAGEBINCODE_NAMESTR);
            vendoritemno = jsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            vendoritemdescription = jsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            component10 = jsonObject.getString(cDatabase.COMPONENT10_NAMESTR);
            //brand = jsonObject.getString(cDatabase.BRAND_NAMESTR);
            printdocuments = jsonObject.getString(cDatabase.PRINTDOCUMENTS_NAMESTR);
            status = jsonObject.getInt(cDatabase.STATUS_NAMESTR);
            if (pickOrderType.equalsIgnoreCase(pickOrderTypeEnu.PICK.toString())) {
                switch (status) {
                    case cPickorderLine.STATUS_STEP1:
                        localstatus = cPickorderLine.LOCALSTATUS_NEW;
                        break;
                    case cPickorderLine.STATUS_STEP1_TOREPORT:
                        localstatus = cPickorderLine.LOCALSTATUS_DONE_SENT;
                        break;
                    default:
                        localstatus = cPickorderLine.LOCALSTATUS_DONE_SENT;
                        break;
                }
            }
            else if (pickOrderType.equalsIgnoreCase(pickOrderTypeEnu.SORT.toString())) {
                linenotake = jsonObject.getString(cDatabase.LINENOTAKE_NAMESTR);
                quantitytaken = jsonObject.getString(cDatabase.QUANTITYTAKEN_NAMESTR);
                takentimestamp = jsonObject.getString(cDatabase.TAKENTIMESTAMP_NAMESTR);
                //localstatus = cPickorderLine.LOCALSTATUS_NEW;
                switch (status) {
                    case cPickorderLine.STATUS_STEP1:
                        localstatus = cPickorderLine.LOCALSTATUS_NEW;
                        break;
                    case cPickorderLine.STATUS_STEP1_TOREPORT:
                        localstatus = cPickorderLine.LOCALSTATUS_DONE_SENT;
                        break;
                    default:
                        localstatus = cPickorderLine.LOCALSTATUS_DONE_SENT;
                        break;
                }
            }
            //region extrafield1
            if (!ExtraField1.trim().isEmpty()) {
                try {
                    extrafield1 = jsonObject.getString(ExtraField1);
                }
                catch (JSONException e) {
                    extrafield1 = "";
                }
            }
            else {
                extrafield1 = "";
            }
            //endregion extrafield1

            //region extrafield2
            if (!ExtraField2.trim().isEmpty()) {
                try {
                    extrafield2 = jsonObject.getString(ExtraField2);
                }
                catch (JSONException e) {
                    extrafield2 = "";
                }
            }
            else {
                extrafield2 = "";
            }
            //endregion extrafield2

            //region extrafield3
            if (!ExtraField3.trim().isEmpty()) {
                try {
                    extrafield3 = jsonObject.getString(ExtraField3);
                }
                catch (JSONException e) {
                    extrafield3 = "";
                }
            }
            else {
                extrafield3 = "";
            }
            //endregion extrafield3

            //region extrafield4
            if (!ExtraField4.trim().isEmpty()) {
                try {
                    extrafield4 = jsonObject.getString(ExtraField4);
                }
                catch (JSONException e) {
                    extrafield4 = "";
                }
            }
            else {
                extrafield4 = "";
            }
            //endregion extrafield4

            //region extrafield5
            if (!ExtraField5.trim().isEmpty()) {
                try {
                    extrafield5 = jsonObject.getString(ExtraField5);
                }
                catch (JSONException e) {
                    extrafield5 = "";
                }
            }
            else {
                extrafield5 = "";
            }
            //endregion extrafield5

            //region extrafield6
            if (!ExtraField6.trim().isEmpty()) {
                try {
                    extrafield6 = jsonObject.getString(ExtraField6);
                }
                catch (JSONException e) {
                    extrafield6 = "";
                }
            }
            else {
                extrafield6 = "";
            }
            //endregion extrafield6

            //region extrafield7
            if (!ExtraField7.trim().isEmpty()) {
                try {
                    extrafield7 = jsonObject.getString(ExtraField7);
                }
                catch (JSONException e) {
                    extrafield7 = "";
                }
            }
            else {
                extrafield7 = "";
            }
            //endregion extrafield7

            //region extrafield8
            if (!ExtraField8.trim().isEmpty()) {
                try {
                    extrafield8 = jsonObject.getString(ExtraField8);
                }
                catch (JSONException e) {
                    extrafield8 = "";
                }
            }
            else {
                extrafield8 = "";
            }
            //endregion extrafield8


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer getRecordid() {return this.recordid;}
    public Integer getLineNo() {return this.lineno;}
    public String getItemno() {return this.itemno;}
    public String getVariantcode() {return this.variantcode;}
    public String getDescription() {return this.description;}
    public String getDescription2() {return this.description2;}
    public String getBincode() {return this.bincode;}
    public String getContainer() {return this.container;}
    public String getContainertype() {return this.containertype;}
    public String getContainerinput() {return this.containerinput;}
    public String getContainerhandled() {return this.containerhandled;}
    public Double getQuantity() {return this.quantity;}
    public Double getQuantityhandled() {return this.quantityhandled;}
    public String getQuantityrejected() {return this.quantityrejected;}
    public String getSourceno() {return this.sourceno;}
    public String getDestinationno() {return this.destinationno;}
    public String getIspartofmultilineorder() {return this.ispartofmultilineorder;}
    public String getShippingadvice() {return this.shippingadvice;}
    public String getProcessingsequence() {return this.processingsequence;}
    public String getStoresourceopdracht() {return this.storesourceopdracht;}
    public String getStoragebincode() {return this.storagebincode;}
    public String getVendoritemno() {return this.vendoritemno;}
    public String getVendoritemdescription() {return this.vendoritemdescription;}
    public String getComponent10() {return this.component10;}
    //public String getBrand() {return this.brand;}
    public String getPrintdocuments() {return this.printdocuments;}
    public Integer getStatus() {return this.status;}
    public String getLinenotake() {return this.linenotake;}
    public String getQuantitytaken() {return this.quantitytaken;}
    public String getTakentimestamp() {return this.takentimestamp;}
    public Integer getLocalstatus() {return this.localstatus;}
    public String getLocalSortLocation() {return this.localsortlocation;}
    public String getExtrafield1() {return this.extrafield1;}
    public String getExtrafield2() {return this.extrafield2;}
    public String getExtrafield3() {return this.extrafield3;}
    public String getExtrafield4() {return this.extrafield4;}
    public String getExtrafield5() {return this.extrafield5;}
    public String getExtrafield6() {return this.extrafield6;}
    public String getExtrafield7() {return this.extrafield7;}
    public String getExtrafield8() {return this.extrafield8;}
}
