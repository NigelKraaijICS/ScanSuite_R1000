package SSU_WHS.Picken.PickorderShipPackages;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cPickorderShipPackageViewModel extends AndroidViewModel {
    private cPickorderShipPackageRepository Repository;

    public cPickorderShipPackageViewModel(Application application) {
        super(application);
        Repository = new cPickorderShipPackageRepository(application);
    }

    public void insert(cPickorderShipPackageEntity pickorderShipPackageEntity) {
        Repository.insert(pickorderShipPackageEntity);}

    public void deleteAll() {
        Repository.deleteAll();}

}
