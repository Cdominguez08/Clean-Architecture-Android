package com.cd.cleanarchitecture.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cd.cleanarchitecture.R
import com.cd.cleanarchitecture.adapters.FavoriteListAdapter
import com.cd.cleanarchitecture.api.APIConstants.BASE_API_URL
import com.cd.cleanarchitecture.api.CharacterRequest
import com.cd.cleanarchitecture.database.CharacterDao
import com.cd.cleanarchitecture.database.CharacterDatabase
import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.databinding.FragmentFavoriteListBinding
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.presentation.FavoriteListViewModel
import com.cd.cleanarchitecture.usecases.GetAllFavoriteCharactersUseCase
import com.cd.cleanarchitecture.utils.getViewModel
import com.cd.cleanarchitecture.utils.setItemDecorationSpacing
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment() {

    //region Fields

    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var listener: OnFavoriteListFragmentListener
    private lateinit var characterRequest: CharacterRequest
    private val characterDao: CharacterDao by lazy {
        CharacterDatabase.getDatabase(activity!!.applicationContext).characterDao()
    }

    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase by lazy {
        GetAllFavoriteCharactersUseCase(characterDao)
    }

    private val viewModel : FavoriteListViewModel by lazy {
        getViewModel {
            FavoriteListViewModel(getAllFavoriteCharactersUseCase)
        }
    }

    //endregion

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnFavoriteListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnFavoriteListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        characterRequest = CharacterRequest(BASE_API_URL)

        return DataBindingUtil.inflate<FragmentFavoriteListBinding>(
            inflater,
            R.layout.fragment_favorite_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@FavoriteListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListAdapter = FavoriteListAdapter { character ->
            listener.openCharacterDetail(character)
        }
        favoriteListAdapter.setHasStableIds(true)

        rvFavoriteList.run {
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))
            adapter = favoriteListAdapter
        }

        viewModel.favoriteCharacterList.observe(viewLifecycleOwner, Observer(viewModel::onFavoriteCharacterList))

        viewModel.events.observe(viewLifecycleOwner, Observer {
            events ->
                events?.getContentIfNotHandled()?.let {
                    when(it){
                        is FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList -> it.run {
                            tvEmptyListMessage.isVisible = false
                            favoriteListAdapter.updateData(characterList)
                        }
                        FavoriteListViewModel.FavoriteListNavigation.ShowEmptyListMessage -> {
                            tvEmptyListMessage.isVisible = true
                            favoriteListAdapter.updateData(emptyList())
                        }
                    }
                }
        })
    }

    //endregion

    //region Inner Classes & Interfaces

    interface OnFavoriteListFragmentListener {
        fun openCharacterDetail(character: Character)
    }

    //endregion

    //region Companion object

    companion object {

        fun newInstance(args: Bundle? = Bundle()) = FavoriteListFragment().apply {
            arguments = args
        }
    }

    //endregion
}