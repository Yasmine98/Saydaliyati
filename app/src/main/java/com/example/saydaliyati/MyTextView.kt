package com.example.saydaliyati

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by kamran on 27/6/16.
 *
 */
class MyTextView : TextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
         //   val tf = Typeface.createFromAsset(context.assets, "fonts/OpenSans-Semibold.ttf")
           // typeface = tf
        }
    }

}