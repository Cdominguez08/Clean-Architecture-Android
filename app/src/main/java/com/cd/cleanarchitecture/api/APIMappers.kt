package com.cd.cleanarchitecture.api

import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.database.LocationEntity
import com.cd.cleanarchitecture.database.OriginEntity
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.domain.Episode
import com.cd.cleanarchitecture.domain.Location
import com.cd.cleanarchitecture.domain.Origin

fun List<EpisodeServer>.toEpisodeDomainList() = map(EpisodeServer::toEpisodeDomain)

fun CharacterResponseServer.toCharacterDomainList(): List<Character> = results.map {
    it.run{
        Character(
            id,
            name,
            image,
            gender,
            species,
            status,
            origin.toOriginDomain(),
            location.toLocationDomain(),
            episodeList.map { episode -> "$episode/" }
        )
    }
}

fun OriginServer.toOriginDomain() = Origin(
    name,
    url
)

fun LocationServer.toLocationDomain() = Location(
    name,
    url
)

fun EpisodeServer.toEpisodeDomain() = Episode(
    id,
    name
)