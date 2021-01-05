package com.ygapp.mytangocho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_word_list.*

class WordListActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    lateinit var realm: Realm
    lateinit var results: RealmResults<WordDB>
    lateinit var word_list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    var length: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        constraint_LayoutWordList.setBackgroundResource(intBackgroundColor)

        buttonAddNewWord.setOnClickListener {
            val intent = Intent(this@WordListActivity, EditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_add))
            startActivity(intent)
        }

        buttonBack1.setOnClickListener {
            finish()
        }

        buttonSort.setOnClickListener {
            results = realm.where<WordDB>(WordDB::class.java).findAll().sort(getString(R.string.db_field_memory_flag))

            word_list.clear()

            length = results.size
            for (i in 0..length - 1) {
                if (results[i]!!.boolMemoryFlag) {
                    word_list.add(results[i]!!.strAnswer + ":" + results[i]!!.strQuestion + "[暗記済]")
                } else {
                    word_list.add(results[i]!!.strAnswer + ":" + results[i]!!.strQuestion)
                }
            }

            listview.adapter = adapter

        }

        listview.onItemClickListener = this
        listview.onItemLongClickListener = this
    }

    override fun onResume() {
        super.onResume()
        //realmインスタンスの取得
        realm = Realm.getDefaultInstance()

        //DBに登録している単語を一覧表示(onResumeメソッドでrealmインスタンスを持ってきているのでここ)
        results = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_answer))

        word_list = ArrayList<String>() //表示景気を修正した新しいリスト

        length = results.size
        for (i in 0..length - 1) {
            if (results[i]!!.boolMemoryFlag) {
                word_list.add(results[i]!!.strAnswer + ":" + results[i]!!.strQuestion + "[暗記済]")
            } else {
                word_list.add(results[i]!!.strAnswer + ":" + results[i]!!.strQuestion)
            }
        }
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, word_list)
        listview.adapter = adapter

    }

    override fun onPause() {
        super.onPause()
        //realmインスタンスの後片付け
        realm.close()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedDB = results[p2]!!
        val strSelectedQuestion = selectedDB.strQuestion
        val strSelectedAnswer = selectedDB.strAnswer

        val intent = Intent(this@WordListActivity, EditActivity::class.java)
        intent.putExtra(getString(R.string.intent_key_question), strSelectedQuestion)  //問題
        intent.putExtra(getString(R.string.intent_key_answer), strSelectedAnswer)    //答え
        intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_change))    //ステータス
        startActivity(intent)

    }

    override fun onItemLongClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long): Boolean {
        val selectedDB = results[p2]!!

        val dialog = AlertDialog.Builder(this@WordListActivity).apply {
            setTitle(selectedDB.strAnswer + "の削除")
            setMessage("削除してもいいですか？")

            setPositiveButton("はい") { dialog, which ->
                realm.beginTransaction()
                selectedDB.deleteFromRealm()
                realm.commitTransaction()

                word_list.removeAt(p2)
                listview.adapter = adapter
            }
            setNegativeButton("いいえ") { dialog, which -> }
            show()
        }
        return true //長押しをし続けたときの挙動の設定。
    }

}