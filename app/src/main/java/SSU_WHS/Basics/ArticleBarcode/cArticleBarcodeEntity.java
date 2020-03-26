package SSU_WHS.Basics.ArticleBarcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_ARTICLEBARCODE, primaryKeys = {"Barcode"})
public class cArticleBarcodeEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name="Barcode")
    public String barcode = "";
    public String getBarcodeStr() {return this.barcode;}

    @ColumnInfo(name="BarCodeType")
    private String barcodeType;
    public String getBarcodeTypeStr() {return this.barcodeType;}

    @ColumnInfo(name="IsUniqueBarcode")
    private String isUniqueBarcode;
    public String getIsUniqueBarcodeStr() {
        return isUniqueBarcode;
    }

    @ColumnInfo(name="QtyPerUnitOfMeasure")
    private String qtyPerUnitOfMeasure;
    public String getQtyPerUnitOfMeasureStr() {
        return qtyPerUnitOfMeasure;
    }

    @ColumnInfo(name="UnitOfMeasure")
    public String unitOfMeasure;
    public String getUnitOfMeasureStr() {
        return unitOfMeasure;
    }

    @ColumnInfo(name="DataTimestamp")
    private String dateTimeStampStr;
    public String getDateTimeStampStr() {
        return dateTimeStampStr;
    }


    //End Region Public Properies

    public cArticleBarcodeEntity(JSONObject pvJsonObject) {
        try {
            this.barcode = pvJsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.barcodeType = pvJsonObject.getString(cDatabase.BARCODETYPE_NAMESTR);
            this.isUniqueBarcode = pvJsonObject.getString(cDatabase.ISUNIQUEBARCODE_NAMESTR);
            this.qtyPerUnitOfMeasure = pvJsonObject.getString(cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR);
            this.unitOfMeasure = pvJsonObject.getString(cDatabase.UNITOFMEASURE_NAMESTR);
            this.dateTimeStampStr = pvJsonObject.getString(cDatabase.DATATIMESTAMP_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor


}
