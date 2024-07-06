package kr.toru.lmwnassignment.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.decode.SvgDecoder
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.R
import kr.toru.lmwnassignment.data.repository.RemoteRepository
import kr.toru.lmwnassignment.network.ApiService
import kr.toru.lmwnassignment.network.httpClientAndroid

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        test()
        loadImage()
    }

    private fun test() {
        CoroutineScope(Dispatchers.IO).launch {
            val remoteRepository = RemoteRepository(
                apiService = ApiService(
                    httpClient = httpClientAndroid
                )
            )

            val coins = remoteRepository.getCoins().getOrNull()?.data?.coins

            remoteRepository.getCoinDetail(coins?.first()?.uuid ?: "Qwsogvtv82FCd").runCatching {
                getOrThrow().data.coin
            }.onSuccess {
                println("Coin Name: ${it.name}, 24hVolume: ${it.volumeFor24h}")
            }.onFailure {
                Log.e("Toru", "Error: ${it.message}")
            }
        }
    }

    private fun loadImage() {
        val image = findViewById<ImageView>(R.id.img)
        image.load("https://cdn.coinranking.com/mgHqwlCLj/usdt.svg") {
            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        }
    }
}