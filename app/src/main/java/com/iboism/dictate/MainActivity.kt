package com.iboism.dictate

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private val dictationController : DictationController by lazy { DictationController(applicationContext) }
    private val textChanged = (object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            updateFabVisibility(tts_edit_text.text.isNotBlank())
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.alpha = 0f
        fab.setOnClickListener { _ ->
            tts_edit_text.text?.let { dictationController.dictate(it.toString()) }
        }
        tts_edit_text.addTextChangedListener(textChanged)
    }

    private fun updateFabVisibility(show: Boolean) {
        if (show) {
            fab.animateShow()
        } else {
            fab.animateHide()
        }
    }

    private fun FloatingActionButton.animateHide() {
        if (this.alpha == 1f) {
            this.animate()
                    .rotationBy(90f).alpha(0f).scaleX(0f).scaleY(0f)
                    .setDuration(250)
                    .setInterpolator(AnticipateInterpolator())
                    .start()
        }
    }

    private fun FloatingActionButton.animateShow() {
        if (this.alpha == 0f) {
            this.animate()
                    .rotationBy(-90f).alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(250)
                    .setInterpolator(OvershootInterpolator())
                    .start()
        }
    }
}
