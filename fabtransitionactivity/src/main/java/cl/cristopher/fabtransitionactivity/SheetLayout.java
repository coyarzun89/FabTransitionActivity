package cl.cristopher.fabtransitionactivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.codetail.animation.SupportAnimator;

public class SheetLayout extends FrameLayout implements View.OnTouchListener {

    private static final int DEFAULT_ANIMATION_DURATION = 350;
    private static final int DEFAULT_FAB_SIZE = 56;

    private static final int FAB_CIRCLE = 0;
    private static final int FAB_EXPAND = 1;

    @IntDef({FAB_CIRCLE, FAB_EXPAND})
    private @interface Fab {
    }

    private LinearLayout mFabExpandLayout;
    private ImageView mFab;

    int mFabType = FAB_CIRCLE;
    boolean mAnimatingFab = false;
    private int animationDuration;
    private int mFabSize;

    OnFabAnimationEndListener mListener;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        contractFab();
        return true;
    }

    public SheetLayout(Context context) {
        super(context);
        init();
    }

    public SheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadAttributes(context, attrs);
    }

    public SheetLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        loadAttributes(context, attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.bottom_sheet_layout, this);
        mFabExpandLayout = ((LinearLayout) findViewById(R.id.container));
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        TypedValue outValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        // use ?attr/colorPrimary as background color
        theme.resolveAttribute(R.attr.colorPrimary, outValue, true);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FooterLayout,
                0, 0);

        int containerGravity;
        try {
            setColor(a.getColor(R.styleable.FooterLayout_ft_color,
                    outValue.data));
            animationDuration = a.getInteger(R.styleable.FooterLayout_ft_anim_duration,
                    DEFAULT_ANIMATION_DURATION);
            containerGravity = a.getInteger(R.styleable.FooterLayout_ft_container_gravity, 1);
            mFabSize = a.getInteger(R.styleable.FooterLayout_ft_fab_type, DEFAULT_FAB_SIZE);
        } finally {
            a.recycle();
        }

        mFabExpandLayout.setGravity(getGravity(containerGravity));
    }

    public void setFab(ImageView imageView) {
        mFab = imageView;
    }

    private int getGravity(int gravityEnum) {
        return (gravityEnum == 0 ? Gravity.START
                : gravityEnum == 1 ? Gravity.CENTER_HORIZONTAL : Gravity.END)
                | Gravity.CENTER_VERTICAL;
    }

    public void setColor(int color) {
        mFabExpandLayout.setBackgroundColor(color);
    }

    @Override
    public void addView(@NonNull View child) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@NonNull View child, int width, int height) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child, width, height);
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    /**
     * hide() and show() methods are useful for remembering the toolbar state on screen rotation.
     */
    public void hide() {
        mFabExpandLayout.setVisibility(View.INVISIBLE);
        mFabType = FAB_CIRCLE;
    }

    public void show() {
        mFabExpandLayout.setVisibility(View.VISIBLE);
        mFabType = FAB_EXPAND;
    }

    private boolean canAddViewToContainer() {
        return mFabExpandLayout != null;
    }

    public void expandFab() {
        mFabType = FAB_EXPAND;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            expandPreLollipop();
        } else {
            expandLollipop();
        }
    }

    public void contractFab() {
        if (!isFabExpanded()) {
            return;
        }

        mFabType = FAB_CIRCLE;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            contractPreLollipop();
        } else {
            contractLollipop();
        }
    }

    public boolean isFabExpanded() {
        return mFabType == FAB_EXPAND;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void expandLollipop() {
        mAnimatingFab = true;

        // Center point on the screen of the FAB after translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab));
        int y = (int) (ViewUtils.centerY(mFab));

        // Start and end radii of the toolbar expand animation.
        float startRadius = getFabSizePx() / 2;
        float endRadius = (float) Math.hypot(
                Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        mFabExpandLayout.setAlpha(0f);
        mFabExpandLayout.setVisibility(View.VISIBLE);

        mFab.setVisibility(View.INVISIBLE);
        mFab.setTranslationX(0f);
        mFab.setTranslationY(0f);
        mFab.setAlpha(1f);

        Animator toolbarExpandAnim = ViewAnimationUtils.createCircularReveal(
                mFabExpandLayout, x, y, startRadius, endRadius);
        toolbarExpandAnim.setStartDelay(animationDuration / 2);
        toolbarExpandAnim.setDuration(animationDuration / 2);
        toolbarExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFabExpandLayout.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatingFab = false;
                if(mListener!=null)
                    mListener.onFabAnimationEnd();

            }
        });

        toolbarExpandAnim.start();
    }

    private void expandPreLollipop() {
        mAnimatingFab = true;

        // Center point on the screen of the FAB after translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab));
        int y = (int) (ViewUtils.centerY(mFab));

        // Start and end radii of the toolbar expand animation.
        float startRadius = getFabSizePx() / 2;
        float endRadius = (float) Math.hypot(
                Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        mFabExpandLayout.setAlpha(0f);
        mFabExpandLayout.setVisibility(View.VISIBLE);

        mFab.setVisibility(View.INVISIBLE);
        mFab.setTranslationX(0f);
        mFab.setTranslationY(0f);
        mFab.setAlpha(0f);

        final SupportAnimator toolbarExpandAnim = io.codetail.animation.ViewAnimationUtils
                .createCircularReveal(
                        mFabExpandLayout, x, y, startRadius, endRadius);
        toolbarExpandAnim.setDuration(animationDuration / 2);

        toolbarExpandAnim.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                mFabExpandLayout.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd() {
                mFab.setAlpha(1f);
                mAnimatingFab = false;
                if (mListener != null)
                    mListener.onFabAnimationEnd();

            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });


        // Play toolbar expand animation after slide animations finish.
        toolbarExpandAnim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void contractLollipop() {
        mAnimatingFab = true;

        mFab.setAlpha(0f);
        //mFab.setTranslationX(dx);
        //mFab.setTranslationY(dy);
        mFab.setVisibility(View.VISIBLE);

        // Center point on the screen of the FAB before translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab));
        int y = (int) (ViewUtils.centerY(mFab));

        // Start and end radii of the toolbar contract animation.
        float endRadius = getFabSizePx() / 2;
        float startRadius = (float) Math.hypot(
                Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));



        Animator toolbarContractAnim = ViewAnimationUtils.createCircularReveal(
                mFabExpandLayout, x, y, startRadius, endRadius);
        toolbarContractAnim.setDuration(animationDuration / 2);


        toolbarContractAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFab.setAlpha(1f);
                mFabExpandLayout.setAlpha(0f);

                mAnimatingFab = false;
                mFabExpandLayout.setVisibility(View.INVISIBLE);
                mFabExpandLayout.setAlpha(1f);
            }
        });

        toolbarContractAnim.start();
    }

    private void contractPreLollipop() {
        mAnimatingFab = true;

        mFab.setAlpha(0f);
        mFab.setVisibility(View.VISIBLE);

        // Center point on the screen of the FAB before translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab));
        int y = (int) (ViewUtils.centerY(mFab));

        // Start and end radii of the toolbar contract animation.
        float endRadius = getFabSizePx() / 2;
        float startRadius = (float) Math.hypot(
                Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        final SupportAnimator toolbarContractAnim = io.codetail.animation.ViewAnimationUtils
                .createCircularReveal(
                        mFabExpandLayout, x, y, startRadius, endRadius);
        toolbarContractAnim.setDuration(animationDuration / 2);

        toolbarContractAnim.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                mFab.setAlpha(1f);
                mFabExpandLayout.setAlpha(0f);

                mFabExpandLayout.setVisibility(View.INVISIBLE);
                mFabExpandLayout.setAlpha(1f);
                mAnimatingFab = false;
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });

        toolbarContractAnim.start();
    }

    private int getFabSizePx() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(mFabSize * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public interface OnFabAnimationEndListener {
        void onFabAnimationEnd();
    }

    public void setFabAnimationEndListener(OnFabAnimationEndListener eventListener) {
        mListener = eventListener;
    }
}
