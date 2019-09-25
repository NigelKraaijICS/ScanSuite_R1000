package SSU_WHS.Basics.ArticleImages;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

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

}
