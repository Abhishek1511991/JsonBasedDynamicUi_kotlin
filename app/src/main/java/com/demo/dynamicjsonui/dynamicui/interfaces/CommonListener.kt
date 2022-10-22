package com.demo.dynamicjsonui.dynamicui.interfaces

import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import com.rey.material.widget.Spinner

interface CommonListener: View.OnClickListener, CompoundButton.OnCheckedChangeListener,
    AdapterView.OnItemSelectedListener, View.OnFocusChangeListener, Spinner.OnItemSelectedListener