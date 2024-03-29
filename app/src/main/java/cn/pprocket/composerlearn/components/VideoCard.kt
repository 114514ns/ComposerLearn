package cn.pprocket.composerlearn.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.pprocket.composerlearn.MainActivity
import cn.pprocket.composerlearn.page.getImageLoader
import cn.pprocket.impl.AlphaImpl
import cn.pprocket.`object`.User
import cn.pprocket.`object`.Video
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@Composable
fun VideoCard(video: Video, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
            .height(380.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        val playLink = MainActivity.client.getPlayLink(video)
                        val encodedPlayLink = Uri.encode(playLink)
                        withContext(Dispatchers.Main) {
                            navController.navigate("video/${encodedPlayLink}")
                        }
                    }

                }
            }

    ) {
        Column {

            Row(
                modifier = Modifier.weight(2f)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = video.author.avatar,
                        imageLoader = getImageLoader()
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(2.5f)
                        .aspectRatio(1f)
                        .clip(CircleShape)

                )
                Column {
                    Text(
                        text = video.author.name,
                        modifier = Modifier
                            .padding(top = 5.dp),
                        style = MaterialTheme.typography.titleMedium

                    )
                    Text(
                        text = Random.nextInt(21, 8547).toString() + "followers",
                        modifier = Modifier,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 13.sp

                    )
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(8f)
                )

            }
            Box {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = video.cover,

                        imageLoader = getImageLoader()

                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.weight(8f)
                        .padding(12.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color.DarkGray, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = video.length.toString() + "s",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(18.dp)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(4.dp),
                    color = Color.White
                )
            }
            Text(
                text = video.title,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .weight(2.3f)
                    .padding(start = 12.dp)
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .weight(2.5f)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        //.background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .clip(RoundedCornerShape(22.dp))
                        .padding(start = 12.dp)
                    //.border(2.dp, Color.Red, RoundedCornerShape(22.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "点赞次数",
                        tint = Color.Red,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = Random.nextInt(1000).toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

