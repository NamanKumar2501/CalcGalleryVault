/*
package com.example.calculator.documents

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.calculator.R
import com.example.calculator.databinding.ActivityDocumentViewerBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

class DocumentViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val pdfUrl = intent.getStringExtra("pdf_url")

        if (pdfUrl != null) {
            val uri = Uri.parse(pdfUrl)
            // Load PDF using PDFView library
            binding.pdfView.fromUri(uri)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageError { page, _ ->
                    // Handle page error
                }
                .onTap { false }
                .scrollHandle(DefaultScrollHandle(this))
                .spacing(10) // in dp
                .load()
        } else {
            // Handle the case when pdfUrl is null
            // Maybe show an error message or finish the activity
            finish()
        }


    }
}*/
package com.example.calculator.documents

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityDocumentViewerBinding

import java.io.File
import java.io.IOException

class DocumentViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentViewerBinding
    private var pdfRenderer: PdfRenderer? = null
    private var currentPageIndex = 0
    private lateinit var currentPage: PdfRenderer.Page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pdfUrl = intent.getStringExtra("pdf_url")
        if (pdfUrl != null) {
            val uri = Uri.parse(pdfUrl)
            val file = File(uri.path ?: return)  // You might need to adjust this based on how the PDF is stored (local storage or URI)

            try {
                val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(parcelFileDescriptor)

                showPage(currentPageIndex)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            finish()  // Handle the case when the URL is null
        }
    }

    private fun showPage(index: Int) {
        pdfRenderer?.let {
            if (index >= 0 && index < it.pageCount) {
                currentPage?.close() // Close the previous page
                currentPage = it.openPage(index)

                val width = resources.displayMetrics.densityDpi / 2
                val height = (width * currentPage.height) / currentPage.width
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

                currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Set the rendered image to the ImageView
                binding.pdfImageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPage.close()
        pdfRenderer?.close()
    }
}
