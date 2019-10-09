package SSU_WHS.Picken.PickorderLineBarcodes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERLINEBARCODE, primaryKeys = {cDatabase.PICKORDERLINEBARCODE_LINENO,cDatabase.PICKORDERLINEBARCODE_BARCODE})
public class cPickorderLineBarcodeEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_LINENO)
    public String lineNoStr;
    public String getLineNoStr() {return lineNoStr;}

    @NonNull
    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_BARCODE)
    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_QUANTITY)
    public String quantityHandledStr;
    public String getQuantityhandledStr() {return quantityHandledStr;}

    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_ISMANUAL)
    public String isManualStr;
    public String getIsManualStr() {return isManualStr;}

    //empty constructor
    public cPickorderLineBarcodeEntity() {

    }

    public cPickorderLineBarcodeEntity(JSONObject jsonObject) {
        try {
            this.lineNoStr = jsonObject.getString(cDatabase.LINENO_NAMESTR);
            this.barcodeStr = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.quantityHandledStr = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
            this.isManualStr = "false";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public cPickorderLineBarcodeEntity(Long pvLineNoLng, String pvBarcodeStr) {
        this.lineNoStr = cText.longToString(pvLineNoLng);
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledStr = "0";
        this.isManualStr = "N";
    }
}
