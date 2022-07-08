package dev.mvgioser.registroymembresia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.mvgioser.registroymembresia.ui.fragments.MembresiaFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mainFragment: MembresiaFragment = MembresiaFragment()

        supportFragmentManager.beginTransaction().add(R.id.LinearFragment_Container, mainFragment!!)
            .commit()
    }
    class Fragment : MembresiaFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // replace if you already have a layout
            return inflater.inflate(R.layout.fragment_membresia, container, false)
        }
    }
}