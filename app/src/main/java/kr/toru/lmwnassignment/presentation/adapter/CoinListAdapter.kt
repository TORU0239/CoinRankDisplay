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
    override fun bind(model: ItemViewModel) {}
}

class CoinListItemViewHolder(private val binding: CoinListItemBinding)
    : ItemViewHolder(binding) {
    override fun bind(model: ItemViewModel) {}
}

class InviteFriendItemViewHolder(private val binding: InviteFriendListItemBinding)
    : ItemViewHolder(binding) {
    override fun bind(model: ItemViewModel) {}
}

abstract class ItemViewHolder(private val binding: ViewBinding): ViewHolder(binding.root) {
    abstract fun bind(model: ItemViewModel)
}