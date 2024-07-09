package kr.toru.lmwnassignment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kr.toru.lmwnassignment.R
import kr.toru.lmwnassignment.data.response.CoinInfoResponse
import kr.toru.lmwnassignment.databinding.CoinListItemBinding
import kr.toru.lmwnassignment.databinding.CoinTopRankListItemBinding
import kr.toru.lmwnassignment.databinding.InviteFriendListItemBinding
import kr.toru.lmwnassignment.databinding.TextListItemBinding
import kotlin.math.absoluteValue

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
            2 -> {
                TextSectionItemViewHolder(
                    TextListItemBinding.inflate(inflater, parent, false)
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
            is ItemViewModel.TextSectionItemViewModel -> 2
            is ItemViewModel.InviteFriendItemViewModel -> 3
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

    data class TextSectionItemViewModel(
        val title: String
    ): ItemViewModel()
}


class TopRankCoinItemViewHolder(private val binding: CoinTopRankListItemBinding)
    : ItemViewHolder(binding) {

    private val imageLoader = ImageLoader.Builder(binding.root.context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    override fun bind(model: ItemViewModel) {
        model as ItemViewModel.TopRankingCoinItemViewModel
        binding.coinTopRankItems.txtCoinSymbol1.text = model.rankedCoinList[0].symbol
        binding.coinTopRankItems.txtCoinSymbol2.text = model.rankedCoinList[1].symbol
        binding.coinTopRankItems.txtCoinSymbol3.text = model.rankedCoinList[2].symbol

        imageLoader.enqueue(
            ImageRequest.Builder(binding.root.context)
                .data(model.rankedCoinList[0].iconUrl)
                .target(binding.coinTopRankItems.imgBitCoin1)
                .build()
        )

        imageLoader.enqueue(
            ImageRequest.Builder(binding.root.context)
                .data(model.rankedCoinList[1].iconUrl)
                .target(binding.coinTopRankItems.imgBitCoin2)
                .build()
        )

        imageLoader.enqueue(
            ImageRequest.Builder(binding.root.context)
                .data(model.rankedCoinList[2].iconUrl)
                .target(binding.coinTopRankItems.imgBitCoin3)
                .build()
        )

        binding.coinTopRankItems.txtCoinName1.text = model.rankedCoinList[0].name
        binding.coinTopRankItems.txtCoinName2.text = model.rankedCoinList[1].name
        binding.coinTopRankItems.txtCoinName.text = model.rankedCoinList[2].name

        binding.coinTopRankItems.tvCoinPriceTrend1.text = model.rankedCoinList[0].change.toDouble().absoluteValue.toString()
        binding.coinTopRankItems.tvCoinPriceTrend2.text = model.rankedCoinList[1].change.toDouble().absoluteValue.toString()
        binding.coinTopRankItems.tvCoinPriceTrend.text = model.rankedCoinList[2].change.toDouble().absoluteValue.toString()


        for (i in 0 until 3) {
            val (textColor, image) = model.rankedCoinList[i].change.toPriceTrendColor()
            val textView = when(i) {
                0 -> binding.coinTopRankItems.tvCoinPriceTrend1
                1 -> binding.coinTopRankItems.tvCoinPriceTrend2
                else -> binding.coinTopRankItems.tvCoinPriceTrend
            }

            textView.setTextColor(
                ContextCompat.getColor(binding.root.context, textColor)
            )
            textView.setCompoundDrawablesWithIntrinsicBounds(
                image, 0, 0, 0
            )
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
            binding.tvCoinPriceTrend.text = change.toDouble().absoluteValue.toString()

            val (textColor, image) = change.toPriceTrendColor()

            binding.tvCoinPriceTrend.setTextColor(
                ContextCompat.getColor(binding.root.context, textColor)
            )
            binding.imgCoinPriceTrend.setImageResource(image)

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

class TextSectionItemViewHolder(private val binding: TextListItemBinding): ItemViewHolder(binding) {
    override fun bind(model: ItemViewModel) {
        model as ItemViewModel.TextSectionItemViewModel
        binding.txtTitle.text = model.title
    }
}

abstract class ItemViewHolder(private val binding: ViewBinding): ViewHolder(binding.root) {
    abstract fun bind(model: ItemViewModel)
}