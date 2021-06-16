package com.cd.cleanarchitecture.database

import com.cd.cleanarchitecture.api.CharacterServer
import com.cd.cleanarchitecture.api.LocationServer
import com.cd.cleanarchitecture.api.OriginServer
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.domain.Location
import com.cd.cleanarchitecture.domain.Origin

//funcion para convertir la lista
fun List<CharacterEntity>.toCharacterDomainList() = map(CharacterEntity::toCharacterDomain)

//Mappers para convertir de entities a domain
fun CharacterEntity.toCharacterDomain() = Character(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginDomain(),
    location.toLocationDomain(),
    episodeList
)

fun OriginEntity.toOriginDomain() = Origin(
    originName,
    originUrl
)

fun LocationEntity.toLocationDomain() = Location(
    locationName,
    locationUrl
)


//Mappers para convertir de domain a entities
fun Character.toCharacterEntity() = CharacterEntity(
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

fun Origin.toOriginEntity() = OriginEntity(
    name,
    url
)

fun Location.toLocationEntity() = LocationEntity(
    name,
    url
)