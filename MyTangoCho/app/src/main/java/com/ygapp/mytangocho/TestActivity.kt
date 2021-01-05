package com.ygapp.mytangocho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity(), View.OnClickListener {

    var boolStatusMemory: Boolean = false
    var boolMemoryrized: Boolean = false

    var intState: Int = 0
    var BEFORE_START: Int = 1
    var RUNNING_QUESTION: Int = 2
    var RUNNING_ANSWER: Int = 3
    var TEST_FINISHED: Int = 4

    lateinit var realm: Realm
    lateinit var results: RealmResults<WordDB>
    lateinit var word_list: ArrayList<WordDB>

    var intLength: Int = 0
    var intCount: Int = 0 //何問目カウンター

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val bundle = intent.extras
        boolStatusMemory = bundle!!.getBoolean(getString(R.string.intent_key_memory_flag))
        constraintLayoutTest.setBackgroundResource(intBackgroundColor)

        //テストスタート前の状態では問題と答えの画像非表示にしておく
        intState = BEFORE_START
        imageViewFlashQuestion.visibility = View.INVISIBLE
        imageViewFlashAnswer.visibility = View.INVISIBLE

        //ボタンを「テスト始める」「終わる」の画像にしておく
        buttonNext.setBackgroundResource(R.drawable.image_button_test_start)
        buttonEndTest.setBackgroundResource(R.drawable.image_button_end_test)

        buttonNext.setOnClickListener(this)
        buttonEndTest.setOnClickListener(this)
        checkBox.setOnClickListener {
                boolMemoryrized = checkBox.isChecked
        }

    }

    override fun onResume() {
        super.onResume()
        //Realmインスタンスの取得
        realm = Realm.getDefaultInstance()

        if (boolStatusMemory){
            results = realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_memory_flag),false).findAll()
        }else{
            results = realm.where(WordDB::class.java).findAll()
        }

        intLength = results.size
        textViewRemaining.text = intLength.toString()

        word_list = ArrayList(results)
        Collections.shuffle(word_list)
    }


    override fun onPause() {
        super.onPause()
        //Realmの後片付け
        realm.close()
    }

    override fun onClick(p0: View) {

        when(p0.id){
            //上のボタンを押したとき...
            R.id.buttonNext ->
                when(intState){
                    BEFORE_START -> {           //スタート前は、問題を出す
                        intState = RUNNING_QUESTION
                        showQuestion()
                    }

                    RUNNING_QUESTION -> {       //問題を出してる時は、答えを出す
                        intState = RUNNING_ANSWER
                        showAnswer()
                    }

                    RUNNING_ANSWER -> {         //答えを出してたら、(次の)問題を出す
                        intState = RUNNING_QUESTION
                        showQuestion()
                    }


                }

            R.id.buttonEndTest -> {
                val dialog = AlertDialog.Builder(this@TestActivity).apply {
                    setTitle("テストの終了")
                    setMessage("テストを終了してもいいですか？")
                    setPositiveButton("はい"){ dialog, which ->
                        if (intState == TEST_FINISHED){
                            val selectedDB = realm.where(WordDB::class.java). equalTo(getString(R.string.db_field_question),
                                    word_list[intCount - 1].strQuestion).findFirst()!!
                            realm.beginTransaction()
                            selectedDB.boolMemoryFlag = boolMemoryrized
                            realm.commitTransaction()
                        }
                        finish()
                    }
                    setNegativeButton("いいえ"){ dialog, which -> }
                    show()
                }

            }


        }

    }

    private fun showQuestion() {



        if (intCount > 0){
            val selectedDB = realm.where(WordDB::class.java). equalTo(getString(R.string.db_field_question)
                    , word_list[intCount - 1].strQuestion).findFirst()!!
            realm.beginTransaction()
            selectedDB.boolMemoryFlag = boolMemoryrized
            realm.commitTransaction()
        }

        intCount ++
        textViewRemaining.text = (intLength - intCount).toString()



        imageViewFlashAnswer.visibility = View.INVISIBLE    //答えの画像を消す
        textViewFlashAnswer.text = ""                       //答えのテキストを消す
        imageViewFlashQuestion.visibility = View.VISIBLE    //問題の画像は表示
        textViewFlashQuestion.text = word_list[intCount - 1].strQuestion

        buttonNext.setBackgroundResource(R.drawable.image_button_go_answer)

        checkBox.isChecked = word_list[intCount - 1].boolMemoryFlag
        boolMemoryrized = checkBox.isChecked

    }

    private fun showAnswer() {

        imageViewFlashAnswer.visibility = View.VISIBLE
        textViewFlashAnswer.text = word_list[intCount - 1].strAnswer

        buttonNext.setBackgroundResource(R.drawable.image_button_go_next_question)

        if (intLength == intCount){
            intState = TEST_FINISHED
            textViewMessage.text = "テスト終了"

            buttonNext.isEnabled = false
            buttonNext.visibility = View.INVISIBLE

            buttonEndTest.setBackgroundResource(R.drawable.image_button_back)

        }



    }





}