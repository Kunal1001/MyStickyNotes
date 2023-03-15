package com.example.mystickynotesbykunalkumar

data class Notes(val text:String = "",
                val uid:String){
    constructor(): this("", "")
}
