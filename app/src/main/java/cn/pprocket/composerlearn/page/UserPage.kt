package cn.pprocket.composerlearn.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController

import coil.compose.AsyncImage
import kotlin.random.Random



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage(navController: NavController) {
    var lists = listOf(
        "我的收藏",
        "我的关注",
        "我的粉丝",
        "我的作品",
        "我的动态",
        "我的钱包",
        "我的订单",
        "我的客服",
        "我的设置"
    )
    val isOpen = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        lists.forEach() {
            Card(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxSize()
                    .width(400.dp)
                    .padding(15.dp),
                //.wrapContentSize(Alignment.Center),
                onClick = {
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
        }
    }
    if (isOpen.value) {
        MinimalDialog(onDismissRequest = { isOpen.value = false })

    }
}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    val items = listOf("爱酱", "七点空间", "快看漫画", "51漫画")
    val show = remember { mutableStateOf(false) }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            items.forEach {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Row {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = null,
                        )
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(16.dp),
                            onClick = {
                                onDismissRequest()
                                show.value = true
                            }
                        )
                    }
                }
            }
        }
    }

}

