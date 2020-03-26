package SSU_WHS.Inventory.InventoryorderBarcodes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INVENTORYORDERBARCODE)
public class cInventoryorderBarcodeEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcode = "";

    public String getBarcodeStr() {
        return this.barcode;
    }

    @ColumnInfo(name = cDatabase.BARCODETYPE_NAMESTR)
    public String barcodetype;

    public String getBarcodeTypesStr() {
        return this.barcodetype;
    }

    @ColumnInfo(name = cDatabase.ISUNIQUEBARCODE_NAMESTR)
    public Boolean isuniquebarcode;

    public Boolean getIsUniqueBarcodeBln() {
        return this.isuniquebarcode;
    }

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;

    public String getItemNoStr() {
        return this.itemno;
    }

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantCode;

    public String getVariantCodeStr() {
        return this.variantCode;
    }

    @ColumnInfo(name = cDatabase.ITEMTYPE_NAMESTR)
    public String itemType;

    public String getItemTypeStr() {
        return this.itemType;
    }
    @ColumnInfo(name = cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR)
    public Double quantityPerUnitOfMeasure;

    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasure;
    }
    @ColumnInfo(name = cDatabase.UNITOFMEASURE_NAMESTR)
    public String unitOfMeasure;

    public String getUnitOfMeasureStr() {
        return this.unitOfMeasure;
    }
    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityHandled;

    public Double getQuantityHandled() {
        return this.quantityHandled;
    }
    @ColumnInfo(name = cDatabase.INV_AMOUNT_MANUAL_NAMESTR)
    public Boolean invAmountManual;

    //End Region Public Properties

    public  cInventoryorderBarcodeEntity(){

    }

    public cInventoryorderBarcodeEntity(JSONObject pvJsonObject) {
        try {
            this.barcode = pvJsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.barcodetype = pvJsonObject.getString(cDatabase.BARCODETYPE_NAMESTR);
            this.isuniquebarcode = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.ISUNIQUEBARCODE_NAMESTR), false);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantCode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.itemType = pvJsonObject.getString(cDatabase.ITEMTYPE_NAMESTR);
            this.quantityPerUnitOfMeasure = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR));
            this.unitOfMeasure = pvJsonObject.getString(cDatabase.UNITOFMEASURE_NAMESTR);
            this.quantityHandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));
            this.invAmountManual = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.INV_AMOUNT_MANUAL_NAMESTR), false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public cInventoryorderBarcodeEntity(cArticleBarcode pvArticleBarcode) {
            this.barcode = pvArticleBarcode.getBarcodeStr();
            this.barcodetype = cText.pIntToStringStr(pvArticleBarcode.getBarcodeTypeInt());
            this.isuniquebarcode = pvArticleBarcode.getUniqueBarcodeBln();
            this.itemno = pvArticleBarcode.getItemNoStr();
            this.variantCode = pvArticleBarcode.getVariantCodeStr();
            this.itemType = "";
            this.quantityPerUnitOfMeasure = pvArticleBarcode.getQuantityPerUnitOfMeasureDbl();
            this.unitOfMeasure = pvArticleBarcode.getUnitOfMeasureStr();
            this.quantityHandled = 0.0;
            this.invAmountManual = false;
    }
//End Region Constructor
}