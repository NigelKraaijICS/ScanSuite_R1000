package SSU_WHS.Branches;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cBranchViewModel extends AndroidViewModel {
    private cBranchRepository mRepository;

    public cBranchViewModel(Application application) {
        super(application);

        mRepository = new cBranchRepository(application);
    }
    public void insert(cBranchEntity branchEntity) {mRepository.insert(branchEntity);}

    public LiveData<List<cBranchEntity>> getBranchesForUser(Boolean forcerefresh, String user) {return mRepository.getBranchesForUser(forcerefresh, user); }

    public List<cBranchEntity> getLocalBranches() {return mRepository.getAll();}

    public void deleteAll() {mRepository.deleteAll();}

    public cBranchEntity getBranchByCode(String branch) {return mRepository.getBranchByCode(branch);}

}
