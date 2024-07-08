package kr.toru.lmwnassignment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import kr.toru.lmwnassignment.data.response.CoinInfoResponse
import kr.toru.lmwnassignment.databinding.CoinListItemBinding
import kr.toru.lmwnassignment.databinding.CoinTopRankListItemBinding
import kr.toru.lmwnassignment.databinding.InviteFriendListItemBinding

class CoinListAdapter(
    private val listItem: List<ItemViewModel>
): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = CoinTopRankListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopRankCoinItemViewHolder(binding)
            }
            1 -> {
                val binding = CoinListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CoinListItemViewHolder(binding)
            }
            else -> {
                val binding = InviteFriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return InviteFriendItemViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            0 -> (holder as TopRankCoinItemViewHolder).bind(listItem[position])
            1 -> (holder as CoinListItemViewHolder).bind(listItem[position])
            else -> (holder as InviteFriendItemViewHolder).bind(listItem[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(listItem[position]){
            is ItemViewModel.TopRankingCoinItemViewModel -> 0
            is ItemViewModel.CoinItemViewModel -> 1
            is ItemViewModel.InviteFriendItemViewModel -> 2
        }
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
    : ItemViewHolder<ItemViewModel, CoinTopRankListItemBinding>(binding) {
    override fun bind(model: ItemViewModel) {}
}

class CoinListItemViewHolder(private val binding: CoinListItemBinding)
    : ItemViewHolder<ItemViewModel, CoinListItemBinding>(binding) {
    override fun bind(model: ItemViewModel) {}
}

class InviteFriendItemViewHolder(private val binding: InviteFriendListItemBinding)
    : ItemViewHolder<ItemViewModel, InviteFriendListItemBinding>(binding) {
    override fun bind(model: ItemViewModel) {}
}

abstract class ItemViewHolder<T, VB: ViewBinding>(private val binding: VB): ViewHolder(binding.root) {
    abstract fun bind(model: T)
}