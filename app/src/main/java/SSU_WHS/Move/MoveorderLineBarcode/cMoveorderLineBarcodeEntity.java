package SSU_WHS.Move.MoveorderLineBarcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_MOVEORDERLINEBARCODE, primaryKeys = {cDatabase.PICKORDERLINEBARCODE_LINENO,cDatabase.BARCODE_NAMESTR})
public class cMoveorderLineBarcodeEntity {

    //Region Public Properties
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public int lineNoInt = 0;
    public int getLineNoInt() {
        return this.lineNoInt;
    }

    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcodeStr = "";
    @NonNull
    public String getBarcodeStr() {
        return this.barcodeStr;
    }

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityHandled;
    public Double getQuantityHandled() {
        return this.quantityHandled;
    }

    //End Region Public Properties

    //Region Constructor
    public cMoveorderLineBarcodeEntity() {

    }

    public cMoveorderLineBarcodeEntity(JSONObject pvJsonObject) {
        try {
            this.lineNoInt = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.barcodeStr = pvJsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.quantityHandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//End Region Constructor
}