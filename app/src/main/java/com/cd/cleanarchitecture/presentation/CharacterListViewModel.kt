package com.cd.cleanarchitecture.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cd.cleanarchitecture.api.*
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.usescases.GetAllCharactersUseCase
import io.reactivex.disposables.CompositeDisposable

class CharacterListViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : ViewModel() {

    //Live data de los estados
    private val _events = MutableLiveData<Event<CharacterListNavigation>>()
    val events : LiveData<Event<CharacterListNavigation>> get() = _events

    private val disposable = CompositeDisposable()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    //region Private Methods

    fun onLoadMoreItems(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (isLoading || isLastPage || !isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }

        currentPage += 1
        onGetAllCharacters()
    }

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
    }

    fun onRetryGetAllCharacter(itemCount: Int) {
        if (itemCount > 0) {
            _events.value = Event(CharacterListNavigation.HideLoading)
            return
        }

        onGetAllCharacters()
    }

    fun onGetAllCharacters(){
        disposable.add(
            getAllCharactersUseCase
                .invoke(currentPage)
                .doOnSubscribe {
                    _events.value = Event(CharacterListNavigation.ShowLoading)
                }
                .subscribe({ characterList ->
                    if (characterList.size < PAGE_SIZE) {
                        isLastPage = true
                    }

                    _events.value = Event(CharacterListNavigation.HideLoading)
                    _events.value = Event(CharacterListNavigation.ShowCharacterList(characterList))
                }, { error ->
                    isLastPage = true
                    _events.value = Event(CharacterListNavigation.HideLoading)
                    _events.value = Event(CharacterListNavigation.ShowCharacterError(error))
                })
        )
    }

    //endregion

    companion object{
        private const val PAGE_SIZE = 20
    }

    sealed class CharacterListNavigation{
        data class ShowCharacterError(val error : Throwable) : CharacterListNavigation()
        data class ShowCharacterList(val characterList : List<Character>) : CharacterListNavigation()
        object HideLoading : CharacterListNavigation()
        object ShowLoading : CharacterListNavigation()
    }
}







