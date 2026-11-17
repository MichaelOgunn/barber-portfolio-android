package org.setu.barberportfolio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.RemoteInput
import org.setu.barberportfolio.models.CutStyle
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import timber.log.Timber

class AddCutActivity: BaseActivity() {
    private lateinit var styleName: EditText
    private lateinit var notes: EditText
    private var editingId: Long? = null
    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cut)

        styleName = findViewById(R.id.styleNameInput)
        notes = findViewById(R.id.notesInput)
        imagePreview = findViewById(R.id.imageViewAdd)
        val imagePicker =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
                if (uri != null) {
                    try {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (_: SecurityException) {
                        // Some providers don't allow persistable access.
                    }
                    selectedImageUri = uri
                    imagePreview.setImageURI(uri)
                }
            }

        findViewById<Button>(R.id.imagePickButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }
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
        Timber.d("Creating cut: ${styleNameText}")

        if (styleNameText.isNotEmpty()) {
            if (editingId == null) {
                AppData.cutStyle.create(CutStyle(styleName = styleNameText, notes = notesText))
            } else {
                Timber.d("Updating cut id=$editingId: ${styleNameText}")
                AppData.cutStyle.update(CutStyle(id = editingId!!, styleName = styleNameText, notes = notesText))
            }
            finish()
        } else {
            styleName.error = "Style Name cannot be empty"
        }
    }

}