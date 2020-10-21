package SSU_WHS.Basics.IdentifierWithDestination;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Workplaces.cWorkplaceViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cIdentifierWithDestination {

    //region Public Properties
    private String indentifierStr;
    public String getIndentifierStr() {
        return indentifierStr;
    }

    public String destinationStr;
    public String getDestinationStr() {
        return destinationStr;
    }

    public List<cIdentifierInfo> infoObl;



    public  static cIdentifierWithDestination currentIdentifier;

    private cWorkplaceViewModel getWorkplaceViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWorkplaceViewModel.class);
    }

    //end region Public Propties

     //Region Constructor
     public cIdentifierWithDestination(List<JSONObject> pvJsonObjectObl) {

        this.infoObl = new ArrayList<>();

        for (JSONObject jsonObject : pvJsonObjectObl) {
            cIdentifierWithDestinationEntity identifierWithDestinationEntity = new cIdentifierWithDestinationEntity((jsonObject));
            this.indentifierStr = identifierWithDestinationEntity.getIdentifierStr();
            this.destinationStr = identifierWithDestinationEntity.getDestinationStr();
            this.infoObl.add((new cIdentifierInfo(identifierWithDestinationEntity.getInfoKeyStr(), identifierWithDestinationEntity.getInfoValueStr())));
        }

    }

    //End Region Constructor

    //Region Public Methods

    public static boolean pGetIdentifierViaWebserviceBln(String  pvIdentifierStr) {

        cWebresult WebResult;

        cIdentifierWithDestinationViewModel identifierWithDestinationViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cIdentifierWithDestinationViewModel.class);

        WebResult =  identifierWithDestinationViewModel.pGetIdentifierFromWebserviceWrs(pvIdentifierStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            cIdentifierWithDestination.currentIdentifier = new cIdentifierWithDestination(WebResult.getResultDtt());
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETIDENTIFIERWITHDESTINATION);
            return  false;
        }
    }

    //End Region Public Methods
}
