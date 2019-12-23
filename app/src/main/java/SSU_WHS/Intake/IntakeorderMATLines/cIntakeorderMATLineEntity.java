package SSU_WHS.Intake.IntakeorderMATLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INTAKEORDERMATLINES)
public class cIntakeorderMATLineEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public Integer lineNoInt;

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemNoStr;

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantCodeStr;

    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String descriptionStr;

    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2Str;

    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String binCodeStr;

    @ColumnInfo(name = cDatabase.BINCODEHANDLED_NAMESTR)
    public String binCodehandledStr;

    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantityDbl;

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityHandledDbl;

    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceNoStr;

    @ColumnInfo(name = cDatabase.DESTINATIONNO_NAMESTR)
    public String destinationNoStr;

    @ColumnInfo(name = cDatabase.ISPARTOFMULTILINEORDER_NAMESTR)
    public String isPartOfMultilineOrderStr;

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingSequenceNoStr;

    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendorItemNoStr;

    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendorItemDescriptionStr;

    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public Integer statusInt;

    @ColumnInfo(name = cDatabase.ACTIONTYPECODE_NAMESTR)
    public String actionTypeCodeStr;

    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public int localStatusInt;

    @ColumnInfo(name = cDatabase.EXTRAFIELD1_NAMESTR)
    public String extraField1Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD2_NAMESTR)
    public String extraField2Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD3_NAMESTR)
    public String extraField3Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD4_NAMESTR)
    public String extraField4Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD5_NAMESTR)
    public String extraField5Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD6_NAMESTR)
    public String extraField6Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD7_NAMESTR)
    public String extraField7Str;

    @ColumnInfo(name = cDatabase.EXTRAFIELD8_NAMESTR)
    public String extraField8Str;

    @ColumnInfo(name = cDatabase.SOURCETYPE_NAMESTR)
    public Integer sourceTypeInt;


    //empty constructor
    public cIntakeorderMATLineEntity() {

    }

    public cIntakeorderMATLineEntity(JSONObject pvJsonObject) {
        try {
            this.lineNoInt = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.itemNoStr = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantCodeStr = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.descriptionStr = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2Str = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.binCodeStr = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.binCodehandledStr = pvJsonObject.getString(cDatabase.BINCODEHANDLED_NAMESTR);
            this.quantityDbl = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITY_NAMESTR));
            this.quantityHandledDbl = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));
            this.sourceNoStr = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.destinationNoStr = pvJsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);
            this.isPartOfMultilineOrderStr = pvJsonObject.getString(cDatabase.ISPARTOFMULTILINEORDER_NAMESTR);
            this.sortingSequenceNoStr = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SORTINGSEQUENCENO_NAMESTR));
            this.vendorItemNoStr = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendorItemDescriptionStr = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.statusInt = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.STATUS_NAMESTR));
            this.actionTypeCodeStr = pvJsonObject.getString(cDatabase.ACTIONTYPECODE_NAMESTR);
            this.localStatusInt =  cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_NEW;
            this.sourceTypeInt = pvJsonObject.getInt(cDatabase.SOURCETYPE_NAMESTR);

            if (this.getQuantityHandledDbl() >= this.getQuantityDbl()) {
                this.localStatusInt = cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
            }

            //region extraField1Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD1().trim().isEmpty()) {
                try {
                    this.extraField1Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD1());
                }
                catch (JSONException e) {
                    this.extraField1Str = "";
                }
            }
            else {
                this.extraField1Str = "";
            }
            //endregion extraField1Str

            //region extraField2Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD2().trim().isEmpty()) {
                try {
                    this.extraField2Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD2());
                }
                catch (JSONException e) {
                    this.extraField2Str = "";
                }
            }
            else {
                this.extraField2Str = "";
            }
            //endregion extraField2Str

            //region extraField3Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD3().trim().isEmpty()) {
                try {
                    this.extraField3Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD3());
                }
                catch (JSONException e) {
                    this.extraField3Str = "";
                }
            }
            else {
                this.extraField3Str = "";
            }
            //endregion extraField3Str

            //region extraField4Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD4().trim().isEmpty()) {
                try {
                    this.extraField4Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD4());
                }
                catch (JSONException e) {
                    this.extraField4Str = "";
                }
            }
            else {
                this.extraField4Str = "";
            }
            //endregion extraField4Str

            //region extraField5Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD5().trim().isEmpty()) {
                try {
                    this.extraField5Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD5());
                }
                catch (JSONException e) {
                    this.extraField5Str = "";
                }
            }
            else {
                this.extraField5Str = "";
            }
            //endregion extraField5Str

            //region extraField6Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD6().trim().isEmpty()) {
                try {
                    this.extraField6Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD6());
                }
                catch (JSONException e) {
                    this.extraField6Str = "";
                }
            }
            else {
                this.extraField6Str = "";
            }
            //endregion extraField6Str

            //region extraField7Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD7().trim().isEmpty()) {
                try {
                    this.extraField7Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD7());
                }
                catch (JSONException e) {
                    this.extraField7Str = "";
                }
            }
            else {
                this.extraField7Str = "";
            }
            //endregion extraField7Str

            //region extraField8Str
            if (!cSetting.GENERIC_ITEM_EXTRA_FIELD8().trim().isEmpty()) {
                try {
                    this.extraField8Str = pvJsonObject.getString(cSetting.GENERIC_ITEM_EXTRA_FIELD8());
                }
                catch (JSONException e) {
                    this.extraField8Str = "";
                }
            }
            else {
                this.extraField8Str = "";
            }
            //endregion extraField8Str


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getRecordidInt() {return this.recordid;}
    public int getLineNoInt() {return this.lineNoInt;}
    public String getItemNoStr() {return this.itemNoStr;}
    public String getVariantCodeStr() {return this.variantCodeStr;}
    public String getDescriptionStr() {return this.descriptionStr;}
    public String getDescription2Str() {return this.description2Str;}
    public String getBincodeStr() {return this.binCodeStr;}
    public String getBincodehandledStr() {return this.binCodehandledStr;}
    public Double getQuantityDbl() {return this.quantityDbl;}
    public Double getQuantityHandledDbl() {return this.quantityHandledDbl;}
    public String getSourceNoStr() {return this.sourceNoStr;}
    public String getDestinationNoStr() {return this.destinationNoStr;}
    public String getIspartOfMultilLneOrderStr() {return this.isPartOfMultilineOrderStr;}
    public int getSortingSequenceStr() {return this.sortingSequenceNoStr;}
    public String getVendorItemNoStr() {return this.vendorItemNoStr;}
    public String getVendorItemDescriptionStr() {return this.vendorItemDescriptionStr;}
    public int getStatusInt() {return this.statusInt;}
    public int getLocalStatusInt() {return this.localStatusInt;}
    public String getActionTypeStr() {return this.actionTypeCodeStr;}

    public String getExtraField1Str() {return this.extraField1Str;}
    public String getExtraField2Str() {return this.extraField2Str;}
    public String getExtraField3Str() {return this.extraField3Str;}
    public String getExtraField4Str() {return this.extraField4Str;}
    public String getExtraField5Str() {return this.extraField5Str;}
    public String getExtraField6Str() {return this.extraField6Str;}
    public String getExtraField7Str() {return this.extraField7Str;}
    public String getExtraField8Str() {return this.extraField8Str;}

    public int getSourceTypeInt() {return this.sourceTypeInt;}
}
