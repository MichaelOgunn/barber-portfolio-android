package org.setu.barberportfolio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.barberportfolio.models.CutStyle
import timber.log.Timber

class CutListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CutAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_cut_list)

        recyclerView = findViewById(R.id.cutsRecycleView)

        adapter = CutAdapter(
            cuts = AppData.cutStyle.findAll(),
            onEdit ={ cut ->
                editCut(cut)
            },
            onDelete ={ cut ->
                deleteCut(cut)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cut = AppData.cutStyle.findAll()[position]
                if (direction == ItemTouchHelper.LEFT) {
                    confirmDelete(cut)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    editCut(cut)
                }
                adapter.notifyItemChanged(position) // Reset the swiped item to its original position
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)

        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            adapter.updateCuts(AppData.cutStyle.findAll())
        }
    }
    private fun editCut(cut: CutStyle) {
        Timber.d("Editing cut: ${cut.styleName}")
        val intent = Intent(this, AddCutActivity::class.java)
        intent.putExtra("cutId", cut.id)
        startActivity(intent)
    }
    private fun deleteCut(cut: CutStyle) {
        Timber.d("Deleting cut: ${cut.styleName}")
        AppData.cutStyle.delete(cut.id)
        adapter.updateCuts(AppData.cutStyle.findAll())
    }

    private fun confirmDelete(cut: CutStyle, onCancelled: () -> Unit = {}) {
        AlertDialog.Builder(this)
            .setTitle("Delete this cut?")
            .setMessage("\"${cut.styleName}\" will be removed.")
            .setPositiveButton("Delete") { _, _ ->
                deleteCut(cut)
            }
            .setNegativeButton("Cancel") { _, _ ->
                onCancelled()
            }
            .setOnCancelListener { onCancelled() }
            .show()
    }

}
