package com.example.piano1

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.example.piano1.data.Note
import com.example.piano1.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream


class PianoLayout : Fragment() {
    var onSave:((file: Uri) -> Unit)? = null

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val FullTones = listOf("C",  "D", "E", "F",  "G",  "A",  "B")
    private val HalfTones = listOf("C#", "D#", "F#", "G#", "A#")
    private var score:MutableList<Note> = mutableListOf<Note>()

    var startTime:Long = 0
    var isPlaying:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        FullTones.forEach {
            var startPlay:Long = 0
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)

            fullTonePianoKey.onKeyDown =  {
                if(isPlaying == false){
                    startPlay()
                }else{
                    startPlay = System.nanoTime() - startTime
                }

                println("Key up $it")
            }

            fullTonePianoKey.onKeyUp = {
                var endPlay = System.nanoTime()-startPlay
                val note = Note(it, startPlay,endPlay)
                score.add(note)
                println("Piano key up $note")
            }

            ft.add(view.pianoKeys.id,fullTonePianoKey,"note_$it")
        }
        HalfTones.forEach{
            var startPlay:Long = 0
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)

            halfTonePianoKey.onKeyUp ={
                var endPlay = System.nanoTime() -startPlay
                val note = Note(it, startPlay,endPlay)
                score.add(note)
                println("Piano key up $note")
            }
            halfTonePianoKey.onKeyDown = {
                if(isPlaying == false){
                    startPlay()
                }else{
                    startPlay = System.nanoTime() - startTime
                }

                println("Key up $it")
            }
            ft.add(view.pianoKeys.id,halfTonePianoKey,"note_$it")
        }

        ft.commit()

        view.saveScoreBt.setOnClickListener {
            var fileName = view.fileNameTextEdit.text.toString()
            saveToSd(fileName)
            isPlaying = false
        }


        return view
    }

    private fun startPlay(){
        startTime = System.nanoTime()
        isPlaying = true

    }
    private fun saveToSd(fileName:String){
        val path = this.activity?.getExternalFilesDir(null)
        if(fileName.isNotEmpty() && path != null){
            var fileName = "$fileName.musikk"
            val file = File(path,fileName)
                if(file.exists()) {
                    Toast.makeText(context, "Filename already used", Toast.LENGTH_LONG).show()
                    Log.e("error", "Filename already in use")
                }
                else{
                    if(score.isNotEmpty()){
                    score.forEach {
                    FileOutputStream(File(path, fileName), true).bufferedWriter().use { writer->
                        writer.write("$it" +
                                "\n")
                        writer.close()
                    }}
                    score.clear()

            }
                this.onSave?.invoke(file.toUri());
                }

        }else {
            Log.e("error","Requirements not met")
            Toast.makeText(context,"File can't be empty and must have a filename", Toast.LENGTH_LONG).show()
        }
    }

}