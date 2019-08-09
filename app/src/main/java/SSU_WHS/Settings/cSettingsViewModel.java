package SSU_WHS.Settings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cSettingsViewModel extends AndroidViewModel {
    private cSettingsRepository mRepository;
    private LiveData<List<cSettingsEntity>> mAllSettings;

    public cSettingsViewModel(Application application) {
        super(application);

        mRepository = new cSettingsRepository(application);
    }
    public LiveData<List<cSettingsEntity>> getSettings() {return mRepository.getSettings();}

    public void insert(cSettingsEntity settingsEntity) {mRepository.insert(settingsEntity);}

    public String getSetting(String key) {return mRepository.getSetting(key);}

    public List<cSettingsEntity> getLocalSettings() {return mRepository.getLocalSettings();}

    public void deleteAll() {mRepository.deleteAll();}
}
