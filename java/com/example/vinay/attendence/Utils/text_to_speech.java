package com.example.vinay.attendence.Utils;


import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class text_to_speech {
    private static TextToSpeech tts;
    public static void speech(Context context, final String message)
    {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    int result = tts.setLanguage(new Locale("en", "IN"));
                    tts.setPitch((float)1.0); //ranges from 0.0 to 2.0
                    tts.setSpeechRate((float)1.0);  //ranges from 0.0 to 2.0
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TTS", "Language is not supported");
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
                        } else {
                            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
                else {
                    Log.e("Error","Status not OK");
                }
            }
        });
    }
}
