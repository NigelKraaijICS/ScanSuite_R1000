package SSU_WHS.LineItemProperty.LineProperty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ICS.Utils.cResult;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleProperty.cArticleProperty;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import nl.icsvertex.scansuite.R;

public class cLineProperty implements List<JSONObject> {

    //Public Properties
    private Integer lineNoInt;
    public Integer getLineNoInt() {return lineNoInt;}

    private String propertyCodeStr;
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

    private Integer sequenceNoHandledInt;
    public Integer getSequenceNoHandledInt() {return sequenceNoHandledInt;}

    private Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return sortingSequenceNoInt;}

    private String layoutStr;
    public String getLayoutStr() {return layoutStr;}

    private boolean isInputBln;
    public boolean getIsInputBln() {return isInputBln;}

    private boolean isRequiredBln;
    public boolean getIsRequiredBln() {return isRequiredBln;}

    private String valueHandledStr;
    public String getValueHandledStr() {return valueHandledStr;}

    private cLinePropertyEntity linePropertyEntity;


    public  static cLineProperty currentLineProperty;
    public static ArrayList<cLineProperty> allLinePropertysObl;

    public List<cLinePropertyValue> propertyValueObl() {

        List<cLinePropertyValue> resultObl = new ArrayList<>();

        if (cLinePropertyValue.allLinePropertysValuesObl == null || cLinePropertyValue.allLinePropertysValuesObl.size() == 0)  {
            return  resultObl;
        }

        for (cLinePropertyValue linePropertyValue : cLinePropertyValue.allLinePropertysValuesObl) {
            if (linePropertyValue.getLineNoInt() == this.getLineNoInt() && linePropertyValue.getPropertyCodeStr().equalsIgnoreCase(this.getPropertyCodeStr())) {
                resultObl.add(linePropertyValue);
            }
        }
        return  resultObl;
    }

    private cLinePropertyViewModel getLinePropertyViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cLinePropertyViewModel.class);
    }

    public cLineProperty (JSONObject pvJsonObject) {
        this.linePropertyEntity = new cLinePropertyEntity(pvJsonObject);
        this.lineNoInt = this.linePropertyEntity.getLineNoInt();
        this.propertyCodeStr = this.linePropertyEntity.getPropertyCodeStr();
        this.sequenceNoHandledInt = this.linePropertyEntity.getSequenceNoHandledInt();
        this.sortingSequenceNoInt = this.linePropertyEntity.getSortingSequenceNoInt();
        this.layoutStr = this.linePropertyEntity.getLayoutStr();
        this.isInputBln = this.linePropertyEntity.getIsInputBln();
        this.isRequiredBln = this.linePropertyEntity.getIsRequiredBln();
        this.valueHandledStr = this.linePropertyEntity.getValueHandledStr();
    }

    public cLineProperty (cArticleProperty articleProperty, int lineNoInt) {
        this.linePropertyEntity = null;
        this.lineNoInt = lineNoInt;
        this.propertyCodeStr = articleProperty.getPropertyCodeStr();
        this.sequenceNoHandledInt = 0;
        this.sortingSequenceNoInt = articleProperty.getSortingSequenceNoInt();
        this.layoutStr = articleProperty.getItemProperty().getLayoutStr();
        this.isInputBln = true;
        this.isRequiredBln = true;
        this.valueHandledStr = "";
    }

    public boolean pInsertInDatabaseBln() {


        if (cLineProperty.allLinePropertysObl == null) {
            cLineProperty.allLinePropertysObl = new ArrayList<>();
        }

        if (!this.getValueHandledStr().equals("")){
            for(cLineProperty lineProperty: cLineProperty.allLinePropertysObl){
                if (lineProperty.getLineNoInt().equals(this.getLineNoInt()) && lineProperty.getPropertyCodeStr().equals(this.getPropertyCodeStr())){
                    if (cLinePropertyValue.allLinePropertysValuesObl == null)  {
                        cLinePropertyValue.allLinePropertysValuesObl = new ArrayList<>();
                    }
                    for (cLinePropertyValue linePropertyValue : cLinePropertyValue.allLinePropertysValuesObl){
                        //Assign value to lineproperty with value
                        if (linePropertyValue.getPropertyCodeStr().equalsIgnoreCase(lineProperty.getPropertyCodeStr()) && linePropertyValue.getLineNoInt() == this.getLineNoInt() && linePropertyValue.getValueStr().equalsIgnoreCase(this.getValueHandledStr())){
                            linePropertyValue.quantityDbl += 1;
                            return true;
                        }
                    }
                    // Propertyvalue not found so make a new one
                    cLinePropertyValue linePropertyValue = new cLinePropertyValue(this.getLineNoInt(), this.getPropertyCodeStr(), this.getValueHandledStr());
                    linePropertyValue.quantityDbl = 1;
                    cLinePropertyValue.allLinePropertysValuesObl.add(linePropertyValue);
                    return true;
                }
            }
            //New Line with values so add
            this.getLinePropertyViewModel().insert(this.linePropertyEntity);
            cLineProperty.allLinePropertysObl.add(this);
            if (cLinePropertyValue.allLinePropertysValuesObl == null)  {
                cLinePropertyValue.allLinePropertysValuesObl = new ArrayList<>();
            }
            // Propertyvalue is always a new one so create
            cLinePropertyValue linePropertyValue = new cLinePropertyValue(this.getLineNoInt(), this.getPropertyCodeStr(), this.getValueHandledStr());
            linePropertyValue.quantityDbl = 1;
            cLinePropertyValue.allLinePropertysValuesObl.add(linePropertyValue);
            return true;
        }

        //New Line no known values so add
        this.getLinePropertyViewModel().insert(this.linePropertyEntity);
        cLineProperty.allLinePropertysObl.add(this);
        return true;
    }

    public void pValueAdded(String pvValueStr) {

        //Try to find value with same value
        for (cLinePropertyValue linePropertyValue : this.propertyValueObl()) {
            if (linePropertyValue.getValueStr().equalsIgnoreCase(pvValueStr)) {
                linePropertyValue.quantityDbl += 1;
                cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                return;
            }
        }

        if (cLinePropertyValue.allLinePropertysValuesObl == null) {
            cLinePropertyValue.allLinePropertysValuesObl = new ArrayList<>();
        }

        //Add a new value
        cLinePropertyValue linePropertyValue = new cLinePropertyValue(this.getLineNoInt(), this.getPropertyCodeStr(),pvValueStr);
        cLinePropertyValue.allLinePropertysValuesObl.add(linePropertyValue);
        cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
    }

    public void pReceiveLineValueAdded(String pvValueStr) {
        //Try to find value with same value
        for (cLinePropertyValue linePropertyValue : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl) {
            if (linePropertyValue.getValueStr().equalsIgnoreCase(pvValueStr)) {
                linePropertyValue.quantityDbl += 1;
                cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                return;
            }
        }

        //Add a new value
        cLinePropertyValue linePropertyValue = new cLinePropertyValue(this.getLineNoInt(), this.getPropertyCodeStr(),pvValueStr);
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl.add(linePropertyValue);
        cLinePropertyValue.currentLinePropertyValue = linePropertyValue;

        for (cLinePropertyValue propertyValue:  cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl){
            if (propertyValue.getPropertyCodeStr().equalsIgnoreCase(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr()) && propertyValue.getValueStr().equals("---")){
                cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl.remove(propertyValue);
                return;
            }
        }
    }

    public cResult pCheckScanForUniquePropertyRst(String pvPropertyValueStr) {

        cResult resultRst = new cResult();
        resultRst.resultBln = true;

        if (!this.getItemProperty().getUniqueBln() || this.propertyValueObl() == null ||this.propertyValueObl().size() ==0 ) {
            resultRst.resultBln = true;
            return  resultRst;
        }



        for (cLinePropertyValue linePropertyValue : this.propertyValueObl()) {

            if (linePropertyValue.getValueStr().equalsIgnoreCase(pvPropertyValueStr)) {

                // We have a match, and this hasn't been scanned already
                if (linePropertyValue.getQuantityDbl() == 0) {
                    resultRst.resultBln = true;
                    return  resultRst;
                }
                else
                {
                    resultRst.resultBln = false;
                    resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_itempropery_value_already_used));
                    return  resultRst;
                }
            }
        }

        resultRst.resultBln = true;
        return  resultRst;

    }

    public boolean pDeleteFromDatabaseBln() {
        if (cLineProperty.allLinePropertysObl != null) {
            cLineProperty.allLinePropertysObl.remove(this);
        }
        return true;
    }

    public static boolean pTruncateTableBln() {

        cLinePropertyViewModel linePropertyViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cLinePropertyViewModel.class);
        linePropertyViewModel.deleteAll();
        cLineProperty.allLinePropertysObl = null;
        return true;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<JSONObject> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(JSONObject jsonObject) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends JSONObject> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends JSONObject> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public JSONObject get(int i) {
        return null;
    }

    @Override
    public JSONObject set(int i, JSONObject jsonObject) {
        return null;
    }

    @Override
    public void add(int i, JSONObject jsonObject) {

    }

    @Override
    public JSONObject remove(int i) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<JSONObject> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<JSONObject> listIterator(int i) {
        return null;
    }

    @NonNull
    @Override
    public List<JSONObject> subList(int i, int i1) {
        return null;
    }
}
