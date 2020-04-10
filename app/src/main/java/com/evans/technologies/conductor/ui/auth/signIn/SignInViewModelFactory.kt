package com.summit.summitfood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evans.technologies.conductor.data.remote.auth.signin.RepositoryAuth

class SignInViewModelFactory(private val repo: RepositoryAuth): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RepositoryAuth::class.java).newInstance(repo)
    }
}