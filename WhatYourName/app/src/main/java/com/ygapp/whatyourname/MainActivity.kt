package com.ygapp.whatyourname

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //使うクラスを決めて名前をつける
        //val 名前を決めて(クラス名):型決めて = 中身決める
//        val textView: TextView = findViewById(R.id.textView)
//        val editText: EditText = findViewById(R.id.editText)
//        val button: Button = findViewById(R.id.button)
//        val imageView: ImageView = findViewById(R.id.imageView)

        button.setOnClickListener(this)

    }
    //buttonが押された時にやることを書く場所
    override fun onClick(p0: View?) {
        //buttonを押されたらtextViewをeditTextの名前さんこんにちは と表示させる
        textView.text = editText.text.toString() + "さん、こんにちは"
        //buttonを押されたらimageViewを表示 画像はhello_image
        imageView.setImageResource(R.drawable.hello_image);
    }
}