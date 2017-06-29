package creationsofali.teknogia.helpers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;

/**
 * Created by ali on 6/17/17.
 */

public class GridRecyclerView extends RecyclerView {

    public GridRecyclerView(Context context) {
        super(context);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {

        if (layout instanceof GridLayoutManager)
            super.setLayoutManager(layout);
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager) {
            // do magic

            GridLayoutAnimationController.AnimationParameters animationParameters =
                    (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

            int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            final int invertedIndex = count - 1 - index;

            if (animationParameters != null) {
                animationParameters = new GridLayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = animationParameters;

                animationParameters.index = index;
                animationParameters.count = count;

                animationParameters.columnsCount = columns;
                animationParameters.rowsCount = count / columns;

                animationParameters.column = columns - 1 - (invertedIndex % columns);
                animationParameters.row = animationParameters.columnsCount - 1 - (invertedIndex / columns);
            }
        } else
            super.attachLayoutAnimationParameters(child, params, index, count);
    }
}
