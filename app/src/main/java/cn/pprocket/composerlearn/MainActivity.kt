package cn.pprocket.composerlearn

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cn.pprocket.Client
import cn.pprocket.cn.pprocket.impl.BilibiliImpl
import cn.pprocket.composerlearn.MainActivity.Companion.client
import cn.pprocket.composerlearn.page.*
import cn.pprocket.composerlearn.ui.theme.ComposerLearnTheme
import cn.pprocket.impl.AlphaImpl
import cn.pprocket.impl.AzureImpl
import kotlinx.coroutines.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.UUID


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
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val navController = rememberNavController()
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("主页", "我喜欢的", "设置")
        val context = LocalContext.current
        val isLoginExecuted = rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(isLoginExecuted) {
            if (!isLoginExecuted.value) {
                try {
                    // 在后台线程执行网络请求
                    withContext(Dispatchers.IO) {
                        login("bilibili", "", context)
                        // 在主线程更新 UI
                    }

                    // 设置为 true，以防止在屏幕方向变化时重新执行
                    isLoginExecuted.value = true
                } catch (e: Exception) {
                    // 处理异常，例如打印日志或显示错误信息
                    e.printStackTrace()
                }
            }
        }
        val openAlertDialog = rememberSaveable { mutableStateOf(true) }

        // ...
        when {
            // ...
            openAlertDialog.value -> {
                AlertDialogExample(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        openAlertDialog.value = false
                        //println("Confirmation registered") // Add logic here to handle confirmation.
                    },
                    dialogTitle = "提示",
                    dialogText = "" +
                            "1. 本软件仅供学习交流使用，不得用于任何商业用途，否则后果自负\n\n" +
                            "2. 版本：24.2.13  Work in progress\n\n" +
                            "3. Made with ❤️ by pprocket\n\n" +
                            "4. 本软件使用的是开源协议，你可以在github上找到源代码\n\n" +
                            "5. 第一页暂时可以忽略",
                    icon = Icons.Default.Info
                )
            }
        }
        Scaffold(

            bottomBar = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(if (isLandscape) 0.dp else 72.dp)
                        .fillMaxWidth(),
                ) {
                    // 第一个图标和文字


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

            }
        ) {

            // 使用 Column 布局容器
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (isLandscape)0.dp else 72.dp)

            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                )

                {
                    composable("list") { VideoListPage(navController) }
                    composable("home") { UserPage(navController) }
                    composable(
                        "video/{link}",
                        arguments = listOf(navArgument("link") {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->
                        val string = backStackEntry.arguments?.getString("link")
                        VideoDetailPage(navController, string)
                    }
                }
            }
        }
    }


    companion object {
        var client: Client = AlphaImpl()
    }

    @Composable
    fun NavIcon() {
    }
}

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
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
                Text("确认")
            }
        },
    )
}

fun login(type: String, token: String, context: Context) {
    val preferencesManager = PreferencesManager(context)
    if (type == "bilibili") {
        client = BilibiliImpl()
        var data = ""
        if (token == "") {
            data = preferencesManager.getData("bili", "none")
        } else {
            data = token
            preferencesManager.saveData("bili", token)
        }
        client.login(data)

    } else if (type == "alpha") {

        var uuid = preferencesManager.getData("uuid", "none")
        if (uuid == "none") {
            uuid = UUID.randomUUID().toString()
            preferencesManager.saveData("uuid", uuid)
        }
        uuid = preferencesManager.getData("uuid", "none")
        client = AlphaImpl()
        client.login(uuid)

    } else if (type == "azure") {
        client = AzureImpl()
        client.login(token)
        preferencesManager.saveData("kuaikan", token)
    }

    PreferencesManager(context).saveData("type", type)
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, "登录成功 当前源 $type", Toast.LENGTH_SHORT).show()
    }
}

