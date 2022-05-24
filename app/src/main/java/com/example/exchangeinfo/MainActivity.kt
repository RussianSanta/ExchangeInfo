package com.example.exchangeinfo


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exchangeinfo.ui.theme.ExchangeInfoTheme
import com.skydoves.landscapist.glide.GlideImage
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExchangeInfoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(this)
                }
            }
        }
    }
}

@Composable
fun Greeting(context: Context) {
    val name = remember { mutableStateOf("") }
    val imgLink = remember { mutableStateOf("") }
    val state = remember {
        mutableStateOf("Unknown")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                imageModel = imgLink.value,
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
            )        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Price of ${name.value}: ${state.value} USD")
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextField( value = name.value,onValueChange = {newText -> name.value = newText})
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    getPriceData(name.value, context, state)
                    getImgData(name.value, context, imgLink)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(text = "Get price")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ExchangeInfoTheme {

    }
}

fun getPriceData(name: String, context: Context, mState: MutableState<String>) {
    val url = "https://api.coingecko.com/api/v3/coins/$name"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            val temp = obj.getJSONObject("market_data").getJSONObject("current_price")
            mState.value = temp.getString("usd")
            Log.d("MyLog", "Response: ${temp.getString("usd")}")
        },
        {
            Log.d("MyLog", "Volley error: $it")
        }
    )
    queue.add(stringRequest)
}

fun getImgData(name: String, context: Context, mState: MutableState<String>) {
    val url = "https://api.coingecko.com/api/v3/coins/$name"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            val temp = obj.getJSONObject("image")
            mState.value = temp.getString("small")
            Log.d("MyLog", "Response: ${temp.getString("small")}")
        },
        {
            Log.d("MyLog", "Volley error: $it")
        }
    )
    queue.add(stringRequest)
}