package com.example.weather

open class BasePresenter<T : BaseView> {

    var view: T? = null

    fun attachView(view: T) {
        this.view = view

        onViewAttached()
    }

    open fun onViewAttached() {}

    fun destroy() {
        view = null
    }
}