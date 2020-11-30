package SSU_WHS.Basics.Translations;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.cWorkplaceViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cTranslation {

    //region Public Properties
    private String textStr;
    public String getTextStr() {
        return textStr;
    }

    private String languageStr;
    public String getLanguageStr() {
        return languageStr;
    }

    private String translationStr;
    public String getTranslationStr() {
        return translationStr;
    }

    private cTranslationEntity translationEntity;

    public static ArrayList<cTranslation> allTranslationsObl;
    public static Boolean translationsAvailableBln;

    private cTranslationsViewModel getTranslationsViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cTranslationsViewModel.class);
    }

    //end region Public Propties

     //Region Constructor
     public cTranslation(JSONObject pvJsonObject) {
        this.translationEntity = new cTranslationEntity(pvJsonObject);
        this.textStr = this.translationEntity.getTextStr();
        this.languageStr =  this.translationEntity.getLanguageStr();
        this.translationStr = this.translationEntity.getTranslationStr();
    }



    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
         this.getTranslationsViewModel().insert(this.translationEntity);

        if (cTranslation.allTranslationsObl == null){
            cTranslation.allTranslationsObl = new ArrayList<>();
        }
        cTranslation.allTranslationsObl.add(this);
        return true;
    }

    public static String pGetTranslastionStr(String pvTextStr, String pvLanguageStr){

        if(cTranslation.allTranslationsObl == null){
            return pvTextStr;
        }



        for (cTranslation translation : cTranslation.allTranslationsObl) {
            if (!translation.getTextStr().equalsIgnoreCase(pvTextStr) || !translation.getLanguageStr().equalsIgnoreCase(pvLanguageStr)) {
                continue;
            }
            return  translation.getTranslationStr();
        }

        return  pvTextStr;
    }

    public static boolean pTruncateTableBln(){
        cTranslationsViewModel translationsViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cTranslationsViewModel.class);
        translationsViewModel.deleteAll();
        return true;
    }

    public static boolean pGetTranslationsViaWebserviceBln() {

        cTranslation.allTranslationsObl = null;
        cTranslation.pTruncateTableBln();

        cWebresult WebResult;

        cTranslationsViewModel translationsViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cTranslationsViewModel.class);

        WebResult =  translationsViewModel.pGetTranslationsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject :  WebResult.getResultDtt()) {
                cTranslation translation = new cTranslation(jsonObject);
                translation.pInsertInDatabaseBln();
            }

            cTranslation.translationsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETWORKPLACES);
            return  false;
        }
    }

    //End Region Public Methods
}
