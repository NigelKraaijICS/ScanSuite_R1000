package SSU_WHS.Basics.ArticleProperty;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ItemProperty.cItemProperty;

public class cArticleProperty {
    //Public Properties
    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    private final String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    public cItemProperty getItemProperty() {

        if (this.getPropertyCodeStr().isEmpty() || cItemProperty.allItemPropertiesObl == null || cItemProperty.allItemPropertiesObl.size() == 0) {
            return  null;
        }

        for (cItemProperty itemProperty :  cItemProperty.allItemPropertiesObl) {
            if (itemProperty.getPropertyStr().equalsIgnoreCase(this.getPropertyCodeStr())) {
                return  itemProperty;
            }
        }
        return  null;
    }

    private final Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return sortingSequenceNoInt;}

    private final String inputWorkflowsStr;
    public String getInputWorkflowsStr(){return inputWorkflowsStr;}

    public ArrayList<String> InputWorkflowObl(){
        return new ArrayList<String>(Arrays.asList( getInputWorkflowsStr().split(";")));
    }

    private final String requiredWorkFlowsStr;
    public String getRequiredWorkFlowsStr() {return this.requiredWorkFlowsStr;}

    public ArrayList<String> RequiredWorkflowObl(){

        return new ArrayList<String>(Arrays.asList( getRequiredWorkFlowsStr().split(";")));
    }

    public cArticleProperty (JSONObject pvJsonObject , cArticle pvArticle) {
        cArticlePropertyEntity articlePropertyEntity = new cArticlePropertyEntity(pvJsonObject);

        this.itemNoStr = pvArticle.getItemNoStr();
        this.variantCodeStr = pvArticle.getVariantCodeStr();
        this.propertyCodeStr = articlePropertyEntity.getPropertyCodeStr();
        this.sortingSequenceNoInt = articlePropertyEntity.getSortingSequenceNoInt();
        this.inputWorkflowsStr = articlePropertyEntity.getInputWorkflowsStr();
        this.requiredWorkFlowsStr = articlePropertyEntity.getRequiredWorkFlowsStr();
    }
}
