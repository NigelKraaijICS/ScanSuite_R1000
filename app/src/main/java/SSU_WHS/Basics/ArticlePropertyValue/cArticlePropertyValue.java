package SSU_WHS.Basics.ArticlePropertyValue;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleProperty.cArticleProperty;
import SSU_WHS.Basics.ItemProperty.cItemProperty;

public class cArticlePropertyValue {

    //Public Properties
    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }


    private String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    public cArticleProperty getArticleProperty(){
        if (this.getPropertyCodeStr().isEmpty() || cArticle.currentArticle.propertyObl == null || cArticle.currentArticle.propertyObl.size() == 0) {
            return  null;
        }

        for (cArticleProperty articleProperty :  cArticle.currentArticle.propertyObl) {
            if (articleProperty.getPropertyCodeStr().equalsIgnoreCase(this.getPropertyCodeStr())) {
                return  articleProperty;
            }
        }

        return  null;
    }

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

    public String valueStr;
    public String getValueStr() {return valueStr;}

    private int sortingSequenceNoInt;
    public  int getSortingSequenceNoInt(){return sortingSequenceNoInt;}

    public static cArticlePropertyValue currentArticlePropertyValue;


    public cArticlePropertyValue(JSONObject pvJsonObject , cArticle pvArticle) {
        cArticlePropertyValueEntity articlePropertyValueEntity = new cArticlePropertyValueEntity(pvJsonObject);
        this.itemNoStr = pvArticle.getItemNoStr();
        this.variantCodeStr = pvArticle.getVariantCodeStr();
        this.propertyCodeStr = articlePropertyValueEntity.getPropertyCodeStr();
        this.valueStr = articlePropertyValueEntity.getValueStr();
        this.sortingSequenceNoInt = articlePropertyValueEntity.getSortingSequenceNoInt();
    }

    public cArticlePropertyValue(cArticleProperty pvArticleProperty) {
        this.itemNoStr = pvArticleProperty.getItemNoStr();
        this.variantCodeStr = pvArticleProperty.getVariantCodeStr();
        this.propertyCodeStr = pvArticleProperty.getPropertyCodeStr();
        this.valueStr =  "";
        this.sortingSequenceNoInt = pvArticleProperty.getSortingSequenceNoInt();
    }

}
