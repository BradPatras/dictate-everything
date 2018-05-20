package com.iboism.dictate

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener

/**
 * Created by bradpatras on 5/19/18.
 */

class DictationController(context: Context) : TextToSpeech.OnInitListener, UtteranceProgressListener()  {
    private val tts = TextToSpeech(context, this)
    private var ttsInitialized: Boolean = false
    private var completionQueue: MutableMap<String, (() -> Unit)> = mutableMapOf()
    private var messageQueue: Array<String> = emptyArray()

    fun dictate(message: String, onFinished: (() -> Unit)? = null) {
        onFinished?.let { completionQueue[message.hashCode().toString()] = it }

        if (ttsInitialized) {
            tts.speak(message, TextToSpeech.QUEUE_ADD, Bundle.EMPTY, message.hashCode().toString())
        } else {
            messageQueue += arrayOf(message)
        }
    }

    fun destroy() {
        tts.stop()
        tts.shutdown()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceProgressListener(this)
            messageQueue.forEach {
                tts.speak(it, TextToSpeech.QUEUE_ADD, Bundle.EMPTY, it.hashCode().toString())
            }

            messageQueue = emptyArray()
            ttsInitialized = true
        }
    }

    override fun onError(utteranceId: String?) {
        utteranceId?.let { completionQueue[it]?.invoke() }
    }

    override fun onStart(utteranceId: String?) {}

    override fun onDone(utteranceId: String?) {
        utteranceId?.let { completionQueue[it]?.invoke() }
    }
}