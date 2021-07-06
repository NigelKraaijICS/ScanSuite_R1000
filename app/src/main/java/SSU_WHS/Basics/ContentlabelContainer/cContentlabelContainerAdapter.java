package SSU_WHS.Basics.ContentlabelContainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.R;

public class cContentlabelContainerAdapter  extends RecyclerView.Adapter<SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainerAdapter.commentViewHolder>{

        private List<cContentlabelContainer> contentlabelContainerObl;

        public static class commentViewHolder extends RecyclerView.ViewHolder{
            private final TextView textViewContainerNo;
            private final AppCompatImageView imageViewCurrentContainer;

            public LinearLayout containerLinearLayout;
            public ConstraintLayout viewForeground;

            public commentViewHolder(View pvView) {
                super(pvView);
                AppCompatImageView imageViewContainer = pvView.findViewById(R.id.imageViewContainer);
                this.imageViewCurrentContainer = pvView.findViewById(R.id.imageViewCurrentContainer);
                this.textViewContainerNo = pvView.findViewById(R.id.textViewContainerNo);
                View menuSeparator = pvView.findViewById(R.id.menuSeparator);
                this.containerLinearLayout = pvView.findViewById(R.id.containerLinearLayout);
            }
        }

        //Region Private Properties
        private final LayoutInflater layoutInflaterObject;
        private final List<LinearLayout> itemPropertyValueLinearLayoutObl = new ArrayList<>();

        //End Region Private Properties

        //Region Constructor
        public cContentlabelContainerAdapter() {
            this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
        }
        //End Region Constructor

        @NonNull
        @Override
        public SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainerAdapter.commentViewHolder onCreateViewHolder(@NonNull ViewGroup pvParentVieGroup, int pvViewTypeInt) {
            View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_container, pvParentVieGroup, false);
            return new SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainerAdapter.commentViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainerAdapter.commentViewHolder pvHolder, int pvPositionInt) {

            this.itemPropertyValueLinearLayoutObl.add(pvHolder.containerLinearLayout);


            final cContentlabelContainer contentlabelContainer = this.contentlabelContainerObl.get(pvPositionInt);
            pvHolder.textViewContainerNo.setText(cText.pLongToStringStr( contentlabelContainer.getContainerSequencoNoLng()));
            if (contentlabelContainer.getContainerSequencoNoLng().equals(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng())){
                pvHolder.imageViewCurrentContainer.setVisibility(View.VISIBLE);
            } else {
                pvHolder.imageViewCurrentContainer.setVisibility(View.INVISIBLE);
            }

                pvHolder.containerLinearLayout.setOnClickListener(pvView -> {
                    for (LinearLayout linearLayout : itemPropertyValueLinearLayoutObl){
                        linearLayout.setSelected(false);
                    }
                    pvView.setSelected(true);
                    cContentlabelContainer.currentContentlabelContainer = contentlabelContainer;

                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                        if (cPickorderLine.currentPickOrderLine.containerObl == null){
                            cPickorderLine.currentPickOrderLine.containerObl = new ArrayList<>();
                        }
                        if (cPickorderLine.currentPickOrderLine.containerObl.size() == 0){
                            //No containers added to the line so make one
                            cContentlabelContainer contentlabelContainerNew = new cContentlabelContainer(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng(), 0.0);
                            cPickorderLine.currentPickOrderLine.containerObl.add(contentlabelContainerNew);
                            cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainerNew;
                        } else {
                            boolean foundBln = false;
                            if(!cPickorderLine.currentPickOrderLine.currentContainer.getContainerSequencoNoLng().equals(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng())){
                                //check if container is in the list
                                for (cContentlabelContainer contentlabelContainerNew : cPickorderLine.currentPickOrderLine.containerObl){
                                    if (contentlabelContainerNew.getContainerSequencoNoLng().equals(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng())){
                                        cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainerNew;
                                        foundBln = true;
                                    }
                                }
                                if (!foundBln){
                                    //Not found add new container to list
                                    cContentlabelContainer contentlabelContainerNew = new cContentlabelContainer(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng(), 0.0);
                                    cPickorderLine.currentPickOrderLine.containerObl.add(contentlabelContainerNew);
                                    cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainerNew;
                                }
                            }
                        }
                    }

                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pRefreshArticleInfo();
                    }

                    if (cAppExtension.activity instanceof PickorderPickActivity){
                        PickorderPickActivity pickorderPickActivity = (PickorderPickActivity) cAppExtension.activity;
                        pickorderPickActivity.pRefreshArticleInfo();
                    }


                    cAppExtension.dialogFragment.dismiss();
                });
            }


        public void pFillData(List<cContentlabelContainer> pvDataObl) {
            this.contentlabelContainerObl = pvDataObl;
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount () {
            if (this.contentlabelContainerObl != null)
                return this.contentlabelContainerObl.size();
            else return 0;
        }
}
