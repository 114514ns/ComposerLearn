package cn.pprocket.composerlearn.page

import android.net.Uri
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import cn.pprocket.cn.pprocket.impl.BilibiliImpl
import cn.pprocket.composerlearn.MainActivity


@Composable
fun VideoDetailPage(navController: NavController, string: String?) {
    var decodedString = Uri.decode(string ?: "https://www.loliapi.com/acg/pp")
    VideoPlayer(decodedString )
}
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val headers = mutableMapOf<String,String>()
    headers["Referer"] = "https://www.bilibili.com"
    headers["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
    val exoPlayer = remember {
        val dataSourceFactory =
            DataSource.Factory {
                val dataSource = DefaultHttpDataSource.Factory()
                // Set a custom authentication request header.
                dataSource.setDefaultRequestProperties(headers)
                dataSource.createDataSource()
            }
        var mediaSource:MediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
        if (MainActivity.client is BilibiliImpl) {
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
        }
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
        onRelease = { playerView ->
            playerView.player = null
            exoPlayer.release()
        }
    )
}