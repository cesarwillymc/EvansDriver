package com.evans.technologies.conductor.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return   inflater.inflate(getLayout(), container, false)
    }
    @LayoutRes
    abstract fun getLayout():Int

    override fun getView(): View? {
        return super.getView()
    }
}