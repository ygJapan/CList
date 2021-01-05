package com.ygapp.ksantrainingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Todo 選択肢を入れるためのArrayAdapterをセット
//        val arrayAdapter =ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item)
//        arrayAdapter.add(10)
//        arrayAdapter.add(20)
//        arrayAdapter.add(30)
        val arrayAdapter = ArrayAdapter.createFromResource(this, R.array.number_of_quesution, android.R.layout.simple_spinner_item)

        // Todo spinnerとアダプター(選択肢)を繋ぐ
        spinner.adapter = arrayAdapter

//        Todo 「スタート」ボタンを押したら
////        Todo １． テスト画面を開く(Spinnerから選んだ問題数を渡す）
        buttonStart.setOnClickListener {
        // Todo 選択したアイテムをゲットする
        val numberOfQuestion : Int = spinner.selectedItem.toString().toInt()

        val intent = Intent(this@MainActivity,TestActivity::class.java)
        intent.putExtra("numberOfQuestion",numberOfQuestion)
        startActivity(intent)

        }

    }
}