package SSU_WHS.Basics.ArticleImages;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cArticleImageViewModel extends AndroidViewModel {


    private cArticleImageRepository Repository;

    public cArticleImageViewModel(Application application) {
        super(application);

        Repository = new cArticleImageRepository(application);
    }
    public void insert(cArticleImageEntity articleImageEntity) {
        Repository.insert(articleImageEntity);}

    public void deleteAll() {
        Repository.deleteAll();}

    public cWebresult pGetArticleImageFromWebserviceWrs(String pvItemNoStr, String pvVariantCodeStr) {return  this.Repository.pGetArticleImageFromWebserviceWrs(pvItemNoStr,pvVariantCodeStr);}

    public cWebresult pGetArticleImagesFromWebserviceWrs(List<String> pvItemNoAndVariantObl) {return this.Repository.pGetArticleImagesFromWebserviceWrs(pvItemNoAndVariantObl);}


}
