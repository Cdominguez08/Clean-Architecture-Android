package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.data.CharacterRepository
import com.cd.cleanarchitecture.database.CharacterDao
import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.database.toCharacterEntity
import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(character: Character) : Maybe<Boolean>{
        return characterRepository.updateFavoriteCharacrter(character)
    }
}