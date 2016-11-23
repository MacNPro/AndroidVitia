package com.llamas.vitia.CustomClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by MacNPro on 9/5/16.
 */
public class MediumRadioButton extends RadioButton {

    public MediumRadioButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Avenir-Medium.ttf"));
    }

}
