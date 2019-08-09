package SSU_WHS.PickorderLineBarcodes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERLINEBARCODE, primaryKeys = {cDatabase.PICKORDERLINEBARCODE_LINENO,cDatabase.PICKORDERLINEBARCODE_BARCODE})
public class cPickorderLineBarcodeEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_LINENO)
    public String lineno;
    @NonNull
    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_BARCODE)
    public String barcode;
    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_QUANTITY)
    public String quantity;
    @ColumnInfo(name = cDatabase.PICKORDERLINEBARCODE_ISMANUAL)
    public Boolean ismanual;

    //empty constructor
    public cPickorderLineBarcodeEntity() {

    }

    public cPickorderLineBarcodeEntity(JSONObject jsonObject) {
        try {
            lineno = jsonObject.getString(cDatabase.LINENO_NAMESTR);
            barcode = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            quantity = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLineno(String pv_lineno) {this.lineno = pv_lineno;}
    public String getLineno() {return lineno;}
    public void setBarcode(String pv_barcode) {this.barcode = pv_barcode;}
    public String getBarcode() {return barcode;}
    public void setQuantityhandled(String pv_quantityhandled) {this.quantity = pv_quantityhandled;}
    public String getQuantityhandled() {return quantity;}
    public boolean getIsmanual() {return ismanual;}
    public void setIsmanual(boolean ismanual) {this.ismanual = ismanual;}
}
