package com.ygapp.mytangocho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    lateinit var realm: Realm
    var strQuestion: String = ""
    var strAnswer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        constraint_layoutedit.setBackgroundResource(intBackgroundColor)

        val bundle = intent.extras
        val strStatus = bundle?.getString(getString(R.string.intent_key_status))
        textViewStatus.text = strStatus

        if(strStatus == getString(R.string.status_change)){
            strQuestion = bundle.getString(getString(R.string.intent_key_question))!!    //問題
            strAnswer = bundle.getString(getString(R.string.intent_key_answer))!!       //答え
            editTextQuestion.setText(strQuestion)
            editTextAnswer.setText(strAnswer)

            editTextQuestion.isEnabled = false
        }else{
            editTextQuestion.isEnabled = true
        }

        buttonTouroku.setOnClickListener {
            if (strStatus == getString(R.string.status_add)) {
                addNewWord()
            } else{
                changeWord()
        }

        }

        buttonback2.setOnClickListener {
        finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //Realmインスタンスの取得
        realm = Realm.getDefaultInstance()
    }

    override fun onPause() {
        super.onPause()
        //Realmインスタンスの後処理
        realm.close()
    }


    private fun addNewWord() {

        val dialog = AlertDialog.Builder(this@EditActivity).apply {
            setTitle("登録")
            setMessage("登録してもいいですか？")
            setPositiveButton("はい") { dialog, which ->

                try {
                    realm.beginTransaction()    //開始処理
                    val wordDB = realm.createObject(WordDB::class.java, editTextQuestion.text.toString())
                    wordDB.strAnswer = editTextAnswer.text.toString()
                    wordDB.boolMemoryFlag = false

                    Toast.makeText(this@EditActivity, R.string.Touroku_Toast, Toast.LENGTH_SHORT).show()

                }catch (e:RealmPrimaryKeyConstraintException){
                    Toast.makeText(this@EditActivity, R.string.Chohuku_Toast, Toast.LENGTH_SHORT).show()
                }finally {
                    editTextQuestion.setText("")
                    editTextAnswer.setText("")

                    realm.commitTransaction()   //終了処理
                }

                            }
            setNegativeButton("いいえ"){ dialog, which -> }
            show()
        }


    }

    private fun changeWord() {
        val selectedDB:WordDB = realm.where(WordDB::class.java).equalTo(WordDB::strQuestion.name, strQuestion).findFirst()!!

        val dialog = AlertDialog.Builder(this@EditActivity).apply{
            setTitle(selectedDB.strAnswer + "の変更")
            setMessage("変更してもいいですか？")
            setPositiveButton("はい"){ dialog, which ->
                realm.beginTransaction()
                selectedDB.strAnswer = editTextAnswer.text.toString()
                selectedDB.boolMemoryFlag = false
                realm.commitTransaction()

                editTextQuestion.setText("")
                editTextAnswer.setText("")

                Toast.makeText(this@EditActivity, R.string.Shusei_Toast,Toast.LENGTH_SHORT).show()

                finish()
            }
            setNegativeButton("いいえ"){ dialog, which ->}
            show()
        }

    }

}