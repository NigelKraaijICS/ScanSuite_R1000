package SSU_WHS.Intake.IntakeorderBarcodes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INTAKEORDERBARCODE)
public class cIntakeorderBarcodeEntity {

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
    @ColumnInfo(name = cDatabase.RECEIVE_AMOUNT_MANUAL_NAMESTR)
    public Boolean receiveAmountManual;

    //End Region Public Properties

    public  cIntakeorderBarcodeEntity(){

    }

    public cIntakeorderBarcodeEntity(JSONObject pvJsonObject) {
        try {
            this.barcode = pvJsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.barcodetype = pvJsonObject.getString(cDatabase.BARCODETYPE_NAMESTR);
            this.isuniquebarcode = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.ISUNIQUEBARCODE_NAMESTR), false);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantCode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.quantityPerUnitOfMeasure = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR));
            this.unitOfMeasure = pvJsonObject.getString(cDatabase.UNITOFMEASURE_NAMESTR);
            this.quantityHandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR));
            this.receiveAmountManual = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RECEIVE_AMOUNT_MANUAL_NAMESTR), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//End Region Constructor
}