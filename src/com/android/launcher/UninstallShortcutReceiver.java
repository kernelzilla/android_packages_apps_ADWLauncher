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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.net.URISyntaxException;

public class UninstallShortcutReceiver extends BroadcastReceiver {
    private static final String ACTION_UNINSTALL_SHORTCUT =
            "com.android.launcher.action.UNINSTALL_SHORTCUT";

    public void onReceive(Context context, Intent data) {
        if (!ACTION_UNINSTALL_SHORTCUT.equals(data.getAction())) {
            return;
        }

        Intent intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        boolean duplicate = data.getBooleanExtra(Launcher.EXTRA_SHORTCUT_DUPLICATE, true);

        if (intent != null && name != null) {
            final ContentResolver cr = context.getContentResolver();
            Cursor c = cr.query(LauncherSettings.Favorites.CONTENT_URI,
                new String[] { LauncherSettings.Favorites._ID, LauncherSettings.Favorites.INTENT },
                LauncherSettings.Favorites.TITLE + "=?", new String[] { name }, null);

            final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
            final int idIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);

            boolean changed = false;

            try {
                while (c.moveToNext()) {
                    try {
                        if (intent.filterEquals(Intent.parseUri(c.getString(intentIndex), 0))) {
                            final long id = c.getLong(idIndex);
                            final Uri uri = LauncherSettings.Favorites.getContentUri(id, false);
                            cr.delete(uri, null, null);
                            changed = true;
                            if (!duplicate) {
                                break;
                            }
                        }
                    } catch (URISyntaxException e) {
                        // Ignore
                    }
                }
            } finally {
                c.close();
            }

            if (changed) {
                cr.notifyChange(LauncherSettings.Favorites.CONTENT_URI, null);
                Toast.makeText(context, context.getString(R.string.shortcut_uninstalled, name),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
