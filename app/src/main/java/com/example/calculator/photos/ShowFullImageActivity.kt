
package com.example.calculator.photos

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.R
import com.github.chrisbanes.photoview.PhotoView
import com.bumptech.glide.Glide  // Import Glide

class ShowFullImageActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_full_image)

        val currentTouchImage = intent.getStringExtra("image")
        val imageView = findViewById<PhotoView>(R.id.full_image_view)

        // Load the image using Glide
        Glide.with(this)
            .load(currentTouchImage)
            .into(imageView)

        // Set up long press listener to show an AlertDialog
        imageView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                currentTouchImage?.let {
                    showDownloadConfirmationDialog(it)
                }
                return true
            }
        })
    }

    private fun showDownloadConfirmationDialog(imageUrl: String) {
        // Create an AlertDialog to confirm the download
        val dialog = AlertDialog.Builder(this)
            .setTitle("Download Image")
            .setMessage("Are you sure you want to download this image?")
            .setPositiveButton("Yes") { _, _ ->
                downloadImage(imageUrl)  // Proceed to download the image
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()  // Dismiss the dialog without doing anything
            }
            .create()

        // Show the dialog
        dialog.show()
    }

    private fun downloadImage(imageUrl: String) {
        try {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(imageUrl)
            val request = DownloadManager.Request(uri)

            // Set the custom folder "CalImage" inside the Downloads directory
            val downloadFolder = "CalImage"
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "$downloadFolder/image_${System.currentTimeMillis()}.jpg"
            )

            // Enable notifications for the download
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            // Add the download request to the DownloadManager
            downloadManager.enqueue(request)

            // Show a success message
            Toast.makeText(this, "Image is downloading to $downloadFolder", Toast.LENGTH_SHORT)
                .show()

        } catch (e: Exception) {
            // Handle exceptions (e.g., permissions, invalid URL)
            e.printStackTrace()
            Toast.makeText(this, "Failed to download image", Toast.LENGTH_SHORT).show()
        }
    }
}
