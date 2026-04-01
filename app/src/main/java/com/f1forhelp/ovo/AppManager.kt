package com.f1forhelp.ovo

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.f1forhelp.ovo.data.BleedEvent

class AppManager : Application() {


    companion object { // Static variable manipulation here.
        lateinit var instance: AppManager
            private set
    }

    lateinit var db: AppDatabase // Can be accessed with AppManager.instance.db
        private set

    //val bleedEvents: MutableList<BleedEvent> = mutableListOf()
    /*fun popupMessage(text : String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }*/
    fun popupMessage(context: Context, text : String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }



    override fun onCreate() {
        super.onCreate()
        instance = this

        db = AppDatabase.getDatabase(this)
    }
}