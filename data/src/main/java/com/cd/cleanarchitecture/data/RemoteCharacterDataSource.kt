package com.cd.cleanarchitecture.data

import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Single

interface RemoteCharacterDataSource {
    fun getAllCharacters(page : Int) : Single<List<Character>>
}