package SSU_WHS.Picken.PickorderShipPackages;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cPickorderShipPackageViewModel extends AndroidViewModel {
    private cPickorderShipPackageRepository mRepository;

    public cPickorderShipPackageViewModel(Application application) {
        super(application);

        mRepository = new cPickorderShipPackageRepository(application);
    }

    public void insert(cPickorderShipPackageEntity pickorderShipPackageEntity) {mRepository.insert(pickorderShipPackageEntity);}

    public LiveData<List<cPickorderShipPackageEntity>> getPickorderShipPackages(Boolean forcerefresh, String branch, String ordernumber) {return mRepository.getPickorderShipPackages(forcerefresh, branch, ordernumber);}

    public void deleteAll() {mRepository.deleteAll();}

}
