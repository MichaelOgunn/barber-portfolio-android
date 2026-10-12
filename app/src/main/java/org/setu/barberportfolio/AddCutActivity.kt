package org.setu.barberportfolio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.RemoteInput
import org.setu.barberportfolio.models.CutStyle

class AddCutActivity: AppCompatActivity() {
    private lateinit var styleName: EditText
    private lateinit var notes: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cut)

        styleName = findViewById(R.id.styleNameInput)
        notes = findViewById(R.id.notesInput)

        findViewById<Button>(R.id.saveButton).setOnClickListener {savecut()}
        findViewById<Button>(R.id.cancelButton).setOnClickListener {finish()}

    }
    private fun savecut() {
        val styleNameText = styleName.text.toString()
        val notesText = notes.text.toString()

        if (styleNameText.isNotEmpty()) {
            val cut = CutStyle(styleName = styleNameText, notes = notesText)
            AppData.cutStyle.create(cut)
            finish()
        } else {
            styleName.error = "Style Name cannot be empty"
        }
    }

}