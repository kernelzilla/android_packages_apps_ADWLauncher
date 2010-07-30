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

/**
 * Interface defining an object that can receive a drag.
 *
 */
public interface DropTarget {

    /**
     * Handle an object being dropped on the DropTarget
     * 
     * @param source DragSource where the drag started
     * @param x X coordinate of the drop location
     * @param y Y coordinate of the drop location
     * @param xOffset Horizontal offset with the object being dragged where the original touch happened
     * @param yOffset Vertical offset with the object being dragged where the original touch happened
     * @param dragInfo Data associated with the object being dragged
     * 
     */
    void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo);
    
    void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo);

    void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo);

    void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo);

    /**
     * Check if a drop action can occur at, or near, the requested location.
     * This may be called repeatedly during a drag, so any calls should return
     * quickly.
     * 
     * @param source DragSource where the drag started
     * @param x X coordinate of the drop location
     * @param y Y coordinate of the drop location
     * @param xOffset Horizontal offset with the object being dragged where the
     *            original touch happened
     * @param yOffset Vertical offset with the object being dragged where the
     *            original touch happened
     * @param dragInfo Data associated with the object being dragged
     * @return True if the drop will be accepted, false otherwise.
     */
    boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo);
}
