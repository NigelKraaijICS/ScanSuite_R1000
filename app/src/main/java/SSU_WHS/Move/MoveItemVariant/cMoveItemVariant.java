package SSU_WHS.Move.MoveItemVariant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import SSU_WHS.Move.MoveorderLines.cMoveorderLine;

public class cMoveItemVariant {

    public static LinkedHashMap<String, cMoveItemVariant> allMoveItemVariantObl;

    public String itemNoStr;
    public String getItemNoStr() {
        return this.itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return this.variantCodeStr;
    }

    public double quantityTakenDbl;
    public double getQuantityTakenDbl() {
        return this.quantityTakenDbl;
    }

    public double quantityPlacedDbl;
    public double getQuantityPlacedDbl() {
        return this.quantityPlacedDbl;
    }


    public String handledTimeStampStr;
    public String getHandledTimeStampStr(){ return  this.handledTimeStampStr;}

    public  String getKeyStr() {
        return  this.getItemNoStr() + "þ" + this.getVariantCodeStr();
    }

    public  double getQuantityTodoPlaceDbl(){
        return  this.getQuantityTakenDbl() - this.getQuantityPlacedDbl();
    }

    public  cMoveorderLine linePlaceTodoLine() {

        cMoveorderLine moveorderLine = new cMoveorderLine(this.getItemNoStr(), this.getVariantCodeStr());

        if (this.linesObl != null && this.linesObl.size() > 0) {
            moveorderLine.descriptionStr = this.linesObl.get(0).getDescriptionStr();
            moveorderLine.description2Str = this.linesObl.get(0).getDescription2Str();

            if (this.linesObl.get(0).barcodesObl != null && this.linesObl.get(0).barcodesObl.size() > 0) {
                moveorderLine.barcodesObl = new ArrayList<>();
                moveorderLine.barcodesObl.add(this.linesObl.get(0).barcodesObl.get(0));
            }

            moveorderLine.quantityDbl = this.getQuantityTakenDbl();
            moveorderLine.quantityHandledDbl = this.getQuantityPlacedDbl();
            moveorderLine.handledTimeStampStr = this.getHandledTimeStampStr();



        }

        return  moveorderLine;
    }

    public  List<cMoveorderLine> linesObl;

    public cMoveItemVariant(String pvItemNoStr, String pvVariantCodeStr) {
        this.itemNoStr = pvItemNoStr;
        this.variantCodeStr = pvVariantCodeStr;
    }




}
