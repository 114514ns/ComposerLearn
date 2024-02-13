package cn.pprocket.composerlearn.page

import android.net.Uri
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.VideoView
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController


@Composable
fun VideoDetailPage(navController: NavController, string: String?) {
    var decodedString = Uri.decode(string ?: "https://www.loliapi.com/acg/pp")
    Snackbar {

        Text("The arg is $decodedString")
    }
    VideoPlayer(decodedString )
}
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
        val player = SimpleExoPlayer.Builder(context).build().apply {
            setMediaSource(mediaSource)
            prepare()
        }
        player
    }
    val gestureDetector = remember {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                val seekDistance = distanceX / context.resources.displayMetrics.density
                exoPlayer.seekTo(exoPlayer.currentPosition + seekDistance.toLong() * 1000)
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { PlayerView(context).apply { player = exoPlayer } },
        update = { playerView ->
            playerView.onResume()
            playerView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
        },
    )
}