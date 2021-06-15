package com.cd.cleanarchitecture.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cd.cleanarchitecture.api.*
import com.cd.cleanarchitecture.database.CharacterDao
import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.usecases.GetEpisodeFromCharacterUseCase
import com.cd.cleanarchitecture.usecases.GetFavoriteCharacterStatusUseCase
import com.cd.cleanarchitecture.usecases.UpdateFavoriteCharacterStatusUseCase
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterDetailViewModel(
    private val character: CharacterServer? = null,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
    private val getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase,
    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase
) : ViewModel() {

    sealed class CharacterDetailNavigation {
        data class ShowEpisodeServerList(val episodeList : List<EpisodeServer>) : CharacterDetailNavigation()
        data class ShowCharacterDetailError(val error : Throwable) : CharacterDetailNavigation()
        object HideLoading : CharacterDetailNavigation()
        object ShowLoading : CharacterDetailNavigation()
        object CloseActivity : CharacterDetailNavigation()
    }

    private val disposable = CompositeDisposable()

    private val _characterValues = MutableLiveData<CharacterServer>()
    val characterValues : LiveData<CharacterServer> get() = _characterValues

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean> get() = _isFavorite

    //LiveDatas para manejar eventos
    private val _events = MutableLiveData<Event<CharacterDetailNavigation>>()
    val events : LiveData<Event<CharacterDetailNavigation>> get() = _events

    fun onCharacterValidation(){

        if(character == null){
            _events.value = Event(CharacterDetailNavigation.CloseActivity)
            return
        }

        _characterValues.value = character
        validateFavoriteCharacterStatus(character.id)
        requestShowEpisodeList(character.episodeList)
    }

    fun requestShowEpisodeList(episodeUrlList: List<String>){
        disposable.add(
            getEpisodeFromCharacterUseCase
                .invoke(episodeUrlList)
                .doOnSubscribe {
                    _events.value = Event(CharacterDetailNavigation.ShowLoading)
                }
                .subscribe(
                    { episodeList ->
                        _events.value = Event(CharacterDetailNavigation.HideLoading)
                        _events.value = Event(CharacterDetailNavigation.ShowEpisodeServerList(episodeList))
                    },
                    { error ->
                        _events.value = Event(CharacterDetailNavigation.HideLoading)
                        _events.value = Event(CharacterDetailNavigation.ShowCharacterDetailError(error))
                    })
        )
    }

    fun onUpdateFavoriteCharacterStatus() {
        val characterEntity : CharacterEntity = character!!.toCharacterEntity()
        disposable.add(
            updateFavoriteCharacterStatusUseCase
                .invoke(characterEntity)
                .subscribe { isFavorite ->
                    _isFavorite.value = isFavorite
                }
        )
    }

    fun validateFavoriteCharacterStatus(id : Int){
        disposable.add(
            getFavoriteCharacterStatusUseCase
                .invoke(id)
                .subscribe { isFavorite ->
                    _isFavorite.value = isFavorite
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}