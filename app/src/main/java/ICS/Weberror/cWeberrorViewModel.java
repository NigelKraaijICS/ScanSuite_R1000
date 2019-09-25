package ICS.Weberror;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class cWeberrorViewModel extends AndroidViewModel {

    //Region Public Properties
    public cWeberrorRepository Repository;
    //End Region Public Properties


    //Region Constructor
    public cWeberrorViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cWeberrorRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cWeberrorEntity weberrorEntity) {Repository.insert(weberrorEntity);}

    public List<cWeberrorEntity> getAllWebErrors() {return  Repository.getAllWebErrorsFromDatabase();}
    public List<cWeberrorEntity> getAllByStatus(String status) {return Repository.getAllByStatus(status); }

    public List<cWeberrorEntity> getAllForWebMethod(String pvMethodStr) {return Repository.getAllForWebMethodStr(pvMethodStr); }
    public List<cWeberrorEntity> getAllForActivity(String pvActivityStr) {return Repository.getAllWebErrorsForActivityFromDatabase(pvActivityStr); }
    public List<cWeberrorEntity> getAllForActivityAndStatus(String activity, String status) {return Repository.getAllForActivityAndStatus(activity, status); }

    public void delete(cWeberrorEntity weberrorEntity) {Repository.delete(weberrorEntity);}
    public void deleteAll() {Repository.deleteAll();}
    public void deleteAllForActivity(String pvActivityStr) {Repository.deleteAllForActivity(pvActivityStr);}

    //Region Public Methods

    //End Public Methods


}
