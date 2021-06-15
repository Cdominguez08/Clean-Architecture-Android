package com.cd.cleanarchitecture.api

import com.cd.cleanarchitecture.database.CharacterEntity
import com.cd.cleanarchitecture.database.LocationEntity
import com.cd.cleanarchitecture.database.OriginEntity

fun CharacterResponseServer.toCharacterServerList(): List<CharacterServer> = results.map {
    it.run{
        CharacterServer(
            id,
            name,
            image,
            gender,
            species,
            status,
            origin,
            location,
            episodeList.map { episode -> "$episode/" }
        )
    }
}

fun CharacterServer.toCharacterEntity() = CharacterEntity(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginEntity(),
    location.toLocationEntity(),
    episodeList
)

fun OriginServer.toOriginEntity() = OriginEntity(
    name,
    url
)

fun LocationServer.toLocationEntity() = LocationEntity(
    name,
    url
)