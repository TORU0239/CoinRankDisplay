package kr.toru.lmwnassignment.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.R
import kr.toru.lmwnassignment.data.response.CoinInfoResponse
import kr.toru.lmwnassignment.databinding.ActivityMainBinding
import kr.toru.lmwnassignment.presentation.adapter.CoinListAdapter
import kr.toru.lmwnassignment.presentation.adapter.ItemViewModel
import kr.toru.lmwnassignment.vm.MainViewModel
import okhttp3.internal.filterList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWindowInset()
        initRecyclerView()
        initEventObserver()
        loadCoinList()
    }

    private fun initWindowInset() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initEventObserver() {
        lifecycleScope.launch {
            viewModel.outputEventFlow.collect { result ->
                when(result) {
                    is MainViewModel.Event.Success -> {
                        result.data.forEach {
                            Log.d("Toru", "Coin Name: ${it.name}, 24hVolume: ${it.volumeFor24h}")
                        }

                        (binding.rvSearchResult.adapter as CoinListAdapter).setData(convertResponse(result.data))
                    }
                    is MainViewModel.Event.Failure -> {
                        Log.e("Toru", "Failure")
                    }
                }
            }
        }
    }

    // temporary item transformer
    private fun convertResponse(coinInfoResponses: List<CoinInfoResponse>): List<ItemViewModel> {
        val top3RankedCoins = coinInfoResponses.filterList{
            rank in 1..3
        }

        val topRankingItemViewModel = listOf(
            ItemViewModel.TopRankingCoinItemViewModel(
                rankedCoinList = top3RankedCoins
            )
        )

        val coinItemViewModel = coinInfoResponses.map {
            ItemViewModel.CoinItemViewModel(
                coinInfo = it
            )
        }

        return topRankingItemViewModel + coinItemViewModel
    }


    private fun loadCoinList() {
        lifecycleScope.launch {
            viewModel.getCoins()
        }
    }

    private fun initRecyclerView() {
        binding.rvSearchResult.adapter = CoinListAdapter()
    }


//    private fun loadImage() {
//        val image = findViewById<ImageView>(R.id.img)
//        image.load("https://cdn.coinranking.com/mgHqwlCLj/usdt.svg") {
//            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
//        }
//
//        getImageLoader(this).enqueue(
//            ImageRequest.Builder(this)
//                .data("https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg")
//                .target(image)
//                .build()
//        )
//    }
}