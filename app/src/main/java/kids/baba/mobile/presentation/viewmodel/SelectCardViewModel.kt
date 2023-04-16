package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.domain.usecase.PostBabyAlbumUseCase
import kids.baba.mobile.presentation.extension.FileUtil
import kids.baba.mobile.presentation.model.CardStyleUiModel
import kids.baba.mobile.presentation.state.PostAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postBabyAlbumUseCase: PostBabyAlbumUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val TAG = "SelectCardViewModel"

    val currentTakenMedia = MutableStateFlow(savedStateHandle[MEDIA_DATA] ?: MediaData())

    private var _cardState: MutableStateFlow<Array<CardStyleUiModel>> = MutableStateFlow(CardStyleUiModel.values())
    val cardState = _cardState.asStateFlow()

    private var _cardPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val cardPosition = _cardPosition.asStateFlow()

    private val _postAlbumState = MutableStateFlow<PostAlbumState>(PostAlbumState.UnInitialized)
    val postAlbumState = _postAlbumState.asStateFlow()

    val nowDate: MutableStateFlow<String> = MutableStateFlow("")

    private val requestHashMap = hashMapOf<String, RequestBody>()

    // TODO: "memberId":"KAKAO2695099524","name":"poi" 인 계정에서의 아이로 테스트했음
    //  이 계정의 babyId 는 아래임. 추후에 SharedPreference 에
    //  사용자가 보고있는 babyId 을 저장하고 이를 불러올 것임.
    private val babyId = "fa0319b3-ceb3-41dd-a297-1ac318f5bb09"

    init {
        getCards()
    }

    fun onCardSelected(position: Int) = viewModelScope.launch {
        Log.d(TAG, "selected position: $position")
        _cardPosition.value = position
    }

    private fun getCards() {
        val cardUiModelArray = defaultCardUiModelArray
        _cardState.value = cardUiModelArray
    }

    fun postAlbumClick() {
        viewModelScope.launch {
            postAlbum()
        }
    }

    private suspend fun postAlbum() {

        val file = File(FileUtil.optimizeBitmap(context, currentTakenMedia.value.mediaUri.toUri()))

        val requestPhotoFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photoFile: MultipartBody.Part = MultipartBody.Part.createFormData("photo", "photo", requestPhotoFile)

        requestHashMap["date"] = nowDate.value.toPlainRequestBody()
        requestHashMap["title"] = currentTakenMedia.value.mediaName.toPlainRequestBody()
        requestHashMap["cardStyle"] = defaultCardUiModelArray[cardPosition.value].name.toPlainRequestBody()

        postBabyAlbumUseCase.postAlbum(babyId, photoFile, requestHashMap).catch {
            Log.d(TAG, " file Size After compress : ${file.length()}")
            _postAlbumState.value = PostAlbumState.Error(it)
        }.collect {
            Log.d(TAG, " file Size After compress : ${file.length()}")
            _postAlbumState.value = PostAlbumState.Success
        }
    }

    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())


    companion object {
        const val MEDIA_DATA = "mediaData"
        private val defaultCardUiModelArray = CardStyleUiModel.values()
    }


}

