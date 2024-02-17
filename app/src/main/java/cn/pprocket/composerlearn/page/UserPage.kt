package cn.pprocket.composerlearn.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import cn.pprocket.composerlearn.login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage(navController: NavController) {
    var lists = listOf(
        "设置源",
    )
    val passport = remember { mutableStateOf("") }
    val isOpen = remember { mutableStateOf(false) }
    var selectedSource by remember { mutableStateOf("") }
    val dropdownMenuOffset = remember { mutableStateOf(DpOffset(0.dp, 0.dp)) }
    var selecetd = ""
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        lists.forEach() {
            Card(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(15.dp),
                onClick = {
                    selecetd = it
                    isOpen.value = true
                }
            ) {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // 添加空间
        }
    }
    if (isOpen.value) {
        var expanded by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val lists = listOf("天涯", "bilibili", "爱酱")

        AlertDialog(
            onDismissRequest = { isOpen.value = false },
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
        )
        {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                TextField(
                    value = selectedSource,
                    onValueChange = { selectedSource = it },
                    label = { Text("选择源") },
                    modifier = Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
                        // 获取 TextField 的位置和大小
                        val position = coordinates.positionInRoot()
                        val size = coordinates.size.toSize()

                        // 计算 DropdownMenu 的偏移量
                        val offset = DpOffset(0.dp, position.y.dp - 150.dp)
                        // 更新 DropdownMenu 的偏移量
                        dropdownMenuOffset.value = offset
                    }.clickable {
                        expanded = !expanded
                    },
                    enabled = false

                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = dropdownMenuOffset.value // 使用计算出的偏移量
                ) {
                    lists.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedSource = item
                                expanded = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp)) // 添加空间
                TextField(
                    value = passport.value,
                    onValueChange = { newText -> passport.value = newText },
                    label = { Text("输入凭据") },
                    modifier = Modifier.fillMaxWidth()
                        .height(75.dp)
                )
                Spacer(modifier = Modifier.height(8.dp)) // 添加空间
                Button(
                    onClick = {

                        CoroutineScope(Dispatchers.IO).launch {
                            when (selectedSource) {
                                "bilibili" -> {
                                    login("bilibili", passport.value, context)
                                }

                                "天涯" -> {
                                    login("azure", passport.value, context)
                                }

                                "爱酱" -> {
                                    login("alpha", passport.value, context)
                                }
                            }
                        }
                        isOpen.value = false


                    },
                    modifier = Modifier.fillMaxWidth() // 让按钮填充宽度
                ) {
                    Text("确认")
                }
            }
        }
    }
}



