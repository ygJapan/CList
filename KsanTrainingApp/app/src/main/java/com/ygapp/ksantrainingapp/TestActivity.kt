package com.ygapp.ksantrainingapp

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.concurrent.schedule

class TestActivity : AppCompatActivity(), View.OnClickListener {

    //問題数の定義
    var numberOfQuestion :Int = 0
    //残り問題数の定義
    var numberOfRemaining: Int = 0
    //正解数の定義
    var numberOfCorrect: Int = 0
    //SoundPoolの定義
    lateinit var soundPool: SoundPool
    //SoundIdの定義
    var intSoundId_Correct: Int = 0
    var intSoundId_InCorrect: Int = 0

    //Timerの定義
   lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        val bundle = intent.extras
        numberOfQuestion  = bundle!!.getInt("numberOfQuestion")
        textViewRemaining.text = numberOfQuestion.toString()
        numberOfRemaining = numberOfQuestion
        numberOfCorrect = 0





//        Todo 「こたえ合わせ」ボタンが押されたら
        buttonAnswerCheck.setOnClickListener {
            if (textViewAnswer.text.toString() != "" && textViewAnswer.text.toString() != "-") {
                answerCheck()
            }
        }
        buttonBack.setOnClickListener {
            finish()
        }



        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonClear.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)

        question()
    }

    override fun onResume() {
        super.onResume()
        //Soundpoolの準備
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SoundPool.Builder().setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build())
                .setMaxStreams(1)
                .build()
        }else{
            SoundPool(1,AudioManager.STREAM_MUSIC,0)
        }

        //効果音ふぁいる
        intSoundId_Correct = soundPool.load(this, R.raw.sound_correct,1)
        intSoundId_InCorrect = soundPool.load(this,R.raw.sound_incorrect,1)

        //Timerの準備
        timer = Timer()
    }

    override fun onPause() {
        super.onPause()
        //効果音を後片付け
        soundPool.release()

        //タイマーのキャンセル
        timer.cancel()
    }

    //答え合わせ処理メソッド
    private fun answerCheck() {

        buttonBack.isEnabled = false
        buttonAnswerCheck.isEnabled = false
        button0.isEnabled = false
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
        buttonMinus.isEnabled = false
        buttonClear.isEnabled = false


        numberOfRemaining -= 1
        textViewRemaining.text = numberOfRemaining.toString()

        imageViewAnswer.visibility = View.VISIBLE

        //自分の答えのインスタンスintMyAnswer
        val intMyAnswer : Int = textViewAnswer.text.toString().toInt()

        //本当の答えのインスタンスintRealAnswer
        val intRealAnswer :Int
                = if (textViewOperator.text == "+"){            //式が足し算(+)なら
            textViewLeft.text.toString().toInt() + textViewRight.text.toString().toInt()
        }else {
            textViewLeft.text.toString().toInt() - textViewRight.text.toString().toInt()
        }

        //比較
        if(intMyAnswer == intRealAnswer){

            numberOfCorrect += 1                                        //正解数１増やす
            textViewCorrect.text = numberOfCorrect.toString()           //画面に正解数を表示(更新,１つ増やす)
            imageViewAnswer.setImageResource(R.drawable.pic_correct)    //○画像の表示
            soundPool.play(intSoundId_Correct, 1.0f,1.0f,0,0,1.0f) //ピンポン音

        }else {

            imageViewAnswer.setImageResource(R.drawable.pic_incorrect)  //×画像の表示
            soundPool.play(intSoundId_InCorrect, 1.0f,1.0f,0,0,1.0f) //ブッブー音
        }


        val intPoint: Int = (( numberOfCorrect.toDouble() / (numberOfQuestion - numberOfRemaining).toDouble()) * 100).toInt()
        textViewPoint.text = intPoint.toString()

        if (numberOfRemaining == 0){

//        　　　　　⇒ もどるボタン〇、こたえあわせボタン×、「テスト終了」表示
            buttonBack.isEnabled = true
            buttonAnswerCheck.isEnabled = false
            textViewMessage.text = "テスト終了"

        }else {

            timer.schedule(1000,{ runOnUiThread { question() }})
        }

    }

    //問題を出す処理をするメソッド
    private fun question() {

        buttonBack.isEnabled = false


        buttonAnswerCheck.isEnabled = true
        button0.isEnabled = true
        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        button9.isEnabled = true
        buttonClear.isEnabled = true
        buttonMinus.isEnabled = true


        val random = Random()
        val intQuestionLeft = random.nextInt(100) + 1
        val intQuestionRight = random.nextInt(100) + 1
        textViewLeft.text = intQuestionLeft.toString()
        textViewRight.text = intQuestionRight.toString()


        when (random.nextInt(2) + 1) {
            1 -> textViewOperator.text = "+"
            2 -> textViewOperator.text = "-"
        }


        textViewAnswer.text = ""


        imageViewAnswer.visibility = View.INVISIBLE

    }

    override fun onClick(p0: View?) {
        val button: Button = p0 as Button

        when(p0?.id){
            //Clearボタンを押したら答えのところを"(空白)"にする
            R.id.buttonClear -> textViewAnswer.text = ""

            //Answerが"(空白)"ならMinusボタンを押したら"-"を入れる
            R.id.buttonMinus
            -> if (textViewAnswer.text.toString() == "")
                textViewAnswer.text = "-"

            //Answerが "0"ではない または "-"ではない 時に0を入れる
            R.id.button0
            -> if (textViewAnswer.text.toString() !="0" && textViewAnswer.text.toString() != "-")
                textViewAnswer.append(button.text)

            //0,-,C以外なら、(→1~9の数字)
            //0が入ってたら上書き 0以外なら押した数字を後ろにつける
            else -> if (textViewAnswer.text.toString() == "0")
                    textViewAnswer.text = button.text
                    else textViewAnswer.append(button.text)

        }
    }
}