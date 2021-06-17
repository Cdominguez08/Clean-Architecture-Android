package com.cd.cleanarchitecture.api

import com.cd.cleanarchitecture.data.RemoteCharacterDataSource
import com.cd.cleanarchitecture.data.RemoteEpisodeDataSource
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//implementacion de la interfaz de datos y el detalle de la implementacion

class CharacterRetrofitDataSource(
    private val characterRequest: CharacterRequest
) : RemoteCharacterDataSource {


    override fun getAllCharacters(page: Int): Single<List<Character>> {
        return characterRequest
            .getService<CharacterService>()
            .getAllCharacters(page)
            .map(CharacterResponseServer::toCharacterDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}

class EpisodeRetrofitDataSource(
    private val episodeRequest: EpisodeRequest
) : RemoteEpisodeDataSource{

    override fun getAllEpisodesFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> {
        return Observable.fromIterable(episodeUrlList)
            .flatMap { episode: String ->
                episodeRequest.baseUrl = episode
                episodeRequest
                    .getService<EpisodeService>()
                    .getEpisode()
                    .toObservable()
            }
            .toList()
            .map(List<EpisodeServer>::toEpisodeDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}
