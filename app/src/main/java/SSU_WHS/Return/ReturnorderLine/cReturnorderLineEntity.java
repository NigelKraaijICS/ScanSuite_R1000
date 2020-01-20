package SSU_WHS.Return.ReturnorderLine;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;

@Entity(tableName= cDatabase.TABLENAME_RETURNORDERLINE)
public class cReturnorderLineEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    public String getItemNoStr() {return this.itemno;}

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name = cDatabase.DOCUMENT_NAMESTR)
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name = cDatabase.RETOURREDEN_NAMESTR)
    public String retourreden;
    public String getRetourreden() {return this.retourreden;}

    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    public String getDescriptionStr() {return this.description;}

    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    public String getDescription2Str() {return this.description2;}

    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    public String getVendorItemNoStr() {return this.vendoritemno;}

    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemdescription;
    public String getVendorItemDescriptionStr() {return this.vendoritemdescription;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENOTAKE_NAMESTR)
    public int sortingsequenceno;
    public int getSortingsequencenoInt() {return this.sortingsequenceno;}

    @ColumnInfo(name = cDatabase.QUANTITYTAKE_NAMESTR)
    public Double quantitytake;
    public Double getQuantitytakeDbl() {return  this.quantitytake;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLEDTAKE_NAMESTR)
    public Double quantityHandledtake;
    public Double getQuantityHandledtake() {return  this.quantityHandledtake;}

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

    @ColumnInfo(name = cDatabase.GENERATED_NAMESTR)
    public boolean generated;
    public boolean getGeneratedBln() {return this.generated;}

    public cReturnorderLineEntity(cArticle pvArticle){
        int sortingSequenceInt;

        if (cReturnorderLine.allLinesObl == null){
            sortingSequenceInt = 1;
        }
        else {
            sortingSequenceInt = 1 + cReturnorderLine.allLinesObl.size();
        }

        this.itemno = pvArticle.getItemNoStr();
        this.variantcode = pvArticle.getVariantCodeStr();
        this.document = cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr();
        if (cBranchReason.currentBranchReason != null){
            this.retourreden = cBranchReason.currentBranchReason.getDescriptionStr();
        }
        else {
            this.retourreden = "";
        }

        this.description = pvArticle.getDescriptionStr();
        this.description2 = pvArticle.getDescription2Str();
        this.vendoritemno = pvArticle.getVendorItemNoStr();
        this.vendoritemdescription = "";
        this.sortingsequenceno = sortingSequenceInt ;
        this.quantitytake = 0.0;
        this.quantityHandledtake = 0.0;
        this.extrafield1 = "";
        this.extrafield2 = "";
        this.extrafield3 = "";
        this.extrafield4 = "";
        this.extrafield5 = "";
        this.extrafield6 = "";
        this.extrafield7 = "";
        this.extrafield8 = "";
        this.generated = true;

    }

    public cReturnorderLineEntity(JSONObject pvJsonObject) {
        try {
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.retourreden = pvJsonObject.getString(cDatabase.RETOURREDEN_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.sortingsequenceno = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENOTAKE_NAMESTR);
            this.quantitytake = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYTAKE_NAMESTR));
            this.quantityHandledtake = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLEDTAKE_NAMESTR));
            this.generated = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.GENERATED_NAMESTR),false);
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

public cReturnorderLineEntity(){

}


}
