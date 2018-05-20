package com.iboism.dictate

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_dictate_dialog.*


class DictateDialogActivity : AppCompatActivity() {
    var controller: DictationController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictate_dialog)

        val intent = this.intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleReceivedText(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller?.destroy()
    }

    private fun handleReceivedText(intent: Intent) {
        val message = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return this.finish()
        val controller = DictationController(applicationContext)
        controller.dictate(message, { this.finish() })

        dialog_layout.setOnClickListener {
            controller.destroy()
            this.finish()
        }
        
        this.controller = controller
    }
}
