package SSU_WHS.Basics.LabelTemplate;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.cWorkplaceViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cLabelTemplate {
    //region Public Propties

    private final String labelcodeStr;
    public String getLabelcodeStr() {
        return labelcodeStr;
    }
    private final String templateStr;
    public String getTemplateStr(){ return templateStr;}
    //end region Public Propties

    private final cLabelTemplateEntity labelTemplateEntity;

    public static ArrayList<cLabelTemplate> allLabelTemplateObl;
    public static ArrayList<cLabelTemplate> adressTemplateObl;
    public static ArrayList<cLabelTemplate> contentTemplateObl;
    public static ArrayList<cLabelTemplate> itemTemplateObl;
    public static ArrayList<cLabelTemplate> binTemplateObl;
    public static ArrayList<cLabelTemplate> containerTemplateObl;

    public static cLabelTemplate currentLabelTemplate;

    private cLabelTemplateViewModel getLabelTemplateViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cLabelTemplateViewModel.class);
    }

    //Region Constructor
    public cLabelTemplate(JSONObject pvJsonObject) {
        this.labelTemplateEntity = new cLabelTemplateEntity(pvJsonObject);
        this.labelcodeStr = labelTemplateEntity.getLabelcodeStr();
        this.templateStr = labelTemplateEntity.getTemplateStr();
    }

    public cLabelTemplate(String pvLabelcodeStr, String pvTemplateStr) {
        this.labelTemplateEntity = null;
        this.labelcodeStr = pvLabelcodeStr;
        this.templateStr = pvTemplateStr;
    }

    //End Region Constructor

    public enum labelTemplateEnu {
        BINLABEL,
        ITEMLABEL,
        CONTENTLABEL,
        CONTAINERLABEL,
        ADDRESSLABEL
    }


    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getLabelTemplateViewModel().insert(this.labelTemplateEntity);

        if (cLabelTemplate.allLabelTemplateObl == null){
            cLabelTemplate.allLabelTemplateObl = new ArrayList<>();
            cLabelTemplate.contentTemplateObl = new ArrayList<>();
            cLabelTemplate.containerTemplateObl = new ArrayList<>();
            cLabelTemplate.binTemplateObl = new ArrayList<>();
            cLabelTemplate.adressTemplateObl = new ArrayList<>();
            cLabelTemplate.itemTemplateObl = new ArrayList<>();
        }
        cLabelTemplate.allLabelTemplateObl.add(this);
        switch (this.labelTemplateEntity.getLabelcodeStr()){
            case "ADRESSLABEL":
                cLabelTemplate.adressTemplateObl.add(this);
                break;
            case "BINLABEL":
                cLabelTemplate.binTemplateObl.add(this);
                break;
            case "CONTAINERLABEL":
                cLabelTemplate.containerTemplateObl.add(this);
                break;
            case "CONTENTLABEL":
                cLabelTemplate.contentTemplateObl.add(this);
                break;
            case "ITEMLABEL":
                cLabelTemplate.itemTemplateObl.add(this);
                break;
        }
        return true;
    }

    public static cLabelTemplate pGetLabelTemplateByName(ArrayList<cLabelTemplate> templateObl ,String pvTemplateStr){
        if(templateObl == null){
            return null;
        }

        for (cLabelTemplate labelTemplate : templateObl)
            if (labelTemplate.templateStr.equalsIgnoreCase(pvTemplateStr)) {
                return labelTemplate;
            }
        return null;
    }

    public static boolean pTruncateTableBln(){

        cLabelTemplateViewModel labelTemplateViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cLabelTemplateViewModel.class);
        labelTemplateViewModel.deleteAll();
        return true;
    }

    public static boolean pGetLabelTemplatesViaWebserviceBln() {

        cLabelTemplate.allLabelTemplateObl = null;
        cLabelTemplate.itemTemplateObl = null;
        cLabelTemplate.adressTemplateObl = null;
        cLabelTemplate.binTemplateObl = null;
        cLabelTemplate.containerTemplateObl = null;
        cLabelTemplate.contentTemplateObl = null;
        cLabelTemplate.pTruncateTableBln();

        cWebresult WebResult;

        cLabelTemplateViewModel labelTemplateViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cLabelTemplateViewModel.class);

        WebResult =  labelTemplateViewModel.pGetLabelTemplatesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject :  WebResult.getResultDtt()) {
                cLabelTemplate labelTemplate = new cLabelTemplate(jsonObject);
                labelTemplate.pInsertInDatabaseBln();
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETLABELTEMPLATES);
            return  false;
        }
    }

    //End Region Public Methods
}
