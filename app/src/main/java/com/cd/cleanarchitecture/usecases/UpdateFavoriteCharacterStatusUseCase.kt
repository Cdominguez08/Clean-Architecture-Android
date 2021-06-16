package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.database.CharacterDao
import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.database.toCharacterEntity
import com.cd.cleanarchitecture.domain.Character
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateFavoriteCharacterStatusUseCase(
    private val characterDao: CharacterDao
) {

    fun invoke(character: Character) : Maybe<Boolean>{

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