package SSU_WHS.General.Comments;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Pickorders.cPickorder;

public class cComment {


    public Long commentLineNoLng;
    public Long getCommentLineNoLng() {
        return commentLineNoLng;
    }

    public String commentcodeStr;
    public String getCommentcodeStr() {
        return commentcodeStr;
    }

    public String commentTextStr;
    public String getCommentTextStr() {
        return commentTextStr;
    }

    private cCommentEntity commentEntity;

    public static ArrayList<cComment> allCommentsObl;
    public static Boolean commentsShownBln;


    private cCommentViewModel getCommentViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cCommentViewModel.class);
    }

    public cComment (JSONObject pvJsonObject) {
        this.commentEntity = new cCommentEntity(pvJsonObject);
        this.commentLineNoLng = this.commentEntity.getCommentLineNoLng();
        this.commentcodeStr = this.commentEntity.getCommentcodeStr();
        this.commentTextStr = this.commentEntity.getCommentTextStr();
    }

    public boolean pInsertInDatabaseBln() {
        this.getCommentViewModel().insert(this.commentEntity);
        if (cComment.allCommentsObl == null) {
            cComment.allCommentsObl = new ArrayList<>();
        }
        cComment.allCommentsObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln() {

        cCommentViewModel commentViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cCommentViewModel.class);
        commentViewModel.deleteAll();
        return true;
    }

    public static List<cComment> pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu pvFeedbackTypeEnu){

        List<cComment> resultObl = new ArrayList<>();



        for (cComment comment : cPickorder.currentPickOrder.commentsObl()) {

            if (comment.getCommentcodeStr().equalsIgnoreCase(pvFeedbackTypeEnu.toString())) {
                resultObl.add(comment);
            }
        }
        return  resultObl;
    }
}


