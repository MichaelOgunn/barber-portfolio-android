package org.setu.barberportfolio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.RemoteInput
import org.setu.barberportfolio.models.CutStyle

class AddCutActivity: AppCompatActivity() {
    private lateinit var styleName: EditText
    private lateinit var notes: EditText
    private var editingId: Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cut)

        styleName = findViewById(R.id.styleNameInput)
        notes = findViewById(R.id.notesInput)

        editingId = intent.getLongExtra("cutId", -1L).takeIf { it != -1L }
        if (editingId != null) {
            findViewById<TextView>(R.id.formTitle).text = "Edit Cut"
            val cut = AppData.cutStyle.findOne(editingId!!)
            if (cut != null) {
                styleName.setText(cut.styleName)
                notes.setText(cut.notes)
            }
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {savecut()}
        findViewById<Button>(R.id.cancelButton).setOnClickListener {finish()}

    }
    private fun savecut() {
        val styleNameText = styleName.text.toString()
        val notesText = notes.text.toString()

        if (styleNameText.isNotEmpty()) {
            if (editingId == null) {
                AppData.cutStyle.create(CutStyle(styleName = styleNameText, notes = notesText))
            } else {
                AppData.cutStyle.update(CutStyle(id = editingId!!, styleName = styleNameText, notes = notesText))
            }
            finish()
        } else {
            styleName.error = "Style Name cannot be empty"
        }
    }

}