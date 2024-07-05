package kr.toru.lmwnassignment.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.R
import kr.toru.lmwnassignment.data.repository.RemoteRepository
import kr.toru.lmwnassignment.network.ApiService
import kr.toru.lmwnassignment.network.httpClientAndroid

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        test()
    }

    private fun test() {
        CoroutineScope(Dispatchers.IO).launch {
            val remoteRepository = RemoteRepository(
                apiService = ApiService(
                    httpClient = httpClientAndroid
                )
            )

            val drinks = remoteRepository.getDrinks()
            drinks.onSuccess {
                it.forEach { drink ->
                    println("Drink Name: ${drink.strDrink}")
                }
            }
        }
    }
}