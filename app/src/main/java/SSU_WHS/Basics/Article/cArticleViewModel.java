package SSU_WHS.Basics.Article;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Webservice.cWebresult;

public class cArticleViewModel extends AndroidViewModel {


    private cArticleRepository Repository;

    public cArticleViewModel(Application application) {
        super(application);
        Repository = new cArticleRepository();
    }

    public cWebresult pGetArticleByBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return this.Repository.pGetArticleViaBarcodeWrs(pvBarcodeScan);}
    public cWebresult pGetBarcodesViaWebserviceWrs(cArticle pvArticle) {return this.Repository.pGetBarcodesWrs(pvArticle);}
    public cWebresult pGetArticleByItemNoVariantWrs(String pvItemNoStr, String pvVariantcodeStr) {return this.Repository.pGetArticleByItemNoVariantWrs(pvItemNoStr, pvVariantcodeStr);}
    public cWebresult pGetUniqueBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return this.Repository.pGetUniqueBarcodesWrs(pvBarcodeScan);}
    public cWebresult pGetStockViaWebserviceWrs(cArticle pvArticle) {return this.Repository.pGetStockWrs(pvArticle);}




}
