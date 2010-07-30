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
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * GridView adapter to show the list of applications and shortcuts
 */
public class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> {
    private final LayoutInflater mInflater;
    private Drawable mBackground;
    private int mTextColor=0;
    private boolean useThemeTextColor=false;
    public ApplicationsAdapter(Context context, ArrayList<ApplicationInfo> apps) {
        super(context, 0, apps);
        mInflater = LayoutInflater.from(context);
        //ADW: Load textcolor and bubble color from theme
        String themePackage=AlmostNexusSettingsHelper.getThemePackageName(getContext(), Launcher.THEME_DEFAULT);
        if(!themePackage.equals(Launcher.THEME_DEFAULT)){
        	Resources themeResources=null;
        	try {
    			themeResources=getContext().getPackageManager().getResourcesForApplication(themePackage);
    		} catch (NameNotFoundException e) {
    			//e.printStackTrace();
    		}
    		if(themeResources!=null){
    			int textColorId=themeResources.getIdentifier("drawer_text_color", "color", themePackage);
    			if(textColorId!=0){
    				mTextColor=themeResources.getColor(textColorId);
    				useThemeTextColor=true;
    			}
    			mBackground=IconHighlights.getDrawable(getContext(), IconHighlights.TYPE_DRAWER);
    		}
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ApplicationInfo info = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.application_boxed, parent, false);
        }

        if (!info.filtered) {
            info.icon = Utilities.createIconThumbnail(info.icon, getContext());
            info.filtered = true;
        }

        final TextView textView = (TextView) convertView;
        textView.setCompoundDrawablesWithIntrinsicBounds(null, info.icon, null, null);
        textView.setText(info.title);
		if(useThemeTextColor){
			textView.setTextColor(mTextColor);
		}
        //TODO:ADW Loading the background drawable for the app drawer hogs the ram and cpu
        //so i'd better not use it, sorry themers
		if(mBackground!=null)
			convertView.setBackgroundDrawable(mBackground);
        return convertView;
    }
}
