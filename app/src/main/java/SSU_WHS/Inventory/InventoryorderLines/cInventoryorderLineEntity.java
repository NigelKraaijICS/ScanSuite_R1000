package SSU_WHS.Inventory.InventoryorderLines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INVENTORYORDERLINE)
public class cInventoryorderLineEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    //    @PrimaryKey
//    @NonNull
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public int lineno;
    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    @ColumnInfo(name = cDatabase.ITEMTYPE_NAMESTR)
    public String itemtype;
    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String bincode;
    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemdescription;
    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingsequenceno;
    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    @ColumnInfo(name = cDatabase.SOURCETYPE_NAMESTR)
    public int sourcetype;
    @ColumnInfo(name = cDatabase.HANDLEDTIMESTAMP_NAMESTR)
    public String handledtimestamp;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLEDALLSCANNERS_NAMESTR)
    public Double quantityhandledAllScanners;

    //empty constructor
    public cInventoryorderLineEntity() {

    }

    public cInventoryorderLineEntity(JSONObject pvJsonObject) {
        try {
            this.lineno = pvJsonObject.getInt(cDatabase.LINENO_NAMESTR);
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.itemtype = pvJsonObject.getString(cDatabase.ITEMTYPE_NAMESTR);
            this.description = pvJsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2 = pvJsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.quantity = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITY_NAMESTR));
            this.vendoritemno = pvJsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendoritemdescription = pvJsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            this.sortingsequenceno = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
            this.status = pvJsonObject.getInt(cDatabase.STATUS_NAMESTR);
            this.sourcetype = pvJsonObject.getInt(cDatabase.SOURCETYPE_NAMESTR);
            this.handledtimestamp = pvJsonObject.getString(cDatabase.HANDLEDTIMESTAMP_NAMESTR);
            this.quantityhandled = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR)) ;
            this.quantityhandledAllScanners = cText.pStringToDoubleDbl(pvJsonObject.getString(cDatabase.QUANTITYHANDLEDALLSCANNERS_NAMESTR));
            //endregion extraField8Str


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getLineNoInt() {return this.lineno;}
    public String getItemNoStr() {return this.itemno;}
    public String getVariantCodeStr() {return this.variantcode;}
    public String getDescriptionStr() {return this.description;}
    public String getDescription2Str() {return this.description2;}
    public String getBincodeStr() {return this.bincode;}
    public Double getQuantityDbl() {return this.quantity;}
    public String getVendorItemNoStr() {return this.vendoritemno;}
    public String getVendorItemDescriptionStr() {return this.vendoritemdescription;}
    public int getStatusInt() {return this.status;}
    public int getSourceTypeInt() {return this.sourcetype;}
    public String getHandledtimestampStr() {return this.handledtimestamp;}
    public Double getQuantityHandledDbl() {return this.quantityhandled;}
    public Double getQuantityHandledAllScannersDbl() {return this.quantityhandledAllScanners;}
}
