package org.setu.barberportfolio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.barberportfolio.models.CutStyle

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
        val intent = Intent(this, AddCutActivity::class.java)
        intent.putExtra("cutId", cut.id)
        startActivity(intent)
    }
    private fun deleteCut(cut: CutStyle) {
        AppData.cutStyle.delete(cut.id)
        adapter.updateCuts(AppData.cutStyle.findAll())
    }
}
