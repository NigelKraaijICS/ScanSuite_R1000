package SSU_WHS.Inventory.InventoryorderLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INVENTORYORDERLINE)
public class cInventoryorderLineEntity {
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
    @ColumnInfo(name = cDatabase.ITEMTYPE_NAMESTR)
    public String itemtype;
    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String bincode;
    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemdescription;
    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingsequenceno;
    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    @ColumnInfo(name = cDatabase.SOURCETYPE_NAMESTR)
    public int sourcetype;
    @ColumnInfo(name = cDatabase.HANDLEDTIMESTAMP_NAMESTR)
    public String handledtimestamp;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLEDALLSCANNERS_NAMESTR)
    public Double quantityhandledAllScanners;
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
    public cInventoryorderLineEntity() {

    }

    public cInventoryorderLineEntity(JSONObject pvJsonObject) {
        try {
            this.lineno = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.itemtype = pvJsonObject.getString(cDatabase.ITEMTYPE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.quantity = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITY_NAMESTR));
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.sortingsequenceno = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
            this.status = pvJsonObject.getInt(cDatabase.STATUS_NAMESTR);
            this.sourcetype = pvJsonObject.getInt(cDatabase.SOURCETYPE_NAMESTR);
            this.handledtimestamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);
            this.quantityhandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR)) ;
            this.quantityhandledAllScanners = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLEDALLSCANNERS_NAMESTR));

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

    public int getLineNoInt() {return this.lineno;}
    public String getItemNoStr() {return this.itemno;}
    public String getVariantCodeStr() {return this.variantcode;}
    public String getDescriptionStr() {return this.description;}
    public String getDescription2Str() {return this.description2;}
    public String getBincodeStr() {return this.bincode;}
    public Double getQuantityDbl() {return this.quantity;}
    public String getVendorItemNoStr() {return this.vendoritemno;}
    public String getVendorItemDescriptionStr() {return this.vendoritemdescription;}
    public int getStatusInt() {return this.status;}
    public int getSourceTypeInt() {return this.sourcetype;}
    public String getHandledtimestampStr() {return this.handledtimestamp;}
    public Double getQuantityHandledDbl() {return this.quantityhandled;}
    public Double getQuantityHandledAllScannersDbl() {return this.quantityhandledAllScanners;}
    public String getExtraField1Str() {return this.extrafield1;}
    public String getExtraField2Str() {return this.extrafield2;}
    public String getExtraField3Str() {return this.extrafield3;}
    public String getExtraField4Str() {return this.extrafield4;}
    public String getExtraField5Str() {return this.extrafield5;}
    public String getExtraField6Str() {return this.extrafield6;}
    public String getExtraField7Str() {return this.extrafield7;}
    public String getExtraField8Str() {return this.extrafield8;}
}
