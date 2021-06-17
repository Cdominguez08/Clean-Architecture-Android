package com.cd.cleanarchitecture.data

import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface LocalCharacterDataSource {

    fun getAllFavoriteCharacter() : Flowable<List<Character>>
    fun getFavoriteCharacterStatus(id : Int) : Maybe<Boolean>
    fun updateFavoriteCharacrter(character: Character) : Maybe<Boolean>
}