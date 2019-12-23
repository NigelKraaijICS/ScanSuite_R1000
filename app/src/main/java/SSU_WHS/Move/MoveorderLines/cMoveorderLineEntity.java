package SSU_WHS.Move.MoveorderLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_MOVEORDERLINE)
public class cMoveorderLineEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    //    @PrimaryKey
    //    @NonNull
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public int lineno;
    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String bincode;
    @ColumnInfo(name = cDatabase.CONTAINER_NAMESTR)
    public String container;
    @ColumnInfo(name = cDatabase.CONTAINERTYPE_NAMESTR)
    public String containerType;
    @ColumnInfo(name = cDatabase.CONTAINERINPUT_NAMESTR)
    public String containerInput;
    @ColumnInfo(name = cDatabase.CONTAINERHANDLED_NAMESTR)
    public String containerHandled;
    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    @ColumnInfo(name = cDatabase.QUANTITYREJECTED_NAMESTR)
    public Double quantityrejected;
    @ColumnInfo(name = cDatabase.HANDLEDTIMESTAMP_NAMESTR)
    public String handledtimestamp;
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    @ColumnInfo(name = cDatabase.DESTINATIONNO_NAMESTR)
    public String destinationno;
    @ColumnInfo(name = cDatabase.ISPARTOFMULTILINEORDER_NAMESTR)
    public String ispartofmultilineorder;
    @ColumnInfo(name = cDatabase.SHIPPINGADVICE_NAMESTR)
    public int shippingadvice;
    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingsequenceno;
    @ColumnInfo(name = cDatabase.PROCESSINGSEQUENCE_NAMESTR)
    public String processingsequenceno;
    @ColumnInfo(name = cDatabase.STORESOURCEORDER_NAMESTR)
    public String storesourceorder;
    @ColumnInfo(name = cDatabase.STORAGEBINCODE_NAMESTR)
    public String storagebincode;
    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemDescription;
    @ColumnInfo(name = cDatabase.PRINTDOCUMENTS_NAMESTR)
    public String printdocuments;
    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    @ColumnInfo(name = cDatabase.ACTIONTYPECODE_NAMESTR)
    public String actiontypecode;
    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public int localstatus;
    @ColumnInfo(name = cDatabase.EXTRAFIELD1_NAMESTR)
    public String extrafield1;
    @ColumnInfo(name = cDatabase.EXTRAFIELD2_NAMESTR)
    public String extrafield2;
    @ColumnInfo(name = cDatabase.EXTRAFIELD3_NAMESTR)
    public String extrafield3;
    @ColumnInfo(name = cDatabase.EXTRAFIELD4_NAMESTR)
    public String extrafield4;
    @ColumnInfo(name = cDatabase.EXTRAFIELD5_NAMESTR)
    public String extrafield5;
    @ColumnInfo(name = cDatabase.EXTRAFIELD6_NAMESTR)
    public String extrafield6;
    @ColumnInfo(name = cDatabase.EXTRAFIELD7_NAMESTR)
    public String extrafield7;
    @ColumnInfo(name = cDatabase.EXTRAFIELD8_NAMESTR)
    public String extrafield8;

    //empty constructor
    public cMoveorderLineEntity() {

    }

    public cMoveorderLineEntity(JSONObject pvJsonObject) {
        try {
            this.lineno = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.container = pvJsonObject.getString(cDatabase.CONTAINER_NAMESTR);
            this.containerType = pvJsonObject.getString(cDatabase.CONTAINERTYPE_NAMESTR);
            this.containerInput = pvJsonObject.getString(cDatabase.CONTAINERINPUT_NAMESTR);
            this.containerHandled = pvJsonObject.getString(cDatabase.CONTAINERHANDLED_NAMESTR);
            this.quantity = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITY_NAMESTR));
            this.quantityhandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));
            this.quantityrejected = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYREJECTED_NAMESTR));
            this.handledtimestamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);
            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.destinationno = pvJsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);
            this.ispartofmultilineorder = pvJsonObject.getString(cDatabase.ISPARTOFMULTILINEORDER_NAMESTR);
            this.shippingadvice = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SHIPPINGADVICE_NAMESTR));
            this.sortingsequenceno = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SORTINGSEQUENCENO_NAMESTR));
            this.processingsequenceno = pvJsonObject.getString(cDatabase.PROCESSINGSEQUENCE_NAMESTR);
            this.storesourceorder = pvJsonObject.getString(cDatabase.STORESOURCEORDER_NAMESTR);
            this.storagebincode = pvJsonObject.getString(cDatabase.STORAGEBINCODE_NAMESTR);
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemDescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.printdocuments = pvJsonObject.getString(cDatabase.PRINTDOCUMENTS_NAMESTR);
            this.status = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.STATUS_NAMESTR));
            this.actiontypecode = pvJsonObject.getString(cDatabase.ACTIONTYPECODE_NAMESTR);
            this.localstatus = cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_NEW;
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

    public int getRecordidInt() {return this.recordid;}
    public int getLineNoInt() {return this.lineno;}
    public String getItemNoStr() {return this.itemno;}
    public String getVariantCodeStr() {return this.variantcode;}
    public String getDescriptionStr() {return this.description;}
    public String getDescription2Str() {return this.description2;}
    public String getBincodeStr() {return this.bincode;}
    public String getContainerStr() {return this.container;}
    public String getContainerTypeStr() {return this.containerType;}
    public String getContainerInput() {return this.containerInput;}
    public String getContainerHandled() {return this.containerHandled;}
    public Double getQuantityDbl() {return this.quantity;}
    public Double getQuantityhandledDbl() {return this.quantityhandled;}
    public Double getQuantityrejectedDbl() {return this.quantityrejected;}
    public String getHandledtimestampStr() {return this.handledtimestamp;}
    public String getSourcenoStr() {return this.sourceno;}
    public String getDestinationNoStr() {return this.destinationno;}
    public String getIspartofMultilineorderStr() {return this.ispartofmultilineorder;}
    public int getShippingadviceInt() {return this.shippingadvice;}
    public int getSortingsequencenoInt() {return this.sortingsequenceno;}
    public String getProcessingSequenceNoStr() {return this.processingsequenceno;}
    public String getStoresourceOrderStr() {return this.storesourceorder;}
    public String getStoragebincodeStr() {return this.storagebincode;}
    public String getVendorItemNoStr() {return this.vendoritemno;}
    public String getVendorItemDescriptionStr() {return this.vendoritemDescription;}
    public String getPrintdocumentsStr() {return this.printdocuments;}
    public int getStatusInt() {return this.status;}
    public String getActiontypecodeStr() {return this.actiontypecode;}
    public int getLocalStatusInt() {return this.localstatus;}


    public String getExtraField1Str() {return this.extrafield1;}
    public String getExtraField2Str() {return this.extrafield2;}
    public String getExtraField3Str() {return this.extrafield3;}
    public String getExtraField4Str() {return this.extrafield4;}
    public String getExtraField5Str() {return this.extrafield5;}
    public String getExtraField6Str() {return this.extrafield6;}
    public String getExtraField7Str() {return this.extrafield7;}
    public String getExtraField8Str() {return this.extrafield8;}
}
