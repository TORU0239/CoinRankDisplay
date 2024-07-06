package kr.toru.lmwnassignment.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.R
import kr.toru.lmwnassignment.data.repository.RemoteRepository
import kr.toru.lmwnassignment.network.ApiService
import kr.toru.lmwnassignment.network.httpClientAndroid
import kr.toru.lmwnassignment.util.imageloading.getImageLoader
import kr.toru.lmwnassignment.vm.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            viewModel.outputEventFlow.collect { result ->
                when(result) {
                    is MainViewModel.Event.Success -> {
                        result.data.forEach {
                            Log.d("Toru", "Coin Name: ${it.name}, 24hVolume: ${it.volumeFor24h}")
                        }
                    }
                    is MainViewModel.Event.Failure -> {
                        Log.e("Toru", "Failure")
                    }
                }
            }
        }

        loadCoinList()
    }

    private fun loadCoinList() {
        lifecycleScope.launch {
            viewModel.getCoins()
        }
    }


    private fun loadImage() {
        val image = findViewById<ImageView>(R.id.img)
//        image.load("https://cdn.coinranking.com/mgHqwlCLj/usdt.svg") {
//            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
//        }

        getImageLoader(this).enqueue(
            ImageRequest.Builder(this)
                .data("https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg")
                .target(image)
                .build()
        )
    }
}