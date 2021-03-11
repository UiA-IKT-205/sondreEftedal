package com.example.piano1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.piano1.databinding.FragmentHalfToneKeyBinding
import kotlinx.android.synthetic.main.fragment_half_tone_key.view.*

class HalfTonePianoKeyFragment:Fragment() {
    private var _binding: FragmentHalfToneKeyBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var onKeyUp:((note:String) -> Unit)? = null
    var onKeyDown:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHalfToneKeyBinding.inflate(inflater)
        val view = binding.root

        view.halfToneKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v:View, event: MotionEvent): Boolean{
                when(event?.action){
                    MotionEvent.ACTION_UP -> this@HalfTonePianoKeyFragment.onKeyUp?.invoke(note)
                    MotionEvent.ACTION_DOWN -> this@HalfTonePianoKeyFragment.onKeyDown?.invoke(note)
                }
                return true
            }
        })


        return view
    }
    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            HalfTonePianoKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}