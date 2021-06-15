package com.cd.cleanarchitecture.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cd.cleanarchitecture.R
import com.cd.cleanarchitecture.adapters.CharacterGridAdapter
import com.cd.cleanarchitecture.api.*
import com.cd.cleanarchitecture.api.APIConstants.BASE_API_URL
import com.cd.cleanarchitecture.databinding.FragmentCharacterListBinding
import com.cd.cleanarchitecture.presentation.CharacterListViewModel
import com.cd.cleanarchitecture.usecases.GetAllCharactersUseCase
import com.cd.cleanarchitecture.utils.getViewModel
import com.cd.cleanarchitecture.utils.setItemDecorationSpacing
import com.cd.cleanarchitecture.utils.showLongToast
import kotlinx.android.synthetic.main.fragment_character_list.*


class CharacterListFragment : Fragment() {

    //region Fields

    private lateinit var characterGridAdapter: CharacterGridAdapter
    private lateinit var listener: OnCharacterListFragmentListener

    private val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val getAllCharactersUseCase : GetAllCharactersUseCase by lazy {
        GetAllCharactersUseCase(characterRequest)
    }

    private val viewModel : CharacterListViewModel by lazy {
        getViewModel {
            CharacterListViewModel(getAllCharactersUseCase)
        }
    }

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                viewModel.onLoadMoreItems(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
    }

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnCharacterListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnCharacterListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return DataBindingUtil.inflate<FragmentCharacterListBinding>(
            inflater,
            R.layout.fragment_character_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@CharacterListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterGridAdapter = CharacterGridAdapter { character ->
            listener.openCharacterDetail(character)
        }
        characterGridAdapter.setHasStableIds(true)

        rvCharacterList.run{
            addOnScrollListener(onScrollListener)
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))

            adapter = characterGridAdapter
        }

        srwCharacterList.setOnRefreshListener {
            viewModel.onRetryGetAllCharacter(rvCharacterList.adapter?.itemCount ?: 0)
        }

        viewModel.events.observe(viewLifecycleOwner, Observer {
            events ->
                events?.getContentIfNotHandled()?.let {
                    when(it){
                        CharacterListViewModel.CharacterListNavigation.HideLoading -> {
                            srwCharacterList.isRefreshing = false
                        }
                        is CharacterListViewModel.CharacterListNavigation.ShowCharacterError -> {
                            context?.showLongToast("Error")
                        }
                        is CharacterListViewModel.CharacterListNavigation.ShowCharacterList -> {
                            it.run {
                                characterGridAdapter.addData(characterList)
                            }
                        }
                        CharacterListViewModel.CharacterListNavigation.ShowLoading -> {
                            srwCharacterList.isRefreshing = true
                        }
                    }
                }
        })

        viewModel.onGetAllCharacters()
    }

    //endregion

    //region Inner Classes & Interfaces

    interface OnCharacterListFragmentListener {
        fun openCharacterDetail(character: CharacterServer)
    }

    //endregion

    //region Companion object

    companion object {

       @JvmStatic
        fun newInstance(args: Bundle? = Bundle()) = CharacterListFragment().apply {
           arguments = args
       }
    }

    //endregion
}