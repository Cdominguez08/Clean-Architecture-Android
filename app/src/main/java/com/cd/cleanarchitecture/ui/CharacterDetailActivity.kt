package com.cd.cleanarchitecture.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cd.cleanarchitecture.R
import com.cd.cleanarchitecture.adapters.EpisodeListAdapter
import com.cd.cleanarchitecture.api.*
import com.cd.cleanarchitecture.requestmanager.APIConstants.BASE_API_URL
import com.cd.cleanarchitecture.data.*
import com.cd.cleanarchitecture.databasemanager.CharacterDatabase
import com.cd.cleanarchitecture.databasemanager.CharacterRoomDataSource
import com.cd.cleanarchitecture.databinding.ActivityCharacterDetailBinding
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.parcelable.CharacterParcelable
import com.cd.cleanarchitecture.parcelable.toCharacterDomain
import com.cd.cleanarchitecture.presentation.CharacterDetailViewModel
import com.cd.cleanarchitecture.usescases.GetEpisodeFromCharacterUseCase
import com.cd.cleanarchitecture.usescases.GetFavoriteCharacterStatusUseCase
import com.cd.cleanarchitecture.usescases.UpdateFavoriteCharacterStatusUseCase
import com.cd.cleanarchitecture.utils.Constants
import com.cd.cleanarchitecture.framework.bindCircularImageUrl
import com.cd.cleanarchitecture.utils.getViewModel
import com.cd.cleanarchitecture.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {

    //region Fields

    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding

    private val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)
    }

    private val episodeRemoteDataSource : RemoteEpisodeDataSource by lazy {
        EpisodeRetrofitDataSource(episodeRequest)
    }

    private val episodeRepository : EpisodeRepository by lazy{
        EpisodeRepository(episodeRemoteDataSource)
    }

    private val getEpisodeFromCharacterUseCase : GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRepository)
    }

    private val characterRequest : CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val remoteCharacterDataSource : RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

    private val localCharacterDataSource : LocalCharacterDataSource by lazy {
        CharacterRoomDataSource(CharacterDatabase.getDatabase(applicationContext))
    }

    private val characterRepository : CharacterRepository by lazy {
        CharacterRepository(remoteCharacterDataSource, localCharacterDataSource)
    }

    private val getFavoriteCharacterStatusUseCase : GetFavoriteCharacterStatusUseCase by lazy{
        GetFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val updateFavoriteCharacterStatusUseCase : UpdateFavoriteCharacterStatusUseCase by lazy {
        UpdateFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val viewModel : CharacterDetailViewModel by lazy {
        getViewModel {
            CharacterDetailViewModel(
                intent.getParcelableExtra<CharacterParcelable>(Constants.EXTRA_CHARACTER)?.toCharacterDomain(),
                getEpisodeFromCharacterUseCase,
                getFavoriteCharacterStatusUseCase,
                updateFavoriteCharacterStatusUseCase
            )
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        viewModel.characterValues.observe(this, Observer(this::loadCharacter))

        viewModel.isFavorite.observe(this, Observer(this::updateFavoriteIcon))

        viewModel.events.observe(this, Observer {
            events ->
                events?.getContentIfNotHandled()?.let {

                    when(it){
                        CharacterDetailViewModel.CharacterDetailNavigation.HideLoading -> {
                            episodeProgressBar.isVisible = false
                        }
                        is CharacterDetailViewModel.CharacterDetailNavigation.ShowCharacterDetailError -> {
                            this@CharacterDetailActivity.showLongToast("Error al cargar episodios")
                        }
                        is CharacterDetailViewModel.CharacterDetailNavigation.ShowEpisodeServerList -> it.run {
                            episodeListAdapter.updateData(episodeList)
                        }
                        CharacterDetailViewModel.CharacterDetailNavigation.ShowLoading -> {
                            episodeProgressBar.isVisible = true
                        }
                        CharacterDetailViewModel.CharacterDetailNavigation.CloseActivity -> {
                            this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
                            finish()
                        }
                    }
                }
        })

        viewModel.onCharacterValidation()

        characterFavorite.setOnClickListener {
            viewModel.onUpdateFavoriteCharacterStatus()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //endregion

    private fun updateFavoriteIcon(isFavorite: Boolean?){
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

    private fun loadCharacter(character: Character){
        binding.characterImage.bindCircularImageUrl(
            url = character.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character.name
        binding.characterDataStatus = character.status
        binding.characterDataSpecies = character.species
        binding.characterDataGender = character.gender
        binding.characterDataOriginName = character.origin.name
        binding.characterDataLocationName = character.location.name
    }

    //endregion
}