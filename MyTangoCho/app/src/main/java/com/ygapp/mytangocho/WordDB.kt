package com.ygapp.mytangocho

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WordDB :RealmObject() {
    //問題(主キー)
    @PrimaryKey
    open var strQuestion :String = ""
    //答え
    open var strAnswer :String= ""
    //暗記済フラグ true = 暗記済  false = 未暗記
    open var boolMemoryFlag: Boolean = false


}