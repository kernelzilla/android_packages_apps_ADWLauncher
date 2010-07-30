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

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.net.Uri;
import android.provider.LiveFolders;
import android.os.AsyncTask;
import android.database.Cursor;
import android.graphics.Rect;

import java.lang.ref.WeakReference;

public class LiveFolder extends Folder {
    private AsyncTask<LiveFolderInfo,Void,Cursor> mLoadingTask;

    public LiveFolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    static LiveFolder fromXml(Context context, FolderInfo folderInfo) {
        final int layout = isDisplayModeList(folderInfo) ?
                R.layout.live_folder_list : R.layout.live_folder_grid;
        return (LiveFolder) LayoutInflater.from(context).inflate(layout, null);
    }

    private static boolean isDisplayModeList(FolderInfo folderInfo) {
        return ((LiveFolderInfo) folderInfo).displayMode ==
                LiveFolders.DISPLAY_MODE_LIST;
    }

    @Override
    public void onItemClick(AdapterView parent, View v, int position, long id) {
        LiveFolderAdapter.ViewHolder holder = (LiveFolderAdapter.ViewHolder) v.getTag();

        if (holder.useBaseIntent) {
            final Intent baseIntent = ((LiveFolderInfo) mInfo).baseIntent;
            if (baseIntent != null) {
                final Intent intent = new Intent(baseIntent);
                Uri uri = baseIntent.getData();
                uri = uri.buildUpon().appendPath(Long.toString(holder.id)).build();
                intent.setData(uri);
        		// set bound
        		if (v != null) {
        		    Rect targetRect = new Rect();
        		    v.getGlobalVisibleRect(targetRect);
        		    try{
        		    	intent.setSourceBounds(targetRect);
        		    }catch(NoSuchMethodError e){};
        		}        
                mLauncher.startActivitySafely(intent);
            }
        } else if (holder.intent != null) {
    		if (v != null) {
    		    Rect targetRect = new Rect();
    		    v.getGlobalVisibleRect(targetRect);
    		    try{
    		    	holder.intent.setSourceBounds(targetRect);
    		    }catch(NoSuchMethodError e){};
    		}        
            mLauncher.startActivitySafely(holder.intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    void bind(FolderInfo info) {
        super.bind(info);
        if (mLoadingTask != null && mLoadingTask.getStatus() == AsyncTask.Status.RUNNING) {
            mLoadingTask.cancel(true);
        }
        mLoadingTask = new FolderLoadingTask(this).execute((LiveFolderInfo) info);
    }

    @Override
    void onOpen() {
        super.onOpen();
        requestFocus();
    }

    @Override
    void onClose() {
        super.onClose();
        if (mLoadingTask != null && mLoadingTask.getStatus() == AsyncTask.Status.RUNNING) {
            mLoadingTask.cancel(true);
        }

        // The adapter can be null if onClose() is called before FolderLoadingTask
        // is done querying the provider
        final LiveFolderAdapter adapter = (LiveFolderAdapter) mContent.getAdapter();
        if (adapter != null) {
            adapter.cleanup();
        }
    }

    static class FolderLoadingTask extends AsyncTask<LiveFolderInfo, Void, Cursor> {
        private final WeakReference<LiveFolder> mFolder;
        private LiveFolderInfo mInfo;

        FolderLoadingTask(LiveFolder folder) {
            mFolder = new WeakReference<LiveFolder>(folder);
        }

        protected Cursor doInBackground(LiveFolderInfo... params) {
            final LiveFolder folder = mFolder.get();
            if (folder != null) {
                mInfo = params[0];
                return LiveFolderAdapter.query(folder.mLauncher, mInfo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (!isCancelled()) {
                if (cursor != null) {
                    final LiveFolder folder = mFolder.get();
                    if (folder != null) {
                        final Launcher launcher = folder.mLauncher;
                        folder.setContentAdapter(new LiveFolderAdapter(launcher, mInfo, cursor));
                    }
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
    }
}
