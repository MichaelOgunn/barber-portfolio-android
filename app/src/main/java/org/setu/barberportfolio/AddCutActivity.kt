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
import android.content.Intent
import kotlin.jvm.java

class AddCutActivity: BaseActivity() {
    private lateinit var styleName: EditText
    private lateinit var notes: EditText
    private var editingId: Long? = null
    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var locationText: TextView
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null
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
                    }
                    selectedImageUri = uri
                    imagePreview.setImageURI(uri)
                }
            }

        findViewById<Button>(R.id.imagePickButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }
        locationText = findViewById(R.id.locationText)

        val mapPicker =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    selectedLatitude = data.getDoubleExtra("latitude", 0.0)
                    selectedLongitude = data.getDoubleExtra("longitude", 0.0)
                    showLocation()
                }
            }

        findViewById<Button>(R.id.buttonLoc).setOnClickListener {
            val intent = Intent(this, MapPickerActivity::class.java)
            mapPicker.launch(intent)
        }



        editingId = intent.getLongExtra("cutId", -1L).takeIf { it != -1L }
        if (editingId != null) {
            findViewById<TextView>(R.id.formTitle).text = "Edit Cut"
            val cut = AppData.cutStyle.findOne(editingId!!)
            if (cut != null) {
                styleName.setText(cut.styleName)
                notes.setText(cut.notes)
                //restoring photo
                selectedImageUri = cut.imageUri?.let { Uri.parse(it) }
                //restoring location
                selectedLatitude = cut.latitude
                selectedLongitude = cut.longitude
                showLocation()
                if(selectedImageUri != null) {
                    imagePreview.setImageURI(selectedImageUri)
                }else{
                    imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            }
        }
        if (savedInstanceState != null) {
            savedInstanceState.getString("imageUri")?.let {
                selectedImageUri = Uri.parse(it)
                imagePreview.setImageURI(selectedImageUri)
            }
            if (savedInstanceState.containsKey("latitude")) {
                selectedLatitude = savedInstanceState.getDouble("latitude")
            }
            if (savedInstanceState.containsKey("longitude")) {
                selectedLongitude = savedInstanceState.getDouble("longitude")
            }
            showLocation()
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {savecut()}
        findViewById<Button>(R.id.cancelButton).setOnClickListener {finish()}


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("imageUri", selectedImageUri?.toString())
        selectedLatitude?.let { outState.putDouble("latitude", it) }
        selectedLongitude?.let { outState.putDouble("longitude", it) }
    }
    private fun showLocation() {
        val lat = selectedLatitude
        val lon = selectedLongitude
        locationText.text = if (lat != null && lon != null) {
            "Location: %.5f, %.5f".format(lat, lon)
        } else {
            "No location selected"
        }
    }

    private fun savecut() {

        val styleNameText = styleName.text.toString()
        val notesText = notes.text.toString()
        Timber.d("Creating cut: ${styleNameText}")


        if (styleNameText.isNotEmpty()) {
            if (editingId == null) {
                AppData.cutStyle.create(CutStyle(styleName = styleNameText, notes = notesText, imageUri = selectedImageUri?.toString(), latitude = selectedLatitude, longitude = selectedLongitude))
            } else {
                Timber.d("Updating cut id=$editingId: ${styleNameText}")
                AppData.cutStyle.update(CutStyle(id = editingId!!, styleName = styleNameText, notes = notesText, imageUri = selectedImageUri?.toString(), latitude = selectedLatitude, longitude = selectedLongitude))
            }
            finish()
        } else {
            styleName.error = "Style Name cannot be empty"
        }
    }

}