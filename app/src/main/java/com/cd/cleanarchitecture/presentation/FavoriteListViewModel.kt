package com.cd.cleanarchitecture.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cd.cleanarchitecture.database.CharacterDao
import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.usescases.GetAllFavoriteCharactersUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteListViewModel(
    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
) : ViewModel() {

    sealed class FavoriteListNavigation{
        data class ShowCharacterList(val characterList : List<Character>) : FavoriteListNavigation()
        object ShowEmptyListMessage : FavoriteListNavigation()
    }

    private val disposable = CompositeDisposable()

    //LiveDatas para manejar eventos
    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val events : LiveData<Event<FavoriteListNavigation>> get() = _events

    private val _favoriteCharacterList : LiveData<List<Character>>
    get() = LiveDataReactiveStreams.fromPublisher(
        getAllFavoriteCharactersUseCase.invoke()
    )

    val favoriteCharacterList : LiveData<List<Character>> get() = _favoriteCharacterList

    /*
    disposable.add(
            characterDao.getAllFavoriteCharacters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ characterList ->
                    if(characterList.isEmpty()) {
                        tvEmptyListMessage.isVisible = true
                        favoriteListAdapter.updateData(emptyList())
                    } else {
                        tvEmptyListMessage.isVisible = false
                        favoriteListAdapter.updateData(characterList)
                    }
                },{
                    tvEmptyListMessage.isVisible = true
                    favoriteListAdapter.updateData(emptyList())
                })
        )
     */

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onFavoriteCharacterList(list : List<Character>){

        if(list.isEmpty()){
            _events.value = Event(FavoriteListNavigation.ShowEmptyListMessage)
            return
        }

        _events.value = Event(FavoriteListNavigation.ShowCharacterList(list))
    }
}






