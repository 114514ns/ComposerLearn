package cn.pprocket.composerlearn.page

import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController


class VideoDetailPage () {

}

@Composable
fun VideoDetailPage(navController: NavController) {
    VideoPlayer("https://dlink.host/1drv/aHR0cHM6Ly8xZHJ2Lm1zL3YvcyFBa2lMd2djRWVHYkR4am00SVl6RWxlQzlSNHI0P2U9UUprNVBP.mp4")
}
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val density = LocalDensity.current.density

    // Create an AndroidView that wraps the VideoView
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { VideoView(context).apply { setVideoPath(videoUrl) } },
        update = { videoView ->
            // Configure the VideoView
            videoView.setOnPreparedListener { mediaPlayer ->
                // Adjust the size of the VideoView based on the video dimensions
                val videoWidth = mediaPlayer.videoWidth
                val videoHeight = mediaPlayer.videoHeight

                val screenWidth = context.resources.displayMetrics.widthPixels
                val screenHeight = context.resources.displayMetrics.heightPixels

                val aspectRatio = videoWidth.toFloat() / videoHeight.toFloat()

                val adjustedWidth: Int
                val adjustedHeight: Int

                if (screenWidth / aspectRatio > screenHeight) {
                    adjustedWidth = (screenHeight * aspectRatio).toInt()
                    adjustedHeight = screenHeight
                } else {
                    adjustedWidth = screenWidth
                    adjustedHeight = (screenWidth / aspectRatio).toInt()
                }

                val layoutParams = videoView.layoutParams
                layoutParams.width = adjustedWidth
                layoutParams.height = adjustedHeight
                videoView.layoutParams = layoutParams

                // Start playing the video
                videoView.start()
            }
        }
    )
}