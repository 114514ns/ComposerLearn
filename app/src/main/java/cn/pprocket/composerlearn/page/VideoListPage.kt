package cn.pprocket.composerlearn.page

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import cn.pprocket.composerlearn.MainActivity
import cn.pprocket.composerlearn.components.VideoCard

import cn.pprocket.`object`.Video
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.util.DebugLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.bouncycastle.jce.provider.BouncyCastleProvider
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListPage(navController: NavController) {
    var recommend by remember { mutableStateOf(emptyList<Video>()) }
    var loading by remember { mutableStateOf(false) }
    var imageLoader = createCustomImageLoader(LocalContext.current)
    var showDialog by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        // 在后台线程执行网络请求
        withContext(Dispatchers.IO) {
            val response = MainActivity.client.getRecommend(1)
            // 在主线程更新 UI
            withContext(Dispatchers.Main) {
                recommend = response
            }
        }
    }
    var index = 0
    if (recommend.isEmpty()) {
        return
    }
    var page = 1
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        state = listState,
        modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)

    ) {

        items(
            recommend.size,
            key = { index -> recommend[index].time}
        ) { index ->
            val video = recommend[index]
            /*
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .height(200.dp)
                    .width(325.dp),
                onClick = {
                    navController.navigate("video")
                    //show video info in dialog
                    //showDialog = true
                    //showText = "Title: ${video.title}\n Author : ${video.author}\n "

                }

            ) {
                Image(
                    painter = rememberImagePainter(
                        data = video.cover,
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(0.8f)
                )
                Text(
                    text = video.title,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(0.2f)
                        .clip(MaterialTheme.shapes.medium)
                )
                */
            VideoCard(video = video)

        }

    }

    /*
    val itemsList = (0..5).toList()
    val itemsIndexedList = listOf("A", "B", "C")

    val itemModifier = Modifier.border(1.dp, Color.Blue).height(80.dp).wrapContentSize()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(itemsList) {
            Text("Item is $it", itemModifier)
        }
        /*
        itemsIndexed(itemsIndexedList) { index, item ->
            Text("Item at index $index is $item", itemModifier)
        }

         */
    }

     */
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastIndex ->
                // 如果最后一个可见的项目是列表中的最后一个项目，那么加载更多数据
                if (lastIndex != null && lastIndex >= recommend.size - 1) {
                    // 在后台线程执行网络请求
                    withContext(Dispatchers.IO) {
                        val response = MainActivity.client.getRecommend(++page)
                        // 在主线程更新 UI
                        withContext(Dispatchers.Main) {
                            recommend = recommend + response
                        }
                    }
                }
            }
    }


}


fun createCustomImageLoader(context: Context): ImageLoader {

    //Security.addProvider(BouncyCastleProvider())
    return ImageLoader.Builder(context)
        .okHttpClient {
            // 使用自定义的OkHttpClient
            OkHttpClient.Builder()

                .addInterceptor(EncryptInterceptor())
                .build()
        }
        .logger(DebugLogger()) // 在调试模式下启用日志记录
        .build()
}

private fun decryptResponse(body: ResponseBody?): ByteArray {
    // 使用AES/ECB/NoPadding解密
    val cipher = Cipher.getInstance("AES/ECB/NoPadding", BouncyCastleProvider.PROVIDER_NAME)
    val key = SecretKeySpec("wPK8CxWaOwPuVzgs".toByteArray(), "AES")
    cipher.init(Cipher.DECRYPT_MODE, key)

    // 读取响应体并解密
    val encryptedData = body?.bytes() ?: ByteArray(0)
    return cipher.doFinal(encryptedData)
}

class EncryptInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        // 添加拦截器
        val request = chain.request()
        val response = chain.proceed(request)

        // 检查响应的URL是否以"ceb"结尾
        if (response.isSuccessful && response.request.url.toString().endsWith("ceb")) {
            val decryptedBody = decryptResponse(response.body)
            val response = response.newBuilder()
                .body(ResponseBody.create(response.body?.contentType(), decryptedBody))
                .build()
            // 构建新的响应
            return response
        } else {
            return response
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
@Composable
fun getImageLoader():ImageLoader {
    return  createCustomImageLoader(LocalContext.current)
}