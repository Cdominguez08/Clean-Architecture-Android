package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.database.CharacterDao
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharactersUseCase(
    private val characterDao: CharacterDao
) {

    fun invoke() = characterDao
        .getAllFavoriteCharacters()
        .onErrorReturn {
            emptyList()
        }
        .subscribeOn(Schedulers.io())
}