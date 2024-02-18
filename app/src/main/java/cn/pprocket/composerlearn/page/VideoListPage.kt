package cn.pprocket.composerlearn.page

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.pprocket.composerlearn.MainActivity
import cn.pprocket.composerlearn.components.VideoCard
import cn.pprocket.impl.AlphaImpl
import cn.pprocket.`object`.Video
import coil.ImageLoader
import coil.util.DebugLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.util.Random
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


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
    if (recommend.isEmpty()) {
        return
    }
    var page = 1
    val listState = rememberLazyGridState()
    var searchText by remember { mutableStateOf("") }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return super.onPostFling(consumed, available)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                return super.onPreFling(available)
            }
        }
    }
    Column{
        TextField(
            value = searchText,
            onValueChange = { newText -> searchText = newText },
            label = {Text("Search") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth()
                .height(75.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp)),
            keyboardActions = KeyboardActions(onDone = {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = MainActivity.client.search(searchText, 1)
                    withContext(Dispatchers.Main) {
                        recommend = response
                    }
                }
            })
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                //.padding(bottom = 50.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .nestedScroll(nestedScrollConnection)

        ) {

            items(
                recommend.size,
                key = { index -> System.currentTimeMillis()+Random().nextLong()}
            ) { index ->
                val video = recommend[index]
                if (MainActivity.client is AlphaImpl) {
                    video.author.avatar = "https://www.loliapi.com/acg/pp"
                }
                VideoCard(video = video, navController = navController)

            }

        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastIndex ->
                // 如果最后一个可见的项目是列表中的最后一个项目，那么加载更多数据
                if (lastIndex != null && lastIndex >= recommend.size - 1) {
                    // 在后台线程执行网络请求
                    withContext(Dispatchers.IO) {
                        if (searchText == "") {
                            val response = MainActivity.client.getRecommend(++page)
                            // 在主线程更新 UI
                            withContext(Dispatchers.Main) {
                                recommend = recommend + response
                            }
                        } else {
                            val response = MainActivity.client.search(searchText, ++page)
                            // 在主线程更新 UI
                            withContext(Dispatchers.Main) {
                                recommend = recommend + response
                            }
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
        if (response.isSuccessful && response.request.url.toString().endsWith("ceb") && MainActivity.client is AlphaImpl) {
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

@Composable
fun getImageLoader():ImageLoader {
    return  createCustomImageLoader(LocalContext.current)
}

