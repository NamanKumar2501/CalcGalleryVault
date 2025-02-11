

package com.example.calculator.videos

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.R

class VideoPlayerActivity : AppCompatActivity() {

    lateinit var videoView: VideoView

    // on below line we are creating
    // a variable for our video url.
    private var videoUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoUrl = intent.getStringExtra("VIDEO_URL")

        videoView = findViewById(R.id.videoView)

        // on below line we are creating
        // uri for our video view.
        val uri: Uri = Uri.parse(videoUrl)

        // on below line we are setting
        // video uri for our video view.
        videoView.setVideoURI(uri)

        // on below line we are creating variable
        // for media controller and initializing it.
        val mediaController = MediaController(this)

        // on below line we are setting anchor
        // view for our media controller.
        mediaController.setAnchorView(videoView)

        // on below line we are setting media player
        // for our media controller.
        mediaController.setMediaPlayer(videoView)

        // on below line we are setting media
        // controller for our video view.
        videoView.setMediaController(mediaController)

        // on below line we are
        // simply starting our video view.
        videoView.start()

        // Set up long press listener to show download confirmation dialog
        videoView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                videoUrl?.let {
                    showDownloadConfirmationDialog(it)
                }
                return true
            }
        })
    }

    private fun showDownloadConfirmationDialog(videoUrl: String) {
        // Create an AlertDialog to confirm the download
        val dialog = AlertDialog.Builder(this)
            .setTitle("Download Video")
            .setMessage("Are you sure you want to download this video?")
            .setPositiveButton("Yes") { _, _ ->
                downloadVideo(videoUrl)  // Proceed to download the video
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()  // Dismiss the dialog without doing anything
            }
            .create()

        // Show the dialog
        dialog.show()
    }

    private fun downloadVideo(videoUrl: String) {
        try {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(videoUrl)
            val request = DownloadManager.Request(uri)

            // Set the custom folder "CalVideos" inside the Downloads directory
            val downloadFolder = "CalVideos"
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "$downloadFolder/video_${System.currentTimeMillis()}.mp4"
            )

            // Enable notifications for the download
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            // Add the download request to the DownloadManager
            downloadManager.enqueue(request)

            // Show a success message
            Toast.makeText(this, "Video is downloading to $downloadFolder", Toast.LENGTH_SHORT)
                .show()

        } catch (e: Exception) {
            // Handle exceptions (e.g., permissions, invalid URL)
            e.printStackTrace()
            Toast.makeText(this, "Failed to download video", Toast.LENGTH_SHORT).show()
        }
    }
}

