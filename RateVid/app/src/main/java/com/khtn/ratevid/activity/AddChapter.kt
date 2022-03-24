package com.khtn.ratevid.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.ChosenImageAdapter
import com.khtn.ratevid.model.ModelChosenImage
import kotlinx.android.synthetic.main.activity_add_chapter.*

class AddChapter : AppCompatActivity() {
    lateinit var imgsList : ArrayList<ModelChosenImage>
    lateinit var adapter: ChosenImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chapter)
        var customListView = findViewById<RecyclerView>(R.id.recycler)
        imgsList= ArrayList<ModelChosenImage>()
        adapter = ChosenImageAdapter(imgsList,"truyen1","chap1")
        customListView?.adapter = adapter
        customListView?.layoutManager = LinearLayoutManager(this)
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        customListView.addItemDecoration(itemDecoration)
        addImgBtn.setOnClickListener {
            startFileChooser()
        }
    }

    private fun startFileChooser() {
        var i= Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i,1111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1111 &&resultCode==Activity.RESULT_OK&& data!=null){
            var filepath=data.data!!
            imgsList.add(ModelChosenImage(imgsList.size+1,filepath,"Waiting to upload"))
            adapter.notifyItemInserted(imgsList.size)
        }
    }
}