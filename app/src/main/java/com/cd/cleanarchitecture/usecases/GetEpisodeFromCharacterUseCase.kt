package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.api.EpisodeRequest
import com.cd.cleanarchitecture.api.EpisodeServer
import com.cd.cleanarchitecture.api.EpisodeService
import com.cd.cleanarchitecture.api.toEpisodeDomainList
import com.cd.cleanarchitecture.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEpisodeFromCharacterUseCase(
    private val episodeRequest: EpisodeRequest
) {

    fun invoke(episodeUrlList : List<String>) : Single<List<Episode>> = Observable.fromIterable(episodeUrlList)
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