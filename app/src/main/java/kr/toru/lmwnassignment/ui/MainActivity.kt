package kr.toru.lmwnassignment.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        initSwipeRefreshLayout()
        initEventObserver()
        loadCoinList(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.e("Toru", "onConfigurationChanged: ${newConfig.orientation}")

        lifecycleScope.launch {
            viewModel.resetCoinCall()
        }
    }

    private fun initWindowInset() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private var currentEvent: MainViewModel.Event? = null

    private fun initEventObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.outputEventFlow.collect { result ->
                    when(result) {
                        is MainViewModel.Event.Success -> {
                            (binding.rvSearchResult.adapter as CoinListAdapter).addNewData(
                                newItemList = convertResponse(result.data),
                                currentOffset = result.currentOffset
                            )
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                        is MainViewModel.Event.Failure -> {
                            (binding.rvSearchResult.adapter as CoinListAdapter).addNewData(
                                newItemList = result.data,
                                currentOffset = result.currentOffset
                            )
                            binding.swipeRefreshLayout.isRefreshing = false
                        }

                        is MainViewModel.Event.Loading -> {
                            currentEvent = result
                            binding.progressBar.visibility = if (result.isLoading) View.VISIBLE else View.GONE
                        }
                    }
                }
            }
        }
    }

    // temporary item transformer
    private fun convertResponse(coinInfoResponses: List<CoinInfoResponse>): List<ItemViewModel> {
        if (coinInfoResponses.size > 3) {

            val first3RankedItems = coinInfoResponses.filterList {
                rank <= 3
            }

            if (first3RankedItems.isEmpty()) {
                // paged item
                return coinInfoResponses.map {
                    ItemViewModel.CoinItemViewModel(
                        coinInfo = it
                    )
                }
            } else {
                // top 3 ranking
                val topRankingItemViewModel = listOf(
                    ItemViewModel.TopRankingCoinItemViewModel(
                        rankedCoinList = first3RankedItems,
                    )
                )

                val textSectionItemViewModel = listOf(
                    ItemViewModel.TextSectionItemViewModel(
                        title = getString(R.string.coin_rank_label)
                    )
                )

                val remainedCoinResponse = coinInfoResponses.subList(3, coinInfoResponses.size)

                val coinItemViewModel = remainedCoinResponse.map {
                    ItemViewModel.CoinItemViewModel(
                        coinInfo = it,
                        clickListener = {
                            it.uuid ?.let { param ->
                                showDetailBottomSheet(uuid = param)
                            }
                        }
                    )
                }
                val mutableList = mutableListOf<ItemViewModel>()
                mutableList.addAll(0, coinItemViewModel)

                var neededIndex = 5
                for (i in 0 until mutableList.size) {
                    if (neededIndex < mutableList.size) {
                        mutableList.add(
                            neededIndex - 1,
                            ItemViewModel.InviteFriendItemViewModel(
                                clickListener = {}
                            )
                        )
                        neededIndex *= 2
                    }
                }

                return topRankingItemViewModel + textSectionItemViewModel + mutableList
            }
        } else {
            return listOf(
                ItemViewModel.TopRankingCoinItemViewModel(
                    rankedCoinList = coinInfoResponses
                )
            )
        }
    }


    private fun loadCoinList(isRefreshing: Boolean = false) {
        lifecycleScope.launch {
            viewModel.getCoins(isRefreshing = isRefreshing)
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.progressBarColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            loadCoinList(true)
        }
    }

    private fun initRecyclerView() {
        binding.rvSearchResult.run {
            adapter = CoinListAdapter()

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = (layoutManager as LinearLayoutManager).childCount
                    val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                    val firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val isLoading = if (currentEvent is MainViewModel.Event.Loading) (currentEvent as MainViewModel.Event.Loading).isLoading else false
                    if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadCoinList(false)
                    }
                }
            })
        }
    }

    private fun showDetailBottomSheet(uuid: String) {
        val detailBottomSheet = DetailBottomSheetFragment.newInstance(uuid = uuid)
        detailBottomSheet.show(supportFragmentManager, "DetailBottomSheet")
    }
}