package SSU_WHS.General.Comments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cCommentViewModel  extends AndroidViewModel {
    public cCommentRepository commentRepository;

    public cCommentViewModel(Application pvApplication) {
        super(pvApplication);
        commentRepository = new cCommentRepository(pvApplication);
    }
    public void deleteAll() {this.commentRepository.pDeleteAll();}
    public void insert(cCommentEntity pvCommentEntity) {commentRepository.pInsert(pvCommentEntity);}

}
