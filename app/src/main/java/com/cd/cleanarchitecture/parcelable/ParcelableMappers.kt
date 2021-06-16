package com.cd.cleanarchitecture.parcelable

import android.os.Parcelable
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.domain.Location
import com.cd.cleanarchitecture.domain.Origin
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterParcelable(
    val id : Int,
    val name : String,
    val image : String?,
    val gender : String,
    val species : String,
    val status: String,
    val origin: OriginParcelable,
    val location: LocationParcelable,
    val episodeList : List<String>
) : Parcelable

@Parcelize
data class OriginParcelable(
    val name : String,
    val url  : String
) : Parcelable

@Parcelize
data class LocationParcelable(
    val name : String,
    val url  : String
) : Parcelable

fun Character.toCharacterParcelable() = CharacterParcelable(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginParcelable(),
    location.toLocationParcelable(),
    episodeList
)

fun Origin.toOriginParcelable() = OriginParcelable(
    name,
    url
)


fun Location.toLocationParcelable() = LocationParcelable(
    name,
    url
)

fun CharacterParcelable.toCharacterDomain() = Character(
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

fun OriginParcelable.toOriginDomain() = Origin(
    name,
    url
)


fun LocationParcelable.toLocationDomain() = Location(
    name,
    url
)