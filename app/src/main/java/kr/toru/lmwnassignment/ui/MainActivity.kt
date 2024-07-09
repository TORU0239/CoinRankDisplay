package kr.toru.lmwnassignment.ui

import android.os.Bundle
import android.util.Log
import android.view.View
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
                        (binding.rvSearchResult.adapter as CoinListAdapter).setData(convertResponse(result.data))
                    }
                    is MainViewModel.Event.Failure -> {
                        Log.e("Toru", "Failure")
                        // TODO: showing error message
                    }

                    is MainViewModel.Event.Loading -> {
                        binding.progressBar.visibility = if (result.isLoading) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    // temporary item transformer
    private fun convertResponse(coinInfoResponses: List<CoinInfoResponse>): List<ItemViewModel> {
        if (coinInfoResponses.size > 3) {
            val topRankingItemViewModel = listOf(
                ItemViewModel.TopRankingCoinItemViewModel(
                    rankedCoinList = coinInfoResponses.subList(0, 3)
                )
            )

            val textSectionItemViewModel = listOf(
                ItemViewModel.TextSectionItemViewModel(
                    title = "Buy,sell, and hold crypto"
                )
            )

            val remainedCoinResponse = coinInfoResponses.subList(3, coinInfoResponses.size)

            val coinItemViewModel = remainedCoinResponse.map {
                ItemViewModel.CoinItemViewModel(
                    coinInfo = it
                )
            }

            return topRankingItemViewModel + textSectionItemViewModel + coinItemViewModel

        } else {
            return listOf(
                ItemViewModel.TopRankingCoinItemViewModel(
                    rankedCoinList = coinInfoResponses
                )
            )
        }
    }


    private fun loadCoinList() {
        lifecycleScope.launch {
            viewModel.getCoins()
        }
    }

    private fun initRecyclerView() {
        binding.rvSearchResult.adapter = CoinListAdapter()
    }
}