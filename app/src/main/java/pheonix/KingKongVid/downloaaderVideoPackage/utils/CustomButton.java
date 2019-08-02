/*
 *     LM videodownloader is a browser app for android, made to easily
 *     download videos.
 *     Copyright (C) 2018 Loremar Marabillas
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package pheonix.KingKongVid.downloaaderVideoPackage.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import pheonix.KingKongVid.downloaaderVideoPackage.R;

public class CustomButton extends android.support.v7.widget.AppCompatTextView {
    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackground(getResources().getDrawable(R.drawable.rounded_button));
        setTextColor(Color.BLACK);
    }
}
