package SSU_WHS.ArticleImages;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cArticleImageViewModel extends AndroidViewModel {
    private cArticleImageRepository mRepository;

    public cArticleImageViewModel(Application application) {
        super(application);

        mRepository = new cArticleImageRepository(application);
    }
    public void insert(cArticleImageEntity articleImageEntity) {mRepository.insert(articleImageEntity);}

    public LiveData<List<cArticleImageEntity>> getArticleImages(Boolean forcerefresh, String user, String owner, String itemno, String variantcode, Boolean refresh) {return mRepository.getArticleImages(forcerefresh, user, owner, itemno, variantcode, refresh); }

    public LiveData<List<cArticleImageEntity>> getArticleImagesMultiple(Boolean forcerefresh, String user, String owner, List<cArticleImage> articleImages) {return mRepository.getArticleImagesMultiple(forcerefresh, user, owner, articleImages);}

    public List<cArticleImageEntity> getLocalArticleImages() {return mRepository.getAll();}

    public void deleteAll() {mRepository.deleteAll();}

    public cArticleImageEntity getArticleImageByItemnoAndVariantCode(String pv_itemno, String pvb_variantcode) { return mRepository.getArticleImageByItemnoAndVariantCode(pv_itemno, pvb_variantcode);}
}
