package com.cd.cleanarchitecture.api

import android.os.Parcelable
import com.cd.cleanarchitecture.api.APIConstants.KEY_EPISODE
import com.cd.cleanarchitecture.api.APIConstants.KEY_GENDER
import com.cd.cleanarchitecture.api.APIConstants.KEY_ID
import com.cd.cleanarchitecture.api.APIConstants.KEY_IMAGE
import com.cd.cleanarchitecture.api.APIConstants.KEY_LOCATION
import com.cd.cleanarchitecture.api.APIConstants.KEY_NAME
import com.cd.cleanarchitecture.api.APIConstants.KEY_ORIGIN
import com.cd.cleanarchitecture.api.APIConstants.KEY_RESULTS
import com.cd.cleanarchitecture.api.APIConstants.KEY_SPECIES
import com.cd.cleanarchitecture.api.APIConstants.KEY_STATUS
import com.cd.cleanarchitecture.api.APIConstants.KEY_URL
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CharacterResponseServer(
    @SerializedName(KEY_RESULTS) val results: List<CharacterServer>
)

@Parcelize
data class CharacterServer(
    @SerializedName(KEY_ID) val id: Int,
    @SerializedName(KEY_NAME) val name: String,
    @SerializedName(KEY_IMAGE) val image: String?,
    @SerializedName(KEY_GENDER) val gender: String,
    @SerializedName(KEY_SPECIES) val species: String,
    @SerializedName(KEY_STATUS) val status: String,
    @SerializedName(KEY_ORIGIN) val origin: OriginServer,
    @SerializedName(KEY_LOCATION) val location: LocationServer,
    @SerializedName(KEY_EPISODE) val episodeList: List<String>
): Parcelable

@Parcelize
data class LocationServer(
    @SerializedName(KEY_NAME) val name: String,
    @SerializedName(KEY_URL) val url: String
): Parcelable

@Parcelize
data class OriginServer(
    @SerializedName(KEY_NAME) val name: String,
    @SerializedName(KEY_URL) val url: String
): Parcelable

data class EpisodeServer(
    @SerializedName(KEY_ID) val id: Int,
    @SerializedName(KEY_NAME) val name: String
)
