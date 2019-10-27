package SSU_WHS.Basics.ArticleBarcode;

import org.json.JSONObject;

import java.util.Date;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;

public class cArticleBarcode {

    //Region Public Properties

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public String barcodeStr;
    public String getBarcodeStr() {
        return barcodeStr;
    }

    public String barcodeWithoutCheckDigitStr;
    public String getBarcodeWithoutCheckDigitStr() {

        this.barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (this.getBarcodeTypeInt() != cBarcodeScan.BarcodeType.EAN8 && this.getBarcodeTypeInt() != cBarcodeScan.BarcodeType.EAN13 ) {
            return  this.barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() != 8 && this.getBarcodeStr().length() != 13 ) {
            return  this.barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() == 8)  {
            this.barcodeWithoutCheckDigitStr = this.barcodeWithoutCheckDigitStr.substring(0,7);
        }

        if (this.getBarcodeStr().length() == 13)  {
            this.barcodeWithoutCheckDigitStr = this.barcodeWithoutCheckDigitStr.substring(0,12);
        }

        return barcodeWithoutCheckDigitStr;
    }

    public int barcodeTypeInt;
    public int getBarcodeTypeInt() { return barcodeTypeInt; }

    public Boolean isUniqueBarcodeBln;
    public Boolean getUniqueBarcodeBln() {return isUniqueBarcodeBln; }

    public Double quantityPerUnitOfMeasureDbl;
    public Double getQuantityPerUnitOfMeasureDbl() { return quantityPerUnitOfMeasureDbl; }

    public String unitOfMeasureStr;
    public String getUnitOfMeasureStr() { return unitOfMeasureStr; }

    public Date dataTimeStampDat;
    public Date getDataTimeStampDat() { return dataTimeStampDat; }

    public cArticleBarcodeEntity articleBarcodeEntity;


    //End Region Public Properties

    //Region Constructor
    public cArticleBarcode(JSONObject pvJsonObject, cArticle pvArticle) {
        this.articleBarcodeEntity = new cArticleBarcodeEntity(pvJsonObject);
        this.itemNoStr = pvArticle.getItemNoStr();
        this.variantCodeStr = pvArticle.getVariantCodeStr();
        this.barcodeStr = this.articleBarcodeEntity.getBarcodeStr();
        this.barcodeTypeInt =  cText.pStringToIntegerInt(this.articleBarcodeEntity.getBarcodeTypeStr());
        this.isUniqueBarcodeBln =  cText.pStringToBooleanBln( this.articleBarcodeEntity.getIsUniqueBarcodeStr(), false);
        this.quantityPerUnitOfMeasureDbl =  cText.pStringToDoubleDbl(this.articleBarcodeEntity.getQtyPerUnitOfMeasureStr());
        this.unitOfMeasureStr = this.articleBarcodeEntity.getUnitOfMeasureStr();
        this.dataTimeStampDat = cText.pStringToDateStr(this.articleBarcodeEntity.getDateTimeStampStr(),"YYYY-MM-dd");
    }

    //End Region Constructor

    //Region Public Methods


    //End Region Public Methods
}
