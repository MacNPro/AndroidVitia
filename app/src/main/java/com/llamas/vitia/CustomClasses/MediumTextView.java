package com.llamas.vitia.CustomClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MacNPro on 9/5/16.
 */
public class MediumTextView extends TextView {

    public MediumTextView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Avenir-Medium.ttf"));
    }

}
