package com.ygapp.straightmachine

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var soundPool : SoundPool

    var soundID: Int =0
    var soundID2: Int=0
    var soundID3: Int=0
    var soundID4: Int=0
    var soundID5: Int=0
    var soundID6: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { soundPool.play(soundID, 1.0f,1.0f,0,0,1.0f) }
        button2.setOnClickListener { soundPool.play(soundID2, 1.0f,1.0f,0,0,1.0f) }
        button3.setOnClickListener { soundPool.play(soundID3, 1.0f,1.0f,0,0,1.0f) }
        button4.setOnClickListener { soundPool.play(soundID4, 1.0f,1.0f,0,0,1.0f) }
        button5.setOnClickListener { soundPool.play(soundID5, 1.0f,1.0f,0,0,1.0f) }
        button6.setOnClickListener { soundPool.play(soundID6, 1.0f,1.0f,0,0,1.0f) }

    }

    override fun onResume() {
        super.onResume()

        //効果音をだすためのクラス、Soundpoolの準備
        //Ver.がLOLIPOP以上ならこうするけど、未満ならこう！みたいな文
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
                .setMaxStreams(1)
                .build()
        } else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
        soundID = soundPool.load(this,R.raw.omedetougozaimasu,1)
        soundID2 = soundPool.load(this,R.raw.goukakudesu,1)
        soundID3 = soundPool.load(this,R.raw.yokudekimashita,1)
        soundID4 = soundPool.load(this,R.raw.zannendeshita,1)
        soundID5 = soundPool.load(this,R.raw.fugoukakudesu,1)
        soundID6 = soundPool.load(this,R.raw.ganbarimasyou,1)

    }

    override fun onPause() {
        super.onPause()

        //使い終わった音ファイルをメモリから後片付け
        soundPool.release()
    }
}