package cn.pprocket.composerlearn

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.pprocket.composerlearn.page.*
import cn.pprocket.composerlearn.ui.theme.ComposerLearnTheme
import cn.pprocket.impl.AlphaImpl
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security


var gson = Gson()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking() {
            Security.addProvider(BouncyCastleProvider())
            runOnUiThread {
                //Toast.makeText(this@MainActivity, "登录成功\nCookie   " , Toast.LENGTH_SHORT).show()
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            ComposerLearnTheme {
                // A surface container using the 'background' color from the theme
                Root()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Root() {
        val navController = rememberNavController()
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("主页", "我喜欢的", "设置")

        LaunchedEffect(Unit) {
            try {
                // 在后台线程执行网络请求
                withContext(Dispatchers.IO) {
                    client.login("7c706e97-108a-3f61-8e07-14da33dea564")
                    // 在主线程更新 UI
                    withContext(Dispatchers.Main) {
                        //Toast.makeText(this@MainActivity, "登录成功\nCookie   " , Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // 处理异常，例如打印日志或显示错误信息
                e.printStackTrace()
            }
        }


        Scaffold(

            bottomBar = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(72.dp)
                        .fillMaxWidth(),
                ) {
                    // 第一个图标和文字
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    selectedItem = 0
                                    navController.navigate("home")
                                },
                                modifier = Modifier.size(50.dp) // 设置 IconButton 的大小
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = "Home",
                                    tint = if (selectedItem == 0) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        }
                    }

                    // 第二个图标和文字
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    selectedItem = 1
                                    navController.navigate("list")
                                },
                                modifier = Modifier.size(50.dp) // 设置 IconButton 的大小
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Favorite",
                                    tint = if (selectedItem == 1) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        }
                    }

                    // 第三个图标和文字
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    selectedItem = 2
                                    // navController.navigate("video")
                                },
                                modifier = Modifier.size(50.dp) // 设置 IconButton 的大小
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings",
                                    tint = if (selectedItem == 2) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        }
                    }
                }
                /*
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Home, contentDescription = null)
                                    1 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                when (index) {
                                    0 -> navController.navigate("home")
                                    1 -> navController.navigate("list")
                                    2 -> navController.navigate("video")
                                    // 添加更多的图标处理逻辑...
                                }
                            }
                        )
                    }
                }

                 */


            },
            content = {

                // 使用 Column 布局容器
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 70.dp)

                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                    )

                    {
                        composable("list") { VideoListPage(navController) }
                        composable("home") { UserPage(navController) }
                        composable("video") { VideoDetailPage(navController) }
                    }
                }
            }
        )
    }


    @Preview
    @Composable
    fun GreetingPreview() {
        ComposerLearnTheme {
            Root()
        }
    }

    companion object {
        var client = AlphaImpl()
    }

    @Composable
    fun NavIcon() {
    }
}

