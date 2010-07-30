/*
*    Copyright 2010 AnderWeb (Gustavo Claramunt) <anderweb@gmail.com>
*
*    This file is part of ADW.Launcher.
*
*    ADW.Launcher is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    ADW.Launcher is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with ADW.Launcher.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.android.launcher;

import android.widget.ImageView;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.KeyEvent;

public class HandleView extends ImageView {
    private static final int ORIENTATION_HORIZONTAL = 1;

    private Launcher mLauncher;
    private int mOrientation = ORIENTATION_HORIZONTAL;

    public HandleView(Context context) {
        super(context);
    }

    public HandleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HandleView, defStyle, 0);
        mOrientation = a.getInt(R.styleable.HandleView_direction, ORIENTATION_HORIZONTAL);
        a.recycle();
    }

    @Override
    public View focusSearch(int direction) {
        View newFocus = super.focusSearch(direction);
        if (newFocus == null && !mLauncher.isAllAppsVisible()) {
            final Workspace workspace = mLauncher.getWorkspace();
            workspace.dispatchUnhandledMove(null, direction);
            return (mOrientation == ORIENTATION_HORIZONTAL && direction == FOCUS_DOWN) ?
                    this : workspace;
        }
        return newFocus;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final boolean handled = super.onKeyDown(keyCode, event);

        if (!handled && mLauncher.isAllAppsVisible() && !isDirectionKey(keyCode)) {
            return mLauncher.getApplicationsGrid().onKeyDown(keyCode, event);
        }

        return handled;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        final boolean handled = super.onKeyUp(keyCode, event);

        if (!handled && mLauncher.isAllAppsVisible() && !isDirectionKey(keyCode)) {
            return mLauncher.getApplicationsGrid().onKeyUp(keyCode, event);
        }

        return handled;
    }

    private static boolean isDirectionKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_UP;
    }

    void setLauncher(Launcher launcher) {
        mLauncher = launcher;
    }
}
