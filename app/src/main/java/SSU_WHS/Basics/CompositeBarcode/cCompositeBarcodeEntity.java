package SSU_WHS.Basics.CompositeBarcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_COMPOSITEBARCODE)

public class cCompositeBarcodeEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.COMPOSITEBARCODE_NAMESTR)
    public String compositeBarcodeStr = "";
    public String getCompositeBarcodeStr() { return compositeBarcodeStr;}

    @ColumnInfo(name = cDatabase.COMPOSITEBARCODETYPE_NAMESTR)
    public String compositeBarcodeTypeStr;
    public String getCompositeBarcodeTypeStr() { return compositeBarcodeTypeStr;}

    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String descriptionStr;
    public String getDescriptionStr() { return descriptionStr;}

    @ColumnInfo(name = cDatabase.FIELDSEPERATOR_NAMESTR)
    public String fieldSeperatorStr;
    public String getFieldSeperatorStr() { return fieldSeperatorStr;}

    @ColumnInfo(name = cDatabase.LAYOUTREGEX_NAMESTR)
    public String layoutRegexStr;
    public String getLayoutRegexStr() { return layoutRegexStr;}

    @ColumnInfo(name = cDatabase.LAYOUTTYPE_NAMESTR)
    public String layoutTypeStr;
    public String getLayoutTypeStr() { return layoutTypeStr;}

    //End Region Public Properties

    //Region Constructor
    public cCompositeBarcodeEntity() {

    }

    public cCompositeBarcodeEntity(JSONObject jsonObject) {
        try {
            this.compositeBarcodeStr = jsonObject.getString(cDatabase.COMPOSITEBARCODESPLITTED_NAMESTR);
            this.compositeBarcodeTypeStr = jsonObject.getString(cDatabase.COMPOSITEBARCODETYPE_NAMESTR);
            this.descriptionStr = jsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.fieldSeperatorStr = jsonObject.getString(cDatabase.FIELDSEPERATOR_NAMESTR);
            this.layoutRegexStr = jsonObject.getString(cDatabase.LAYOUTREGEX_NAMESTR);
            this.layoutTypeStr = jsonObject.getString(cDatabase.LAYOUTTYPE_NAMESTR);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}
