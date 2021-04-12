package com.example.piano1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano1.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val FullTones = listOf("C",  "D", "E", "F",  "G",  "A",  "B", "C2",  "D2",  "E2", "F2",  "G2")
    private val HalfTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        FullTones.forEach {
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)

            fullTonePianoKey.onKeyDown =  {
                println("Key down $it")
            }

            fullTonePianoKey.onKeyUp = {
               println("Key up $it")
            }

            ft.add(view.pianoKeys.id,fullTonePianoKey,"note_$it")
        }
        HalfTones.forEach{
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)

            halfTonePianoKey.onKeyUp ={
                println("Key up $it")
            }
            halfTonePianoKey.onKeyDown = {
                println("Key down $it")
            }
            ft.add(view.pianoKeys.id,halfTonePianoKey,"note_$it")
        }

        ft.commit()



        return view
    }

}