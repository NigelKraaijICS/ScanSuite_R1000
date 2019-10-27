package SSU_WHS.Basics.Article;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_ARTICLE, primaryKeys = {"Itemno", "Variantcode"})
public class cArticleEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name="Itemno")
    public String itemno;
    public String getItemnoStr() {return this.itemno;}

    @NonNull
    @ColumnInfo(name="Variantcode")
    public String variantcode;
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name="Description")
    public String description;
    public String getDescriptionStr() {
        return description;
    }

    @ColumnInfo(name="Description2")
    public String description2;
    public String getDescription2Str() {
        return description2;
    }

    @ColumnInfo(name="ItemInfoCode")
    public String itemInfoCode;
    public String getItemInfoCodeStr() {
        return itemInfoCode;
    }

    @ColumnInfo(name="VendorItemNo")
    public String vendorItemNoStr;
    public String getVendorItemNoStr() {
        return vendorItemNoStr;
    }

    @ColumnInfo(name="VendorItemDescription")
    public String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    @ColumnInfo(name="Component10")
    public String component10Str;
    public String getComponent10Str() {
        return component10Str;
    }

    @ColumnInfo(name="Prince")
    public Double priceDbl;
    public Double getPriceDbl() {
        return priceDbl;
    }


    //End Region Public Properies

    //Region Constructor
    public cArticleEntity() {

    }
    public cArticleEntity(JSONObject pvJsonObject) {
        try {
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.itemInfoCode = pvJsonObject.getString(cDatabase.ITEMINFOCODE_NAMESTR);
            this.vendorItemNoStr = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendorItemDescriptionStr = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.component10Str = pvJsonObject.getString(cDatabase.COMPONENT10_NAMESTR);
            this.priceDbl = pvJsonObject.getDouble(cDatabase.PRICE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor


}
