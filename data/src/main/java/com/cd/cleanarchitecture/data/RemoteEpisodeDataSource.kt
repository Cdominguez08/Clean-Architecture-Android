package com.cd.cleanarchitecture.data

import com.cd.cleanarchitecture.domain.Episode
import io.reactivex.Single

interface RemoteEpisodeDataSource {

    fun getAllEpisodesFromCharacter(episodeUrlList : List<String>) : Single<List<Episode>>
}