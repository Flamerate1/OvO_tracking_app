package com.f1forhelp.ovo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.f1forhelp.ovo.data.BleedEvent
import com.f1forhelp.ovo.data.Cycle
import com.f1forhelp.ovo.menu.AppNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BleedEvent.initDb(this)
        Cycle.initDb(this)

        val bleedEvents = BleedEvent.getAll()
        bleedEvents.forEach {
            println("Bleed Event: ${it.epochMillis}")
        }
        val cycles = Cycle.getAll()
        cycles.forEach {
            println("Prediction Date: ${it.predictionDateMs}")
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



