package SSU_WHS.Picken.Comments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cCommentViewModel  extends AndroidViewModel {
    private cCommentRepository mRepository;

    public cCommentViewModel(Application application) {
        super(application);

        mRepository = new cCommentRepository(application);
    }

    public LiveData<List<cCommentEntity>> getComments(Boolean forcerefresh, String branchStr, String ordernumberStr) {return mRepository.getComments(forcerefresh, branchStr, ordernumberStr);}

    public List<cCommentEntity> getAll() {return mRepository.getAll();}
}
