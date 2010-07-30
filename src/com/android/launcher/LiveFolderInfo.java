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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

class LiveFolderInfo extends FolderInfo {

    /**
     * The base intent, if it exists.
     */
    Intent baseIntent;

    /**
     * The live folder's content uri.
     */
    Uri uri;

    /**
     * The live folder's display type.
     */
    int displayMode;

    /**
     * The live folder icon.
     */
    Drawable icon;

    /**
     * When set to true, indicates that the icon has been resized.
     */
    boolean filtered;

    /**
     * Reference to the live folder icon as an application's resource.
     */
    Intent.ShortcutIconResource iconResource;

    LiveFolderInfo() {
        itemType = LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER;
    }

    @Override
    void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put(LauncherSettings.Favorites.TITLE, title.toString());
        values.put(LauncherSettings.Favorites.URI, uri.toString());
        if (baseIntent != null) {
            values.put(LauncherSettings.Favorites.INTENT, baseIntent.toUri(0));
        }
        values.put(LauncherSettings.Favorites.ICON_TYPE, LauncherSettings.Favorites.ICON_TYPE_RESOURCE);
        values.put(LauncherSettings.Favorites.DISPLAY_MODE, displayMode);
        if (iconResource != null) {
            values.put(LauncherSettings.Favorites.ICON_PACKAGE, iconResource.packageName);
            values.put(LauncherSettings.Favorites.ICON_RESOURCE, iconResource.resourceName);
        }
    }
}
