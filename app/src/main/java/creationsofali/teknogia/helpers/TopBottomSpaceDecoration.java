package creationsofali.teknogia.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Desc: The class for adding margin decoration to RecyclerView
 *       by calling #addItemDecoration to RecyclerView object.
 *       This decoration will add top margin to the first child
 *       and bottom margin to the last child, if the list is vertical.
 *       And start margin to the first child and end margin to the
 *       last child, if the list is horizontal.
 * Author: Ali
 * Date 21 April 17.
 */

public class TopBottomSpaceDecoration extends RecyclerView.ItemDecoration {

    private int bottomSpace, topSpace;

    public TopBottomSpaceDecoration(int topSpace, int bottomSpace) {
        this.bottomSpace = bottomSpace;
        this.topSpace = topSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // bottomPadding
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomSpace;
            outRect.top = 0;
        }
        // topPadding
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.bottom = 0;
            outRect.top = topSpace;
        }

    }
}
