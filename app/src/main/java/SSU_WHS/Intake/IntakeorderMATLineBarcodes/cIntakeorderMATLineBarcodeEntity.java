package SSU_WHS.Intake.IntakeorderMATLineBarcodes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_INTAKEORDERMATLINEBARCODE, primaryKeys = {cDatabase.LINENO_NAMESTR, cDatabase.BARCODE_NAMESTR})
public class cIntakeorderMATLineBarcodeEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public Long lineNoLng;
    public Long getLineNoLng() {return lineNoLng;}

    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcodeStr;
    public String getBarcodeStr() {return barcodeStr;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public String quantityHandledStr;
    public String getQuantityhandledStr() {return quantityHandledStr;}

    //empty constructor
    public cIntakeorderMATLineBarcodeEntity() {

    }

    public cIntakeorderMATLineBarcodeEntity(JSONObject jsonObject) {
        try {
            this.lineNoLng = cText.pStringToLongLng(jsonObject.getString(cDatabase.LINENO_NAMESTR));
            this.barcodeStr = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.quantityHandledStr = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public cIntakeorderMATLineBarcodeEntity(Long pvLineNoLng, String pvBarcodeStr) {
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledStr = "0";
    }
}
