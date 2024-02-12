package cn.pprocket.composerlearn.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.pprocket.composerlearn.page.getImageLoader
import cn.pprocket.`object`.User
import cn.pprocket.`object`.Video
import coil.compose.rememberImagePainter
import kotlin.random.Random

@Composable
fun VideoCard(video: Video) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
            .height(380.dp)
            .clip(RoundedCornerShape(22.dp))
    ) {
        Column {

            Row(
                modifier = Modifier.weight(2f)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = "https://www.loliapi.com/acg/pp",
                        imageLoader = getImageLoader()
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(3f)
                        .clip(RoundedCornerShape(12.dp))
                )
                Text(
                    text = video.author.name,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {

                        }

                )
                Box(modifier = Modifier
                    .padding(8.dp)
                    .weight(8f))

            }
            Image(
                painter = rememberImagePainter(
                    data = video.cover,
                    imageLoader = getImageLoader()
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .weight(8f)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = video.title,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .weight(2.5f)
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .weight(2f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "观看次数"
                )
                Text(
                    text = "观看次数: ${video.views}",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "点赞次数"
                )
                Text(
                    text = "点赞次数: ${video.views+Random.nextInt(100)}",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVideoCard() {
    var video = Video()
    video.title = "Video Title"
    video.cover =
        "https://dlink.host/1drv/aHR0cHM6Ly8xZHJ2Lm1zL3YvcyFBa2lMd2djRWVHYkR4am00SVl6RWxlQzlSNHI0P2U9UUprNVBP.mp4"
    video.setLength(100)
    val author = User()
    author.name = "Uploader"
    video.author = author

    VideoCard(
        video
    )
}