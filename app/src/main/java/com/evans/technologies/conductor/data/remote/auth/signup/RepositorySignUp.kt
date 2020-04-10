package com.evans.technologies.conductor.data.remote.auth.signup

import com.evans.technologies.conductor.model.LoginResponse

interface RepositorySignUp {

    suspend fun SignUpAuth(model: LoginResponse)
}