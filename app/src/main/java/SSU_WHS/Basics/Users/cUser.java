package SSU_WHS.Basics.Users;

import android.arch.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.List;

import SSU_WHS.General.cAppExtension;

public class cUser {
    public static cUserViewModel getUserViewModel() {
        if (userViewModel == null) {
            userViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cUserViewModel.class);
        }
        return userViewModel;
    }

    public static cUserViewModel userViewModel;
    public static List<cUser> allUsers;

    cUser(JSONObject jsonObject) {
       this.userEntity = new cUserEntity(jsonObject);
       this.username = userEntity.getUsernameStr();
       this.name = userEntity.getNameStr();
       this.importfile = userEntity.getImportfileStr();
       insertInDatabase();
       this.indatabase = true;
    }

    public String username;
    public String name;
    public String importfile;
    public  cUserEntity userEntity;
    public boolean indatabase;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getImportfile() {
        return importfile;
    }

    public void insertInDatabase() {
        getUserViewModel().insert(this.userEntity);
    }
}
