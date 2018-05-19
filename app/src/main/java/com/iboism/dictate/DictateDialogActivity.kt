package com.iboism.dictate

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent



class DictateDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictate_dialog)

        val intent = this.intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleRecievedText(intent)
            }
        }
    }

    private fun handleRecievedText(intent: Intent) {
        val message = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
        DictationController(applicationContext).dictate(message, { this.finish() })
    }
}
