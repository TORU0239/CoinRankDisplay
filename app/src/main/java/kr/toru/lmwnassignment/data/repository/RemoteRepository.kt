package kr.toru.lmwnassignment.data.repository

import io.ktor.client.call.body
import kr.toru.lmwnassignment.data.request.Drink
import kr.toru.lmwnassignment.data.request.DrinksResponse
import kr.toru.lmwnassignment.network.ApiService

class RemoteRepository(
    private val apiService: ApiService
) {
    suspend fun getDrinks(): Result<List<Drink>> =
        runCatching {
            apiService.getDrinks().body<DrinksResponse>().drinks
        }
}