package com.cd.cleanarchitecture.usecases

import com.cd.cleanarchitecture.api.EpisodeRequest
import com.cd.cleanarchitecture.api.EpisodeServer
import com.cd.cleanarchitecture.api.EpisodeService
import com.cd.cleanarchitecture.api.toEpisodeDomainList
import com.cd.cleanarchitecture.data.EpisodeRepository
import com.cd.cleanarchitecture.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEpisodeFromCharacterUseCase(
    private val episodeRepository: EpisodeRepository
) {

    fun invoke(episodeUrlList : List<String>) : Single<List<Episode>> = episodeRepository.getAllEpisodeFromCharacter(episodeUrlList)

}