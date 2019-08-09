package SSU_WHS.Basics.Authorisations;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cAuthorisationViewModel  extends AndroidViewModel {
    private cAuthorisationRepository mRepository;

    public cAuthorisationViewModel(Application application) {
        super(application);

        mRepository = new cAuthorisationRepository(application);
    }
    public void insert(cAuthorisationEntity authorisationEntity) {mRepository.insert(authorisationEntity);}

    public LiveData<List<cAuthorisationEntity>> getAuthorisations(Boolean forcerefresh, String username, String branch) {return mRepository.getAuthorisations(forcerefresh, username, branch);}

    public void deleteAll() {mRepository.deleteAll();}
}

