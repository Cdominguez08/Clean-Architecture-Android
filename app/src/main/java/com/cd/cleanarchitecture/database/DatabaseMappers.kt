package com.cd.cleanarchitecture.database

import com.cd.cleanarchitecture.api.CharacterServer
import com.cd.cleanarchitecture.api.LocationServer
import com.cd.cleanarchitecture.api.OriginServer

fun CharacterEntity.toCharacterServer() = CharacterServer(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginServer(),
    location.toLocationServer(),
    episodeList
)

fun OriginEntity.toOriginServer() = OriginServer(
    originName,
    originUrl
)

fun LocationEntity.toLocationServer() = LocationServer(
    locationName,
    locationUrl
)
