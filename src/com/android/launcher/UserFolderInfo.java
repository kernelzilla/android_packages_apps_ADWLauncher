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

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Represents a folder containing shortcuts or apps.
 */
class UserFolderInfo extends FolderInfo {
    /**
     * The apps and shortcuts 
     */
    ArrayList<ApplicationInfo> contents = new ArrayList<ApplicationInfo>();
    
    UserFolderInfo() {
        itemType = LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER;
    }
    
    /**
     * Add an app or shortcut
     * 
     * @param item
     */
    public void add(ApplicationInfo item) {
        contents.add(item);
    }

    @Override
    void onAddToDatabase(ContentValues values) { 
        super.onAddToDatabase(values);
        values.put(LauncherSettings.Favorites.TITLE, title.toString());
    }
}
