package SSU_WHS.General.Comments;

import androidx.lifecycle.ViewModelProviders;

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

    public cCommentEntity commentEntity;
    public boolean inDatabaseBln;

    public static ArrayList<cComment> allCommentsObl;
    public static Boolean commentsShownBln;

    public static cCommentViewModel commentViewModel;
       public static cCommentViewModel getCommentViewModel() {
        if (commentViewModel == null) {
            commentViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cCommentViewModel.class);
        } return commentViewModel;
    }

    public static cCommentAdapter commentAdapter;
    public static cCommentAdapter getcommentAdapter() {
        if (commentAdapter == null) {
            commentAdapter = new cCommentAdapter();
        } return commentAdapter;
    }

    public cComment (JSONObject pvJsonObject) {
        this.commentEntity = new cCommentEntity(pvJsonObject);
        this.commentLineNoLng = this.commentEntity.getCommentLineNoLng();
        this.commentcodeStr = this.commentEntity.getCommentcodeStr();
        this.commentTextStr = this.commentEntity.getCommentTextStr();
    }

    public boolean pInsertInDatabaseBln() {
        cComment.getCommentViewModel().insert(this.commentEntity);
        this.inDatabaseBln = true;
        if (cComment.allCommentsObl == null) {
            cComment.allCommentsObl = new ArrayList<>();
        }
        cComment.allCommentsObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln() {
        cComment.getCommentViewModel().deleteAll();
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


