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
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public int lineno;
    public int getLineNoInt() {return this.lineno;}

    @ColumnInfo(name = cDatabase.ACTIONTYPECODE_NAMESTR)
    public String actiontypecode;
    public String getActiontypecodeStr() {return this.actiontypecode;}

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
    public double quantity;
    public double getQuantityDbl() {return this.quantity;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    public Double getQuantityhandledDbl() {return this.quantityhandled;}

    @ColumnInfo(name = cDatabase.HANDLEDTIMESTAMP_NAMESTR)
    public String handledtimestamp;
    public String getHandledtimestampStr() {return this.handledtimestamp;}

   @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    public String getSourcenoStr() {return this.sourceno;}

    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    public int getStatusInt() {return this.status;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingSequenceNo;
    public int getSortingSequenceNoInt() {return this.sortingSequenceNo;}

    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public int localstatus;
    public int getLocalStatusInt() {return this.localstatus;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD1_NAMESTR)
    public String extrafield1;
    public String getExtraField1Str() {return this.extrafield1;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD2_NAMESTR)
    public String extrafield2;
    public String getExtraField2Str() {return this.extrafield2;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD3_NAMESTR)
    public String extrafield3;
    public String getExtraField3Str() {return this.extrafield3;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD4_NAMESTR)
    public String extrafield4;
    public String getExtraField4Str() {return this.extrafield4;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD5_NAMESTR)
    public String extrafield5;
    public String getExtraField5Str() {return this.extrafield5;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD6_NAMESTR)
    public String extrafield6;
    public String getExtraField6Str() {return this.extrafield6;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD7_NAMESTR)
    public String extrafield7;
    public String getExtraField7Str() {return this.extrafield7;}

    @ColumnInfo(name = cDatabase.EXTRAFIELD8_NAMESTR)
    public String extrafield8;
    public String getExtraField8Str() {return this.extrafield8;}

    @ColumnInfo(name = cDatabase.LOCAL_QUANTITYTAKEN_NAMESTR)
    public double quantityTaken;
    public double getQuantityTakenDbl() { return quantityTaken; }

    @ColumnInfo(name = cDatabase.LOCAL_QUANTITYPLACED_NAMESTR)
    public double quantityPlaced;
    public double getQuantityPlacedDbl() { return quantityPlaced; }

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

            this.quantity = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITY_NAMESTR));
            this.quantityhandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));

            this.handledtimestamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);
            this.sourceno = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            this.status = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.STATUS_NAMESTR));
            this.sortingSequenceNo = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SORTINGSEQUENCENO_NAMESTR));
            this.actiontypecode = pvJsonObject.getString(cDatabase.ACTIONTYPECODE_NAMESTR);
            this.localstatus = cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_NEW;
            this.quantityTaken =  cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYTAKEN_NAMESTR));
            this.quantityPlaced =  cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.LOCAL_QUANTITYPLACED_NAMESTR));

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
