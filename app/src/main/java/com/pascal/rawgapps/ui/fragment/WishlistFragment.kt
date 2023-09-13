package com.pascal.rawgapps.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pascal.rawgapps.databinding.FragmentWishlistBinding
import com.pascal.rawgapps.ui.adapter.GameListingAdapter
import com.pascal.rawgapps.viewmodel.WishlistFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    private lateinit var gameListingAdapter: GameListingAdapter

    private val viewModel: WishlistFragmentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        observe()
        viewModel.getGames(requireContext())

    }

    private fun gameListingAdapterOnClick(position: Int) {
        val toDetailsFragment =
            WishlistFragmentDirections.actionWishlistFragmentToDetailsFragment(viewModel.listing.value!![position].id)
        findNavController().navigate(toDetailsFragment)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupComponents() {
        gameListingAdapter = GameListingAdapter(this::gameListingAdapterOnClick)
        binding.RecyclerViewFragmentListingRecv.layoutManager = GridLayoutManager(context, 2)
        binding.RecyclerViewFragmentListingRecv.adapter = gameListingAdapter
    }

    fun observe() {
        viewModel.listing.observe(viewLifecycleOwner, Observer {
            it?.let {
                gameListingAdapter.submitList(viewModel.listing.value)
            }
        })
    }
}