package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.api.*
import com.cd.cleanarchitecture.data.CharacterRepository
import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetAllCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(currentPage : Int) : Single<List<Character>> = characterRepository.getAllCharacters(currentPage)

}