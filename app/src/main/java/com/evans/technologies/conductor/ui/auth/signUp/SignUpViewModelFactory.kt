package com.evans.technologies.conductor.ui.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evans.technologies.conductor.data.remote.auth.signup.RepositorySignUp

class SignUpViewModelFactory(val repo:RepositorySignUp):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RepositorySignUp::class.java).newInstance(repo)
    }
}