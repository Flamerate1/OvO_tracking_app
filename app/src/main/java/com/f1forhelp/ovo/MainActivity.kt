package com.f1forhelp.ovo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.room.Room
import com.f1forhelp.ovo.data.BleedEvent
import com.f1forhelp.ovo.data.BleedEventDao
import com.f1forhelp.ovo.menu.AppNav
import com.f1forhelp.ovo.menu.BleedEventList

import com.f1forhelp.ovo.menu.MenuMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BleedEvent.initDb(this)

        val events = BleedEvent.getAll()
        events.forEach {
            println("Bleed event: ${it.epochMillis}")
        }

        enableEdgeToEdge()
        setContent {
        /*val eventsFlow by dao.getAllFlow().collectAsState(initial = emptyList())
        println("Bleed event: $eventsFlow}")
        println(events[0])*/

        AppNav()

        //BleedEventList(events)
        //MenuMain()
        }
    }
}



