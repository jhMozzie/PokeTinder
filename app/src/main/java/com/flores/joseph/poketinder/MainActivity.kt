package com.flores.joseph.poketinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flores.joseph.poketinder.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private var listPokemon: List<PokemonResponse> =  emptyList()
    private val adapter by lazy {PokemonAdapter(listPokemon)}
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvTinderPokemon.adapter = adapter
        getALlPokemons()

    }

    private fun getALlPokemons(){
        CoroutineScope(Dispatchers.IO).launch {
            val request = getRetrofit().create(PokemonApi::class.java).getPokemon()
            if(request.isSuccessful){
                request.body()?.let {
                    runOnUiThread {
                        adapter.List = it.results
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())

            .build()
    }
}