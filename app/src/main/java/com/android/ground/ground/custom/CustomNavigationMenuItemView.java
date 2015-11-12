package com.android.ground.ground.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.internal.NavigationMenuItemView;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.util.AttributeSet;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class CustomNavigationMenuItemView extends NavigationMenuItemView {
    private ColorStateList mIconTintList;
    private MenuItemImpl mItemData;


    public CustomNavigationMenuItemView(Context context) {
        super(context);
    }

    public CustomNavigationMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNavigationMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(MenuItemImpl itemData, int menuType) {
        super.initialize(itemData, menuType);
        mItemData = itemData;

    }

    public void setIconTintList(ColorStateList tintList) {
        mIconTintList = tintList;
        if (mItemData != null) {
            // Update the icon so that the tint takes effect
            setIcon(mItemData.getIcon());
        }
    }
}
