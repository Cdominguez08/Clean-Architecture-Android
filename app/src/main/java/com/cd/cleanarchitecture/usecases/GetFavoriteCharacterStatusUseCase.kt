package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.data.CharacterRepository
import com.cd.cleanarchitecture.database.CharacterDao
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(characterId : Int) : Maybe<Boolean>{
        return characterRepository.getFavoriteCharacterStatus(characterId)
    }
}