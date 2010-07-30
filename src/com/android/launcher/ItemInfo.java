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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Represents an item in the launcher.
 */
class ItemInfo {
    
    static final int NO_ID = -1;
    
    /**
     * The id in the settings database for this item
     */
    long id = NO_ID;
    
    /**
     * One of {@link LauncherSettings.Favorites#ITEM_TYPE_APPLICATION},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_SHORTCUT},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_USER_FOLDER}, or
     * {@link LauncherSettings.Favorites#ITEM_TYPE_APPWIDGET}.
     */
    int itemType;
    
    /**
     * The id of the container that holds this item. For the desktop, this will be 
     * {@link LauncherSettings.Favorites#CONTAINER_DESKTOP}. For the all applications folder it
     * will be {@link #NO_ID} (since it is not stored in the settings DB). For user folders
     * it will be the id of the folder.
     */
    long container = NO_ID;
    
    /**
     * Iindicates the screen in which the shortcut appears.
     */
    int screen = -1;
    
    /**
     * Indicates the X position of the associated cell.
     */
    int cellX = -1;

    /**
     * Indicates the Y position of the associated cell.
     */
    int cellY = -1;

    /**
     * Indicates the X cell span.
     */
    int spanX = 1;

    /**
     * Indicates the Y cell span.
     */
    int spanY = 1;

    /**
     * Indicates whether the item is a gesture.
     */
    boolean isGesture = false;

    ItemInfo() {
    }

    ItemInfo(ItemInfo info) {
        id = info.id;
        cellX = info.cellX;
        cellY = info.cellY;
        spanX = info.spanX;
        spanY = info.spanY;
        screen = info.screen;
        itemType = info.itemType;
        container = info.container;
    }

    /**
     * Write the fields of this item to the DB
     * 
     * @param values
     */
    void onAddToDatabase(ContentValues values) { 
        values.put(LauncherSettings.BaseLauncherColumns.ITEM_TYPE, itemType);
        if (!isGesture) {
            values.put(LauncherSettings.Favorites.CONTAINER, container);
            values.put(LauncherSettings.Favorites.SCREEN, screen);
            values.put(LauncherSettings.Favorites.CELLX, cellX);
            values.put(LauncherSettings.Favorites.CELLY, cellY);
            values.put(LauncherSettings.Favorites.SPANX, spanX);
            values.put(LauncherSettings.Favorites.SPANY, spanY);
        }
    }

    static void writeBitmap(ContentValues values, Bitmap bitmap) {
        if (bitmap != null) {
            // Try go guesstimate how much space the icon will take when serialized
            // to avoid unnecessary allocations/copies during the write.
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            ByteArrayOutputStream out = new ByteArrayOutputStream(size);
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                values.put(LauncherSettings.Favorites.ICON, out.toByteArray());
            } catch (IOException e) {
                Log.w("Favorite", "Could not write icon");
            }
        }
    }

}
