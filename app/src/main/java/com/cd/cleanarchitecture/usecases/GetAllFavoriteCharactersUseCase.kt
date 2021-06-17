package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.data.CharacterRepository
import com.cd.cleanarchitecture.database.CharacterDao
import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.database.toCharacterDomain
import com.cd.cleanarchitecture.database.toCharacterDomainList
import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke() : Flowable<List<Character>> = characterRepository.getAllFavoriteCharacter()
}