package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.api.CharacterRequest
import com.cd.cleanarchitecture.api.CharacterResponseServer
import com.cd.cleanarchitecture.api.CharacterService
import com.cd.cleanarchitecture.api.toCharacterServerList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetAllCharactersUseCase(
    private val characterRequest: CharacterRequest
) {

    fun invoke(currentPage : Int) = characterRequest
        .getService<CharacterService>()
        .getAllCharacters(currentPage)
        .map(CharacterResponseServer::toCharacterServerList)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

}