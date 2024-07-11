package kr.toru.lmwnassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.databinding.BottomSheetDetailBinding
import kr.toru.lmwnassignment.vm.DetailViewModel

@AndroidEntryPoint
class DetailBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDetailBinding

    private val viewModel: DetailViewModel by viewModels()

    private val uuid: String by lazy {
        requireArguments().getString(PARAM_UUID) ?: ""
    }

    companion object {

        private const val PARAM_UUID = "name"

        fun newInstance(uuid: String): DetailBottomSheetFragment {
            return DetailBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_UUID, uuid)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectData()

        apiCall {
            viewModel.getCoinDetail(uuid = uuid)
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.eventFlow.collect { event ->
                when (event) {
                   is DetailViewModel.Event.Success -> {
                       // TODO : binding data

                   }
                   else -> {

                   }
                }
            }
        }
    }

    private fun apiCall(function: suspend () -> Unit) {
        lifecycleScope.launch {
            function()
        }
    }
}