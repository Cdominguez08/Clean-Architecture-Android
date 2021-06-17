package com.cd.cleanarchitecture.data

import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe

class CharacterRepository(
    private val remoteCharacterDataSource: RemoteCharacterDataSource,
    private val localCharacterDataSource: LocalCharacterDataSource
) {

    fun getAllCharacters(page : Int) = remoteCharacterDataSource.getAllCharacters(page)

    fun getAllFavoriteCharacter() : Flowable<List<Character>> = localCharacterDataSource.getAllFavoriteCharacter()

    fun getFavoriteCharacterStatus(id : Int) : Maybe<Boolean> = localCharacterDataSource.getFavoriteCharacterStatus(id)

    fun updateFavoriteCharacrter(character: Character) : Maybe<Boolean> = localCharacterDataSource.updateFavoriteCharacrter(character)

}