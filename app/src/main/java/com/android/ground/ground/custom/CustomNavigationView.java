package com.android.ground.ground.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.ground.ground.R;
import com.android.ground.ground.view.OnCustomImageClickListener;
import com.android.ground.ground.view.person.main.MVPview;
import com.android.ground.ground.view.person.main.NavigationHeaderView;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class CustomNavigationView  extends ScrimInsetsFrameLayout {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};

    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;

    private final NavigationMenu mMenu;
    private final CustomNavigationMenuPresenter mPresenter = new CustomNavigationMenuPresenter();

    private OnNavigationItemSelectedListener mListener;
    private int mMaxWidth;

    private MenuInflater mMenuInflater;

    ImageView imageView;
    public CustomNavigationView(Context context) {
        this(context, null);
    }

    public CustomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



        ThemeUtils2.checkAppCompatTheme(context);

        // Create the menu
        mMenu = new NavigationMenu(context);



        // Custom attributes
        TypedArray a = context.obtainStyledAttributes(attrs,
                android.support.design.R.styleable.NavigationView, defStyleAttr,
                android.support.design.R.style.Widget_Design_NavigationView);


        //noinspection deprecation
        setBackgroundDrawable(a.getDrawable(android.support.design.R.styleable.NavigationView_android_background));
        if (a.hasValue(android.support.design.R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation(this, a.getDimensionPixelSize(
                    android.support.design.R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows(this,
                a.getBoolean(android.support.design.R.styleable.NavigationView_android_fitsSystemWindows, false));

        mMaxWidth = a.getDimensionPixelSize(android.support.design.R.styleable.NavigationView_android_maxWidth, 0);

        final ColorStateList itemIconTint;
        if (a.hasValue(android.support.design.R.styleable.NavigationView_itemIconTint)) {
            itemIconTint = a.getColorStateList(android.support.design.R.styleable.NavigationView_itemIconTint);
        } else {
            itemIconTint = createDefaultColorStateList(android.R.attr.textColorSecondary);
        }

        boolean textAppearanceSet = false;
        int textAppearance = 0;
        if (a.hasValue(android.support.design.R.styleable.NavigationView_itemTextAppearance)) {
            textAppearance = a.getResourceId(android.support.design.R.styleable.NavigationView_itemTextAppearance, 0);
            textAppearanceSet = true;
        }

        ColorStateList itemTextColor = null;
        if (a.hasValue(android.support.design.R.styleable.NavigationView_itemTextColor)) {
            itemTextColor = a.getColorStateList(android.support.design.R.styleable.NavigationView_itemTextColor);
        }

        if (!textAppearanceSet && itemTextColor == null) {
            // If there isn't a text appearance set, we'll use a default text color
            itemTextColor = createDefaultColorStateList(android.R.attr.textColorPrimary);
        }

        final Drawable itemBackground = a.getDrawable(android.support.design.R.styleable.NavigationView_itemBackground);

        mMenu.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                return mListener != null && mListener.onNavigationItemSelected(item);
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {}
        });
        mPresenter.setId(PRESENTER_NAVIGATION_VIEW_ID);
        mPresenter.initForMenu(context, mMenu);
        mPresenter.setItemIconTintList(itemIconTint);
        if (textAppearanceSet) {
            mPresenter.setItemTextAppearance(textAppearance);
        }
        mPresenter.setItemTextColor(itemTextColor);
        mPresenter.setItemBackground(itemBackground);
        mMenu.addMenuPresenter(mPresenter);
        addView((View) mPresenter.getMenuView(this));

        if (a.hasValue(android.support.design.R.styleable.NavigationView_menu)) {
            inflateMenu(a.getResourceId(android.support.design.R.styleable.NavigationView_menu, 0));
        }

        if (a.hasValue(android.support.design.R.styleable.NavigationView_headerLayout)) {
            inflateHeaderView(a.getResourceId(android.support.design.R.styleable.NavigationView_headerLayout, 0));
        }
        if (a.hasValue(android.support.design.R.styleable.NavigationView_headerLayout)) {
            inflateFooterView(a.getResourceId(android.support.design.R.styleable.NavigationView_headerLayout, 0));
        }

        //Header
        NavigationHeaderView headerView = new NavigationHeaderView(getContext());
        addHeaderView(headerView);
        headerView.setOnCustomImageClickListener(new OnCustomImageClickListener() {
            @Override
            public void OnCustomImageClick(View view) {
                mImageListener.onHeaderImageClicked(view);
            }
        });
        MVPview headerView2 = new MVPview(getContext());
        addFooterView(headerView2);
        a.recycle();


    }

//    public interface OnHeaderItemSelectedListener {
//
//        /**
//         * Called when an item in the navigation menu is selected.
//               *
//         * @return true to display the item as the selected item
//         */
//        public boolean onHeaderItemSelected();
//    }
//
//    /**
//     * User interface state that is stored by NavigationView for implementing
//     * onSaveInstanceState().
//     */
//    private OnHeaderItemSelectedListener mListener;
//    public void setHeaderItemSelectedListener(OnHeaderItemSelectedListener listener) {
//        mListener = listener;
//    }

    public interface OnHeaderImageClickedListener{
        public void onHeaderImageClicked(View view);
    }
    OnHeaderImageClickedListener mImageListener;
    public void setHeaderImageClickedListener(OnHeaderImageClickedListener listener) {
            mImageListener = listener;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState state = new SavedState(superState);
        state.menuState = new Bundle();
        mMenu.savePresenterStates(state.menuState);
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable savedState) {
        SavedState state = (SavedState) savedState;
        super.onRestoreInstanceState(state.getSuperState());
        mMenu.restorePresenterStates(state.menuState);
    }

    /**
     * Set a listener that will be notified when a menu item is clicked.
     *
     * @param listener The listener to notify
     */
    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        switch (MeasureSpec.getMode(widthSpec)) {
            case MeasureSpec.EXACTLY:
                // Nothing to do
                break;
            case MeasureSpec.AT_MOST:
                widthSpec = MeasureSpec.makeMeasureSpec(
                        Math.min(MeasureSpec.getSize(widthSpec), mMaxWidth), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                widthSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
                break;
        }
        // Let super sort out the height
        super.onMeasure(widthSpec, heightSpec);
    }


    /**
     * Inflate a menu resource into this navigation view.
     *
     * <p>Existing items in the menu will not be modified or removed.</p>
     *
     * @param resId ID of a menu resource to inflate
     */
    public void inflateMenu(int resId) {
        mPresenter.setUpdateSuspended(true);
        getMenuInflater().inflate(resId, mMenu);
        mPresenter.setUpdateSuspended(false);
        mPresenter.updateMenuView(false);
    }

    /**
     * Returns the {@link Menu} instance associated with this navigation view.
     */
    public Menu getMenu() {
        return mMenu;
    }

    /**
     * Inflates a View and add it as a header of the navigation menu.
     *
     * @param res The layout resource ID.
     * @return a newly inflated View.
     */
    public View inflateHeaderView(@LayoutRes int res) {
        return mPresenter.inflateHeaderView(res);
    }
    public View inflateFooterView(@LayoutRes int res) {
        return mPresenter.inflateFooterView(res);
    }


    /**
     * Adds a View as a header of the navigation menu.
     *
     * @param view The view to be added as a header of the navigation menu.
     */
    public void addHeaderView(@NonNull View view) {
        mPresenter.addHeaderView(view);
    }

    public void addFooterView(@NonNull View view) {
        mPresenter.addFooterView(view);
    }

    /**
     * Removes a previously-added header view.
     *
     * @param view The view to remove
     */
    public void removeHeaderView(@NonNull View view) {
        mPresenter.removeHeaderView(view);
    }
    public void removeFooterView(@NonNull View view) {
        mPresenter.removeFooterView(view);
    }

    /**
     * Returns the tint which is applied to our item's icons.
     *
     * @see #setItemIconTintList(ColorStateList)
     *
     * @attr ref R.styleable#NavigationView_itemIconTint
     */
    @Nullable
    public ColorStateList getItemIconTintList() {
        return mPresenter.getItemTintList();
    }

    /**
     * Set the tint which is applied to our item's icons.
     *
     * @param tint the tint to apply.
     *
     * @attr ref R.styleable#NavigationView_itemIconTint
     */
    public void setItemIconTintList(@Nullable ColorStateList tint) {
        mPresenter.setItemIconTintList(tint);
    }

    /**
     * Returns the tint which is applied to our item's icons.
     *
     * @see #setItemTextColor(ColorStateList)
     *
     * @attr ref R.styleable#NavigationView_itemTextColor
     */
    @Nullable
    public ColorStateList getItemTextColor() {
        return mPresenter.getItemTextColor();
    }

    /**
     * Set the text color which is text to our items.
     *
     * @see #getItemTextColor()
     *
     * @attr ref R.styleable#NavigationView_itemTextColor
     */
    public void setItemTextColor(@Nullable ColorStateList textColor) {
        mPresenter.setItemTextColor(textColor);
    }

    /**
     * Returns the background drawable for the menu items.
     *
     * @see #setItemBackgroundResource(int)
     *
     * @attr ref R.styleable#NavigationView_itemBackground
     */
    public Drawable getItemBackground() {
        return mPresenter.getItemBackground();
    }

    /**
     * Set the background of the menu items to the given resource.
     *
     * @param resId The identifier of the resource.
     *
     * @attr ref R.styleable#NavigationView_itemBackground
     */
    public void setItemBackgroundResource(@DrawableRes int resId) {
        setItemBackground(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * Set the background of the menu items to a given resource. The resource should refer to
     * a Drawable object or 0 to use the background background.
     *
     * @attr ref R.styleable#NavigationView_itemBackground
     */
    public void setItemBackground(Drawable itemBackground) {
        mPresenter.setItemBackground(itemBackground);
    }

    /**
     * Sets the currently checked item in this navigation menu.
     *
     * @param id The item ID of the currently checked item.
     */
    public void setCheckedItem(@IdRes int id) {
        MenuItem item = mMenu.findItem(id);
        if (item != null) {
            mPresenter.setCheckedItem((MenuItemImpl) item);
        }
    }

    /**
     * Set the text appearance of the menu items to a given resource.
     *
     * @attr ref R.styleable#NavigationView_itemTextAppearance
     */
    public void setItemTextAppearance(@StyleRes int resId) {
        mPresenter.setItemTextAppearance(resId);
    }

    private MenuInflater getMenuInflater() {
        if (mMenuInflater == null) {
            mMenuInflater = new SupportMenuInflater(getContext());
        }
        return mMenuInflater;
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = getResources().getColorStateList(value.resourceId);
        if (!getContext().getTheme().resolveAttribute(android.support.design.R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{
                DISABLED_STATE_SET,
                CHECKED_STATE_SET,
                EMPTY_STATE_SET
        }, new int[]{
                baseColor.getColorForState(DISABLED_STATE_SET, defaultColor),
                colorPrimary,
                defaultColor
        });
    }

    /**
     * Listener for handling events on navigation items.
     */
    public interface OnNavigationItemSelectedListener {

        /**
         * Called when an item in the navigation menu is selected.
         *
         * @param item The selected item
         *
         * @return true to display the item as the selected item
         */
        public boolean onNavigationItemSelected(MenuItem item);
    }

    /**
     * User interface state that is stored by NavigationView for implementing
     * onSaveInstanceState().
     */
    public static class SavedState extends BaseSavedState {
        public Bundle menuState;

        public SavedState(Parcel in, ClassLoader loader) {
            super(in);
            menuState = in.readBundle(loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(menuState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel, ClassLoader loader) {
                return new SavedState(parcel, loader);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        });
    }

}
class ThemeUtils2 {

    private static final int[] APPCOMPAT_CHECK_ATTRS = { android.support.design.R.attr.colorPrimary };

    static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        if (a != null) {
            a.recycle();
        }
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }
}

