package kr.toru.lmwnassignment.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ApiService(private val httpClient: HttpClient) {
    companion object {
        private const val END_POINT =
            "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink"
    }

    suspend fun getDrinks() = httpClient.get(END_POINT)
}