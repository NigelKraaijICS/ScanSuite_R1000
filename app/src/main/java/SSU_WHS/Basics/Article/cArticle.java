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



                return article;
            }
            return null;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEVIAOWNERBARCODE);
            return null;
        }
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
