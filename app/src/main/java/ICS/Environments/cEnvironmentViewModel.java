package ICS.Environments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

public class cEnvironmentViewModel extends AndroidViewModel {
    private cEnvironmentRepository mRepository;

    public cEnvironmentViewModel(Application application) {
        super(application);

        mRepository = new cEnvironmentRepository(application);
    }
    public void insert(cEnvironmentEntity environmentEntity) {mRepository.insert(environmentEntity);}

    public void delete(cEnvironmentEntity environmentEntity) {mRepository.delete(environmentEntity);}

    public void deleteAll() {mRepository.deleteAll();}

    public List<cEnvironmentEntity> getAll() {return mRepository.getAll();}

    public cEnvironmentEntity getFirst() {return mRepository.getFirst(); }

    public cEnvironmentEntity getEnvironmentByName(String name) {return mRepository.getEnvironmentByName(name);}

    public cEnvironmentEntity getDefaultEnvironment() {return mRepository.getDefaultEnvironment();}

    public int getNumberOfEnvironments() {return mRepository.getNumberOfEnvironments();}


}
