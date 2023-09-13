package com.pascal.rawgapps.data.local.model

import com.pascal.rawgapps.domain.model.UIModelDetails
import com.pascal.rawgapps.domain.model.UIModelListing

fun UIModelDetails.toDBModelDetails(): DBModelDetails {
    return DBModelDetails(
        this.id,
        this.name,
        this.description,
        this.metacritic,
        this.releaseDate,
        this.websiteURL,
        this.redditURL,
        this.genres,
        this.developers,
        this.backgroundImage
    )
}

fun DBModelDetails.toUIModelDetails(): UIModelDetails {
    return UIModelDetails(
        this.id,
        this.name,
        this.description,
        this.metacritic,
        this.releaseDate,
        this.websiteURL,
        this.redditURL,
        ArrayList(this.genres),
        ArrayList(this.developers),
        this.backgroundImage
    )
}

fun DBModelDetails.toUIModelListing(): UIModelListing {
    return UIModelListing(
        this.id,
        this.name,
        this.backgroundImage
    )
}

