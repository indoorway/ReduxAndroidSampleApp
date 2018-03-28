package com.indoorway.reduxandroidsampleapp.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.indoorway.reduxandroidsampleapp.R
import com.indoorway.reduxandroidsampleapp.common.AppEvent
import com.indoorway.reduxandroidsampleapp.common.di.GlobalModelModuleImpl
import com.indoorway.reduxandroidsampleapp.common.utils.bind
import com.indoorway.reduxandroidsampleapp.common.utils.mapNotNull
import com.indoorway.reduxandroidsampleapp.model.GithubResponse
import com.jakewharton.rxrelay2.PublishRelay
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxAppCompatActivity() {

    private val additionalEvents = PublishRelay.create<AppEvent>()

    private var errorDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initModel()
    }

    private fun initModel() {
        bind(uiEvents(), { GlobalModelModuleImpl.globalModel.mainState }, this::handleStates)
    }

    private fun uiEvents(): Observable<AppEvent> =
            Observable.merge(listOf(
                    additionalEvents,
                    Observable.just(MainEvent.OnCreate)
            ))

    private fun handleStates(states: Observable<MainState>) {
        states.publish().apply {
            showGithubData()
            showNetworkError()
            hideNetworkError()
        }.connect()
    }

    private fun Observable<MainState>.showGithubData() {
        mapNotNull { it.githubData }
                .distinctUntilChanged()
                .subscribe { updateGithubData(it) }
    }

    private fun Observable<MainState>.showNetworkError() {
        mapNotNull { it.showError }
                .distinctUntilChanged()
                .filter { it }
                .subscribe { showUnexpectedError() }
    }

    private fun Observable<MainState>.hideNetworkError() {
        mapNotNull { it.showError }
                .distinctUntilChanged()
                .filter { !it }
                .subscribe { hideUnexpectedError() }
    }

    private fun showUnexpectedError() {
        val builder = AlertDialog.Builder(this)

        errorDialog = builder.setTitle(getString(R.string.network_error_dialog_title))
                .setMessage(getString(R.string.network_error_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    private fun hideUnexpectedError() {
        errorDialog?.dismiss()
    }

    private fun updateGithubData(githubResponse: GithubResponse) {
        githubId.text = githubResponse.id.toString()
        githubName.text = githubResponse.name
        githubDescription.text = githubResponse.description
    }
}
