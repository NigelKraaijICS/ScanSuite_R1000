package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_INVENTORYORDERLINEBARCODE, primaryKeys = {cDatabase.LINENO_NAMESTR, cDatabase.BARCODE_NAMESTR})
public class cInventoryorderLineBarcodeEntity {
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

    public  cInventoryorderLineBarcodeEntity(){

    }

    public cInventoryorderLineBarcodeEntity(JSONObject jsonObject) {
        try {
            this.lineNoLng = cText.pStringToLongLng(jsonObject.getString(cDatabase.LINENO_NAMESTR));
            this.barcodeStr = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.quantityHandledStr = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public cInventoryorderLineBarcodeEntity(@NonNull Long pvLineNoLng, @NonNull String pvBarcodeStr) {
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityHandledStr = "0";
    }
}
