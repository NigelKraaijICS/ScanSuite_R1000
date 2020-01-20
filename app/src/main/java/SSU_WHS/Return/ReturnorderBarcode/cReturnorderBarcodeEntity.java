package SSU_WHS.Return.ReturnorderBarcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_RETURNORDERBARCODE)
public class cReturnorderBarcodeEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcodeStr = "";
    @NonNull
    public String getBarcodeStr() {
        return this.barcodeStr;
    }

    @ColumnInfo(name = cDatabase.BARCODETYPE_NAMESTR)
    public String barcodeTypeStr;
    public String getBarcodeTypesStr() {
        return this.barcodeTypeStr;
    }

    @ColumnInfo(name = cDatabase.ISUNIQUEBARCODE_NAMESTR)
    public Boolean isUniqueBarcodeBln;
    public Boolean getIsUniqueBarcodeBln() {
        return this.isUniqueBarcodeBln;
    }

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemNoStr;
    public String getItemNoStr() {
        return this.itemNoStr;
    }

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantCode;
    public String getVariantCodeStr() {
        return this.variantCode;
    }

    @ColumnInfo(name = cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR)
    public Double quantityPerUnitOfMeasureDbl;
    public Double getQuantityPerUnitOfMeasureDbl() {
        return this.quantityPerUnitOfMeasureDbl;
    }

    @ColumnInfo(name = cDatabase.UNITOFMEASURE_NAMESTR)
    public String unitOfMeasureStr;
    public String getUnitOfMeasureStr() {
        return this.unitOfMeasureStr;
    }

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return this.quantityHandledDbl;
    }

    //End Region Public Properties

    public cReturnorderBarcodeEntity(JSONObject pvJsonObject) {
        try {
            this.barcodeStr = pvJsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.barcodeTypeStr = pvJsonObject.getString(cDatabase.BARCODETYPE_NAMESTR);
            this.isUniqueBarcodeBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.ISUNIQUEBARCODE_NAMESTR), false);
            this.itemNoStr = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantCode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.quantityPerUnitOfMeasureDbl = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR));
            this.unitOfMeasureStr = pvJsonObject.getString(cDatabase.UNITOFMEASURE_NAMESTR);
            this.quantityHandledDbl = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public cReturnorderBarcodeEntity(cArticleBarcode pvArticleBarcode) {
        this.barcodeStr = pvArticleBarcode.getBarcodeStr();
        this.barcodeTypeStr = cText.pIntToStringStr(pvArticleBarcode.getBarcodeTypeInt());
        this.isUniqueBarcodeBln = pvArticleBarcode.getUniqueBarcodeBln();
        this.itemNoStr = pvArticleBarcode.getItemNoStr();
        this.variantCode = pvArticleBarcode.getVariantCodeStr();
        this.quantityPerUnitOfMeasureDbl = pvArticleBarcode.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasureStr = pvArticleBarcode.getUnitOfMeasureStr();
        this.quantityHandledDbl = 0.0;
    }

    public  cReturnorderBarcodeEntity(){

    }

//End Region Constructor
}