package SSU_WHS.Basics.Article;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.ArticleProperty.cArticleProperty;
import SSU_WHS.Basics.ArticlePropertyStock.cArticlePropertyStock;
import SSU_WHS.Basics.ArticlePropertyValue.cArticlePropertyValue;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cArticle {

    //Region Public Properties

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantCodeStr(){
        return   this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private final String itemInfoCodeStr;
    public String getItemInfoCodeStr() { return itemInfoCodeStr; }

    public String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    public String vendorItemDescriptionStr;
    public String getItemDescriptionStr() { return vendorItemDescriptionStr; }

    private final String component10Str;
    public String getComponent10Str() { return component10Str; }

    private final Double priceDbl;
    public Double getPriceDbl() { return priceDbl; }

    private final cArticleEntity articleEntity;
    public  List<cArticleBarcode> barcodesObl;
    public  List<cArticleStock> stockObl;
    public  List<cArticleProperty> propertyObl;
    public List<cArticlePropertyValue> propertyValueObl;
    public cArticlePropertyValue currentPropertyValue;

    public cArticleImage articleImage;
    private cArticleViewModel getArticleViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleViewModel.class);
    }

    public  static cArticle currentArticle;

    //End Region Public Properties

    //Region Constructor
    public cArticle(JSONObject pvJsonObject) {
        this.articleEntity = new cArticleEntity(pvJsonObject);
        this.itemNoStr = this.articleEntity.getItemnoStr();
        this.variantCodeStr =  this.articleEntity.getVariantCodeStr();

        if (this.articleEntity.getDescriptionStr() == null) {
            this.descriptionStr = "";
            this.description2Str = "";
            this.itemInfoCodeStr = "";
            this.vendorItemNoStr = "";
            this.vendorItemDescriptionStr = "";
            this.component10Str = "";
            this.priceDbl = (double) 0;
            return;
        }

        this.descriptionStr = this.articleEntity.getDescriptionStr();
        this.description2Str = this.articleEntity.getDescription2Str();
        this.itemInfoCodeStr = this.articleEntity.getItemInfoCodeStr();
        this.vendorItemNoStr = this.articleEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.articleEntity.getVendorItemDescriptionStr();
        this.component10Str = this.articleEntity.getComponent10Str();
        this.priceDbl = this.articleEntity.getPriceDbl();
    }

    public cArticle(String pvitemNoStr, String pvVariantCodeStr) {
        this.articleEntity = null;
        this.itemNoStr = pvitemNoStr;
        this.variantCodeStr =  pvVariantCodeStr;
        this.descriptionStr = "???";
        this.description2Str = "???";
        this.itemInfoCodeStr =  "???";
        this.vendorItemNoStr =  "???";
        this.vendorItemDescriptionStr =  "???";
        this.component10Str = "???";
        this.priceDbl = (double) 0;
    }

    //End Region Constructor

    //Region Public Methods

    public static cArticle pGetArticleByBarcodeViaWebservice(cBarcodeScan pvBarcodescan){

        cWebresult webresult;

        cArticleViewModel articleViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleViewModel.class);
        webresult = articleViewModel.pGetArticleByBarcodeViaWebserviceWrs(pvBarcodescan);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {
            for (JSONObject jsonObject :  webresult.getResultDtt()) {


                cArticle article = new cArticle(jsonObject);

                cArticleBarcode articleBarcode = new cArticleBarcode(article.getItemNoStr(),article.getVariantCodeStr(),pvBarcodescan.getBarcodeOriginalStr());

                if (article.barcodesObl == null) {
                    article.barcodesObl = new ArrayList<>();
                }

                article.barcodesObl.add(articleBarcode);
                article.pGetPropertyViaWebservice();

                return article;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEVIAOWNERBARCODE);
        }
        return null;
    }

    public static cArticle pGetArticleByItemNoVariantViaWebservice(String pvItemNoStr, String pvVariantcodeStr){

        cWebresult webresult;

        cArticleViewModel articleViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleViewModel.class);
        webresult = articleViewModel.pGetArticleByItemNoVariantWrs(pvItemNoStr, pvVariantcodeStr);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {
            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                cArticle article = new cArticle(jsonObject);

              if( ! article.pGetBarcodesViaWebserviceBln(null)){
                  return null;
              }
                article.pGetPropertyViaWebservice();
                return article;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEVIAOWNERBARCODE);
        }
        return null;
    }

    public boolean pGetBarcodesViaWebserviceBln(cBarcodeScan pvBarcodeScan) {
        cWebresult webresult;

        webresult = getArticleViewModel().pGetBarcodesViaWebserviceWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            this.barcodesObl = new ArrayList<>();
            for (JSONObject jsonObject : webresult.getResultDtt()) {
                cArticleBarcode articleBarcode = new cArticleBarcode(jsonObject, this);
                this.barcodesObl.add(articleBarcode);

            }

            // We didn't receive a barcode, so this barcode should be an unique barcode
            if (this.barcodesObl.size() == 0) {
                webresult = getArticleViewModel().pGetUniqueBarcodeViaWebserviceWrs(pvBarcodeScan);
                if (webresult.getResultBln() && webresult.getSuccessBln()) {
                    for (JSONObject jsonObject : webresult.getResultDtt()) {
                        cArticleBarcode articleBarcode = new cArticleBarcode(jsonObject, this);
                        this.barcodesObl.add(articleBarcode);
                    }
                }
                return true;
            }
            }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
            return false;
        }

        return  true;
    }

    public void pGetStockViaWebserviceBln(){
        cWebresult webresult;

        webresult = getArticleViewModel().pGetStockViaWebserviceWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                cArticleStock articleStock = new cArticleStock(jsonObject, this);


                if (this.stockObl == null) {
                    this.stockObl = new ArrayList<>();
                }

                this.stockObl.add(articleStock);

            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
        }
    }

    public void pGetPropertyViaWebservice(){
        cWebresult webresult;

        webresult = getArticleViewModel().pGetItemPropertyWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                cArticleProperty articleProperty = new cArticleProperty(jsonObject, this);
                if (articleProperty.getItemProperty() == null){
                    continue;
                }

                if (this.propertyObl == null){
                    this.propertyObl = new ArrayList<>();
                }
                this.propertyObl.add(articleProperty);
                currentArticle = this;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEPROPERTIES);
        }
        pGetPropertyValueViaWebservice();
    }

    public void pGetPropertyValueViaWebservice(){
        cWebresult webresult;

        webresult = getArticleViewModel().pGetItemPropertyValueWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                cArticlePropertyValue articlePropertyValue = new cArticlePropertyValue(jsonObject , this);
                if (this.propertyValueObl == null){
                    this.propertyValueObl = new ArrayList<>();
                }
                this.propertyValueObl.add(articlePropertyValue);

            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
        }
    }

    public cArticleStock pGetStockForBINViaWebservice(String pvBinCodeStr){
        cWebresult webresult;

        webresult = getArticleViewModel().pGetStockViaWebserviceWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                cArticleStock articleStock = new cArticleStock(jsonObject, this);


                if (!articleStock.getBincodeStr().equalsIgnoreCase(pvBinCodeStr)) {
                    continue;
                }

              return  articleStock;

            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
        }

        return  null;

    }

    public cArticleStock pGetPropertyStockForBINViaWebservice(ArrayList<cArticlePropertyValue> pvPropertyValueObl, String pvBinCodeStr){
        cWebresult webresult;
        double amountDbl = 0.0;
        ArrayList <cArticlePropertyStock> articlePropertyStockObl = new ArrayList<>();

        for (cArticlePropertyValue articlePropertyValue: pvPropertyValueObl){

            if (articlePropertyValue.getValueStr() == null){
                continue;
            }
            webresult = getArticleViewModel().pGetPropertyStockWrs(articlePropertyValue);
            if (webresult.getResultBln() && webresult.getSuccessBln()) {
                for (JSONObject jsonObject :  webresult.getResultDtt()) {
                    cArticlePropertyStock articlePropertyStock = new cArticlePropertyStock(jsonObject);
                    if (!articlePropertyStock.getBincodeStr().equalsIgnoreCase(pvBinCodeStr) || !articlePropertyStock.getItemNoStr().equalsIgnoreCase(this.getItemNoStr()) || !articlePropertyStock.getVariantCodeStr().equalsIgnoreCase(this.getVariantCodeStr())) {
                        continue;
                    }
                    articlePropertyStockObl.add(articlePropertyStock);
                    if (amountDbl == 0.0) {
                        amountDbl = articlePropertyStock.getQuantityDbl();
                    }
                }
            }  else {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLESTOCKWITHPROPERTY);
            }
        }

        if (articlePropertyStockObl.size() == 0){
            return null;
        }

        if (articlePropertyStockObl.size() == 1){
            return new cArticleStock(articlePropertyStockObl.get(0));
        }

        //Multiple properties so compare the available amount should be the same at the location

        for (cArticlePropertyStock articlePropertyStock: articlePropertyStockObl){
            if (amountDbl != articlePropertyStock.getQuantityDbl()){
                return null;
            }
        }
        return new cArticleStock(articlePropertyStockObl.get(0));
    }

    public boolean pGetArticleImageBln(){

        if (this.articleImage != null) {
            return  true;
        }

        this.articleImage = cArticleImage.pGetArticleImageByItemNoAndVariantCode(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.articleImage != null){
            return  true;
        }

        cWebresult Webresult;

        cArticleImageViewModel articleImageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
        Webresult = articleImageViewModel.pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        if (Webresult.getResultDtt().size() == 1) {
            cArticleImage articleImage = new cArticleImage(Webresult.getResultDtt().get(0));
            articleImage.pInsertInDatabaseBln();
            this.articleImage = articleImage;
            return true;
        }

        return  false;

    }

    //End Region Public Methods
}
