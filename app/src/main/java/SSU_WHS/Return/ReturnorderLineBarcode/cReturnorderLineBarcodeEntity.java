package SSU_WHS.Return.ReturnorderLineBarcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_RETURNORDERLINEBARCODE)
public class cReturnorderLineBarcodeEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @NonNull
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public Long lineNoLng = 0L;
    @NonNull
    public Long getLineNoLng() {return lineNoLng;}

    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcodeStr = "";
    @NonNull
    public String getBarcodeStr() {return barcodeStr;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public String quantityHandledStr;
    public String getQuantityhandledStr() {return quantityHandledStr;}

    public cReturnorderLineBarcodeEntity(JSONObject jsonObject) {
        try {
            this.lineNoLng = cText.pStringToLongLng(jsonObject.getString(cDatabase.LINENO_NAMESTR));
            this.barcodeStr = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.quantityHandledStr = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public cReturnorderLineBarcodeEntity(@NonNull Long pvLineNoLng, @NonNull String pvBarcodeStr, Double pvQuantityHandled) {
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledStr = cText.pDoubleToStringStr(pvQuantityHandled);
    }

    public  cReturnorderLineBarcodeEntity(){

    }
}
