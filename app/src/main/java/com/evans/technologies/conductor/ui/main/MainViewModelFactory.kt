package com.evans.technologies.conductor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evans.technologies.conductor.data.remote.main.RepositoryMain

class MainViewModelFactory(private val repo:RepositoryMain):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RepositoryMain::class.java).newInstance(repo)
    }
}