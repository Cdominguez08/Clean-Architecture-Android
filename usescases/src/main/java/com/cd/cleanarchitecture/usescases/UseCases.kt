package com.cd.cleanarchitecture.usescases

import com.cd.cleanarchitecture.data.CharacterRepository
import com.cd.cleanarchitecture.data.EpisodeRepository
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.domain.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class GetAllCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(currentPage : Int) : Single<List<Character>> = characterRepository.getAllCharacters(currentPage)

}

class GetAllFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke() : Flowable<List<Character>> = characterRepository.getAllFavoriteCharacter()
}

class GetEpisodeFromCharacterUseCase(
    private val episodeRepository: EpisodeRepository
) {

    fun invoke(episodeUrlList : List<String>) : Single<List<Episode>> = episodeRepository.getAllEpisodeFromCharacter(episodeUrlList)

}

class GetFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(characterId : Int) : Maybe<Boolean> {
        return characterRepository.getFavoriteCharacterStatus(characterId)
    }
}

class UpdateFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(character: Character) : Maybe<Boolean>{
        return characterRepository.updateFavoriteCharacrter(character)
    }
}