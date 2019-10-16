package SSU_WHS.Picken.PickorderLines;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderLineViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPickorderLineRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cPickorderLineEntity pvPickorderLineEntity) {this.Repository.pInsert(pvPickorderLineEntity);}
    public void deleteAll() {this.Repository.pTruncate();}
    public void delete(cPickorderLineEntity pvPickorderLineEntity) {this.Repository.pDelete(pvPickorderLineEntity);}

    public boolean pUpdateQuantityHandledBln(Double pvQuantityHandledDbl) {return this.Repository.pUpdateQuantityHandledBln(pvQuantityHandledDbl);}
    public boolean pUpdateProcessingSequenceBln(String pvProcessingSequenceStr) {return this.Repository.pUpdateProcessingSequenceBln(pvProcessingSequenceStr);}
    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {return this.Repository.pUpdateLocalStatusBln(pvNewStatusInt);}
    public boolean pUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {return this.Repository.pUpdateLocalHandledTimeStampBln(pvHandledTimeStampStr);}

    public cWebresult pPickLineHandledViaWebserviceWrs() {return this.Repository.pPickLineHandledViaWebserviceWrs();}
    public cWebresult pResetViaWebserviceWrs() {return this.Repository.pResetViaWebserviceWrs();}
    public cWebresult pGetSortLocationAdviceViaWebserviceWrs(String pvSourceNoStr) {return this.Repository.pGetSortLocationAdviceViaWebserviceWrs(pvSourceNoStr);}

    public List<cPickorderLineEntity> pGetLinesForSourceNoObl(String pvSourceNoStr) {return this.Repository.pGetLinesForSourceNoObl(pvSourceNoStr);}
    public List<cPickorderLineEntity> pGetSortLineForItemNoAndVariantCodeObl(String pvItemNoStr, String pvVariantCodeStr) {return this.Repository.pGetSortLineForItemNoAndVariantCodeObl(pvItemNoStr, pvVariantCodeStr);}

    public cWebresult pGetArticleImageFromWebserviceWrs(String pvItemNoStr, String pvVariantCodeStr) {return  this.Repository.pGetArticleImageFromWebserviceWrs(pvItemNoStr,pvVariantCodeStr);}

}
