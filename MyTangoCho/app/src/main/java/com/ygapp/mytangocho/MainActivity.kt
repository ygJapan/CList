package com.ygapp.mytangocho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_main.*

var intBackgroundColor = 0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonEdit.setOnClickListener {
            val intent = Intent(this,WordListActivity::class.java)
            startActivity(intent)
        }



        button01.setOnClickListener {
            intBackgroundColor = R.color.color01
            constraint_Layoutmain.setBackgroundResource(intBackgroundColor)
        }
        button02.setOnClickListener {
            intBackgroundColor = R.color.color02
            constraint_Layoutmain.setBackgroundResource(intBackgroundColor)
        }
        button03.setOnClickListener {
            intBackgroundColor = R.color.color03
            constraint_Layoutmain.setBackgroundResource(intBackgroundColor)
        }
        button04.setOnClickListener {
            intBackgroundColor = R.color.color04
            constraint_Layoutmain.setBackgroundResource(intBackgroundColor) }
        button05.setOnClickListener {
            intBackgroundColor = R.color.color05
            constraint_Layoutmain.setBackgroundResource(intBackgroundColor)
        }
        button06.setOnClickListener {
            intBackgroundColor = R.color.color06
            constraint_Layoutmain.setBackgroundResource(intBackgroundColor)
        }

        buttonTest.setOnClickListener {
            val intent = Intent(this@MainActivity, TestActivity::class.java)
            when(radioGroup.checkedRadioButtonId){
                //暗記済の単語を除外する場合
                R.id.radioButton1 -> intent.putExtra(getString(R.string.intent_key_memory_flag),true)
                //暗記済の単語を除外しない場合(含める場合)
                R.id.radioButton2 -> intent.putExtra(getString(R.string.intent_key_memory_flag),false)
            }
            startActivity(intent)
        }
    }
}