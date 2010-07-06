/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
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

    public ApplicationsAdapter(Context context, ArrayList<ApplicationInfo> apps) {
        super(context, 0, apps);
        mInflater = LayoutInflater.from(context);
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
        //ADW: Load textcolor and bubble color from theme
        String themePackage=AlmostNexusSettingsHelper.getThemePackageName(getContext(), Launcher.THEME_DEFAULT);
        int color=getContext().getResources().getColor(R.color.bubble_dark_background);
        if(themePackage!=Launcher.THEME_DEFAULT){
        	Resources themeResources=null;
        	try {
    			themeResources=getContext().getPackageManager().getResourcesForApplication(themePackage);
    		} catch (NameNotFoundException e) {
    			e.printStackTrace();
    		}
    		if(themeResources!=null){
    			int textColorId=themeResources.getIdentifier("drawer_text_color", "color", themePackage);
    			if(textColorId!=0){
    				textView.setTextColor(themeResources.getColor(textColorId));
    			}
    		}
        }

        return convertView;
    }
}
