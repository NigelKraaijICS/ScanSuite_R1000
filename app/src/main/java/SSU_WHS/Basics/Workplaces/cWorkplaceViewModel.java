package SSU_WHS.Basics.Workplaces;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cWorkplaceViewModel extends AndroidViewModel {
    private cWorkplaceRepository mRepository;

    public cWorkplaceViewModel(Application application) {
        super(application);

        mRepository = new cWorkplaceRepository(application);
    }
    public void insert(cWorkplaceEntity workplaceEntity) {mRepository.insert(workplaceEntity);}

    public LiveData<List<cWorkplaceEntity>> getWorkplaces(Boolean forcerefresh, String branch) {return mRepository.getWorkplaces(forcerefresh, branch); }

    public List<cWorkplaceEntity> getLocalWorkplaces() {return mRepository.getAll();}

    public void deleteAll() {mRepository.deleteAll();}

    public cWorkplaceEntity getWorkplaceByCode(String workplace) {return mRepository.getWorkplaceByCode(workplace);}
}
