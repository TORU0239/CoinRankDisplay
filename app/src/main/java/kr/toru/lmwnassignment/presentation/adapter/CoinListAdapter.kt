package kr.toru.lmwnassignment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import kr.toru.lmwnassignment.R
import kr.toru.lmwnassignment.data.response.CoinInfoResponse
import kr.toru.lmwnassignment.databinding.CoinListItemBinding
import kr.toru.lmwnassignment.databinding.CoinTopRankListItemBinding
import kr.toru.lmwnassignment.databinding.InviteFriendListItemBinding

class CoinListAdapter(
    private var listItem: List<ItemViewModel> = listOf()
): RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                TopRankCoinItemViewHolder(
                    CoinTopRankListItemBinding.inflate(inflater, parent, false)
                )
            }
            1 -> {
                CoinListItemViewHolder(
                    CoinListItemBinding.inflate(inflater, parent, false)
                )
            }
            else -> {
                InviteFriendItemViewHolder(
                    InviteFriendListItemBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when(listItem[position]){
            is ItemViewModel.TopRankingCoinItemViewModel -> 0
            is ItemViewModel.CoinItemViewModel -> 1
            is ItemViewModel.InviteFriendItemViewModel -> 2
        }
    }

    fun setData(newItemList: List<ItemViewModel>) {
        val startPosition = listItem.size
        listItem += newItemList
        notifyItemRangeInserted(startPosition, newItemList.size)
    }
}

sealed class ItemViewModel {
    data class TopRankingCoinItemViewModel(
        val rankedCoinList: List<CoinInfoResponse>
    ): ItemViewModel()

    data class CoinItemViewModel(
        val coinInfo: CoinInfoResponse
    ): ItemViewModel()

    data class InviteFriendItemViewModel(
        val clickListener: () -> Unit
    ): ItemViewModel()
}


class TopRankCoinItemViewHolder(private val binding: CoinTopRankListItemBinding)
    : ItemViewHolder(binding) {
    override fun bind(model: ItemViewModel) {
        model as ItemViewModel.TopRankingCoinItemViewModel
        binding.coinTopRankItems.txtCoinSymbol1.text = model.rankedCoinList[0].symbol
        binding.coinTopRankItems.txtCoinSymbol2.text = model.rankedCoinList[1].symbol
        binding.coinTopRankItems.txtCoinSymbol3.text = model.rankedCoinList[2].symbol

        binding.coinTopRankItems.imgBitCoin1.load(model.rankedCoinList[0].iconUrl) {
            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        }

        binding.coinTopRankItems.imgBitCoin2.load(model.rankedCoinList[1].iconUrl) {
            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        }

        binding.coinTopRankItems.imgBitCoin3.load(model.rankedCoinList[2].iconUrl) {
            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        }
    }
}

class CoinListItemViewHolder(private val binding: CoinListItemBinding)
    : ItemViewHolder(binding) {

    private val imageLoader = ImageLoader.Builder(binding.root.context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    override fun bind(model: ItemViewModel) {
        model as ItemViewModel.CoinItemViewModel

        with(model.coinInfo) {
            binding.tvCoinName.text = name
            binding.tvCoinSymbol.text = symbol
            binding.tvCoinPrice.text = price
            binding.tvCoinPriceTrend.text = change
            change.toPriceTrendColor().run {
                binding.tvCoinPriceTrend.setTextColor(first)
                binding.imgCoinPriceTrend.setImageResource(second)
            }

            imageLoader.enqueue(
                ImageRequest.Builder(binding.root.context)
                    .data(iconUrl)
                    .target(binding.imgCoin)
                    .build()
            )
        }
    }
}


private fun String.toPriceTrendColor() =
    when {
        this.startsWith("-") -> Pair(R.color.coinPriceDownColor, R.drawable.down_arrow)
        else -> Pair(R.color.coinPriceUpColor, R.drawable.up_arrow)
    }

class InviteFriendItemViewHolder(private val binding: InviteFriendListItemBinding)
    : ItemViewHolder(binding) {
    override fun bind(model: ItemViewModel) {}
}

abstract class ItemViewHolder(private val binding: ViewBinding): ViewHolder(binding.root) {
    abstract fun bind(model: ItemViewModel)
}