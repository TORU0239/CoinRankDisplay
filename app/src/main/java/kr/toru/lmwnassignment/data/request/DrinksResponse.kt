package kr.toru.lmwnassignment.data.request

import kotlinx.serialization.Serializable

@Serializable
data class DrinksResponse(
    val drinks: List<Drink>
)