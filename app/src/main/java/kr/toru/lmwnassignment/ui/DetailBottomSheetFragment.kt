package kr.toru.lmwnassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.toru.lmwnassignment.databinding.BottomSheetDetailBinding

class DetailBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDetailBinding

    companion object {
        fun newInstance(itemId: Int): DetailBottomSheetFragment {
            return DetailBottomSheetFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}