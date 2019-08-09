package SSU_WHS.Picken.PickorderBarcodes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERBARCODE, primaryKeys = {cDatabase.BARCODE_NAMESTR, cDatabase.BARCODETYPE_NAMESTR, cDatabase.ITEMNO_NAMESTR, cDatabase.VARIANTCODE_NAMESTR})
public class cPickorderBarcodeEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcode;
    @NonNull
    @ColumnInfo(name = cDatabase.BARCODETYPE_NAMESTR)
    public String barcodetype;
    @ColumnInfo(name = cDatabase.ISUNIQUEBARCODE_NAMESTR)
    public String isuniquebarcode;
    @NonNull
    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    @NonNull
    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    @ColumnInfo(name = cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR)
    public String quantityperunitofmeasure;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public String quantityhandled;

    //empty constructor
    public cPickorderBarcodeEntity() {

    }

    public cPickorderBarcodeEntity(JSONObject jsonObject) {
        try {
            barcode = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            barcodetype = jsonObject.getString(cDatabase.BARCODETYPE_NAMESTR);
            isuniquebarcode = jsonObject.getString(cDatabase.ISUNIQUEBARCODE_NAMESTR);
            itemno= jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            variantcode= jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            quantityperunitofmeasure= jsonObject.getString(cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR);
            quantityhandled = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setBarcode(String pv_barcode) {this.barcode = pv_barcode;}
    public String getBarcode() {return barcode;}
    public void setBarcodeType(String pv_barcodetype) {this.barcodetype = pv_barcodetype;}
    public String getBarcodeType() {return barcodetype;}
    public void setIsuniquebarcode(String pv_isuniquebarcode) {this.isuniquebarcode = pv_isuniquebarcode;}
    public String getIsuniquebarcode() {return isuniquebarcode;}
    public void setItemno(String pv_itemno) {this.itemno = pv_itemno;}
    public String getItemno() {return itemno;}
    public void setVariantcode(String pv_variantcode) {this.variantcode = pv_variantcode;}
    public String getVariantcode() {return variantcode;}
    public void setQuantityperunitofmeasure(String pv_quantityperunitofmeasure) {this.quantityperunitofmeasure = pv_quantityperunitofmeasure;}
    public String getQuantityperunitofmeasure() {return quantityperunitofmeasure;}
    public void setQuantityhandled(String pv_quantityhandled) {this.quantityhandled = pv_quantityhandled;}
    public String getQuantityhandled() {return quantityhandled;}
}
