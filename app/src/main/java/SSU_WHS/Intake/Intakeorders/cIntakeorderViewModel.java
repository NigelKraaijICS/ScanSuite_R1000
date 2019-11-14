package SSU_WHS.Intake.Intakeorders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineEntity;
import SSU_WHS.Webservice.cWebresult;

public class cIntakeorderViewModel  extends AndroidViewModel {
    //Region Public Properties
    private cIntakeorderRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cIntakeorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cIntakeorderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cIntakeorderEntity pvIntakeorderEntity) {this.Repository.pInsertInDatabase(pvIntakeorderEntity);}
    public void deleteAll() {this.Repository.pTruncateTable();}

    public cWebresult pGetIntakeordersFromWebserviceWrs(String pvSearchTextStr) {return this.Repository.pGetIntakeordersFromWebserviceWrs(pvSearchTextStr);}

    public List<cIntakeorderEntity> pGetIntakeordersFromDatabaseObl() {return this.Repository.pGetIntakeordersFromDatabaseObl();}
    public cWebresult pGetIntakeorderMATBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}
    public cWebresult pGetIntakeorderMATLineBarcodesFromWebserviceWrs() {return this.Repository.pGetMATLineBarcodesFromWebserviceWrs();}
    public cWebresult pGetIntakeorderMATLinesFromWebserviceWrs() {return this.Repository.pGetMATLinesFromWebserviceWrs();}

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public List<cIntakeorderEntity> pGetIntakeordersFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetIntakeordersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public List<cIntakeorderMATLineEntity> pGetAllLinesFromDatabaseObl(){return  this.Repository.pGetAllMATLinesFromDatabaseObl();}
    public List<cIntakeorderMATLineEntity> pGetIntakeorderMATLinesToSendFromDatabaseObl(){return  this.Repository.pGetMATLinesToSendFromDatabaseObl();}
    public List<cIntakeorderMATLineEntity> pGetIntakeorderMATLinesNotHandledFromDatabaseObl(){return  this.Repository.pGetMATLinesNotHandledFromDatabaseObl();}
    public List<cIntakeorderMATLineEntity> pGetIntakeorderMATLinesBusyFromDatabaseObl(){return  this.Repository.pGetMATLinesBusyFromDatabaseObl();}
    public List<cIntakeorderMATLineEntity> pGetIntakeorderMATLinesHandledFromDatabaseObl(){return  this.Repository.pGetMATLinesHandledFromDatabaseObl();}

    public Double pQuantityNotHandledDbl() {return this.Repository.pQuantityNotHandledDbl();}
    public Double pQuantityHandledDbl() {return this.Repository.pQuantityHandledDbl();}
    public Double pGetQuantityTotalDbl() {return this.Repository.pGetTotalQuantityDbl();}

    public cWebresult pHandledViaWebserviceWrs() { return this.Repository.pHandledViaWebserviceBln();}



}
