package com.evans.technologies.conductor.data.network.Interactor



class ResourceBound<T> private constructor(
    val status: Status,
    val data: T?,
    val message: String?
) {

    companion object {
        fun <T> success(data: T): ResourceBound<T> {
            return ResourceBound(Status.SUCCESS, data, null)
        }

        fun <T> error(
            msg: String?,
            data: T?
        ): ResourceBound<T> {
            return ResourceBound(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ResourceBound<T> {
            return ResourceBound(Status.LOADING, data, null)
        }
    }

}