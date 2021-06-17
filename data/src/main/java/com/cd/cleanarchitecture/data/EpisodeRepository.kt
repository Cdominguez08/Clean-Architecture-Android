package com.cd.cleanarchitecture.data

class EpisodeRepository(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource
) {

    fun getAllEpisodeFromCharacter(episodeList : List<String>) = remoteEpisodeDataSource.getAllEpisodesFromCharacter(episodeList)
}