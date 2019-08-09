package SSU_WHS.Users;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cUserViewModel extends AndroidViewModel {
    private cUserRepository mRepository;

    public cUserViewModel(Application application) {
        super(application);

        mRepository = new cUserRepository(application);
    }
    public void insert(cUserEntity userEntity) {mRepository.insert(userEntity);}

    //public LiveData<List<cUserEntity>> getUsers(Boolean forcerefresh, String branch) {return mRepository.getUsers(forcerefresh, branch); }
    //public LiveData<List<cUser>> getUsers(Boolean forcerefresh, String branch) {return mRepository.getUsers(forcerefresh, branch); }

    public LiveData<Boolean> gotUsers(Boolean forcerefresh, String branch) {return mRepository.gotUsers(forcerefresh, branch); }

    public List<cUserEntity> getLocalUsers() {return mRepository.getLocalUsers();}

    public void deleteAll() {mRepository.deleteAll();}

    public cUserEntity getUserByCode(String username) {return mRepository.getUserByCode(username);}

    public cWebresult userLogonWrs(String user, String password, String culture) {return mRepository.userLogonWrs(user, password, culture);}


}

