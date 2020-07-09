package SSU_WHS.Basics.ArticleBarcode;

import org.json.JSONObject;

import java.util.Date;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;

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


    public String getBarcodeWithoutCheckDigitStr() {

         String barcodeWithoutCheckDigitStr;
        barcodeWithoutCheckDigitStr = this.getBarcodeStr();

        if (this.getBarcodeTypeInt() != cBarcodeScan.BarcodeType.EAN8 && this.getBarcodeTypeInt() != cBarcodeScan.BarcodeType.EAN13 ) {
            return  barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() != 8 && this.getBarcodeStr().length() != 13 ) {
            return  barcodeWithoutCheckDigitStr;
        }

        if (this.getBarcodeStr().length() == 8)  {
            barcodeWithoutCheckDigitStr = barcodeWithoutCheckDigitStr.substring(0,7);
        }

        if (this.getBarcodeStr().length() == 13)  {
            barcodeWithoutCheckDigitStr =barcodeWithoutCheckDigitStr.substring(0,12);
        }

        return barcodeWithoutCheckDigitStr;
    }

    private int barcodeTypeInt;
    public int getBarcodeTypeInt() { return barcodeTypeInt; }

    public Boolean isUniqueBarcodeBln;
    public Boolean getUniqueBarcodeBln() {return isUniqueBarcodeBln; }

    public Double quantityPerUnitOfMeasureDbl;
    public Double getQuantityPerUnitOfMeasureDbl() { return quantityPerUnitOfMeasureDbl; }

    public String unitOfMeasureStr;
    public String getUnitOfMeasureStr() { return unitOfMeasureStr; }

    private Date dataTimeStampDat;
    public Date getDataTimeStampDat() { return dataTimeStampDat; }

    private cArticleBarcodeEntity articleBarcodeEntity;


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

    public cArticleBarcode(cMoveorderBarcode pvMoveorderBarcode) {
        this.itemNoStr = pvMoveorderBarcode.getItemNoStr();
        this.variantCodeStr = pvMoveorderBarcode.getVariantCodeStr();
        this.barcodeStr = pvMoveorderBarcode.getBarcodeStr();
        this.barcodeTypeInt =  cText.pStringToIntegerInt(pvMoveorderBarcode.getBarcodeTypesStr());
        this.isUniqueBarcodeBln =  pvMoveorderBarcode.getIsUniqueBarcodeBln();
        this.quantityPerUnitOfMeasureDbl =  pvMoveorderBarcode.getQuantityPerUnitOfMeasureDbl();
        this.unitOfMeasureStr = pvMoveorderBarcode.getUnitOfMeasureStr();
        this.dataTimeStampDat = null;
    }

    //End Region Constructor

    //Region Public Methods


    //End Region Public Methods
}
