package ICS.Weberror;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cWeberrorViewModel extends AndroidViewModel {
    private cWeberrorRepository mRepository;

    public cWeberrorViewModel(Application application) {
        super(application);

        mRepository = new cWeberrorRepository(application);
    }
    public void insert(cWeberrorEntity weberrorEntity) {mRepository.insert(weberrorEntity);}

    public LiveData<List<cWeberrorEntity>> getAllForActivityLive(String activity) {return mRepository.getAllForActivityLive(activity); }

    public List<cWeberrorEntity> getAllForActivity(String activity) {return mRepository.getAllForActivity(activity); }

    public LiveData<List<cWeberrorEntity>> getAllLive() { return mRepository.getAllLive();}

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteAllForActivity(String activity) {mRepository.deleteAllForActivity(activity);}

    public LiveData<List<cWeberrorEntity>> getAllForActivityAndStatusLive(String activity, String status) {return mRepository.getAllForActivityAndStatusLive(activity, status); }

    public LiveData<List<cWeberrorEntity>> getAllByStatusLive(String status) {return mRepository.getAllByStatusLive(status); }

}
