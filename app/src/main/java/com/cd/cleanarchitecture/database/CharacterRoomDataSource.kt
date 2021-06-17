package com.cd.cleanarchitecture.database

import com.cd.cleanarchitecture.data.LocalCharacterDataSource
import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterRoomDataSource(
    database: CharacterDatabase
) : LocalCharacterDataSource {

    private val characterDao : CharacterDao by lazy {
        database.characterDao()
    }

    override fun getAllFavoriteCharacter(): Flowable<List<Character>> {
        return characterDao
            .getAllFavoriteCharacters()
            .map(List<CharacterEntity>::toCharacterDomainList)
            .onErrorReturn {
                emptyList()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean> {
        return characterDao.getCharacterById(id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun updateFavoriteCharacrter(character: Character): Maybe<Boolean> {
        val characterEntity = character.toCharacterEntity()
        return characterDao.getCharacterById(characterEntity.id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                if(isEmpty){
                    characterDao.insertCharacter(characterEntity)
                }else{
                    characterDao.deleteCharacter(characterEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}