package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.state.MyPageEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageGroupUseCase: GetMyPageGroupUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {
    val groupAddButton = MutableStateFlow("+ 그룹만들기")


    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    // TODO: 편집이 완료되었을 때 MyPageFragment View 에 갱신해야 함.
    private val _babyGroupTitle = MutableStateFlow(EncryptedPrefs.getString("babyGroupTitle"))
    val babyGroupTitle = _babyGroupTitle.asStateFlow()

    fun loadGroups() = viewModelScope.launch {
        getMyPageGroupUseCase.get().catch {

        }.collect {
            _eventFlow.emit(MyPageEvent.LoadMember(it.groups))
        }
    }

    fun loadBabies() = viewModelScope.launch {

        when (val result = getBabiesUseCase()) {
            is Result.Success -> {
                val babies = result.data
                _eventFlow.emit(MyPageEvent.LoadBabies(babies.myBaby + babies.others))
            }
            is Result.NetworkError -> {
                _eventFlow.emit(MyPageEvent.Error(result.throwable))
            }
            else -> {
                _eventFlow.emit(MyPageEvent.Error(Throwable("error")))
            }
        }
    }

    fun getMyInfo() = viewModelScope.launch {
        getMemberUseCase.getMeNoPref().map { it.toPresentation() }.collect {
            _eventFlow.emit(MyPageEvent.LoadMyInfo(it))
        }
    }

    fun addGroup() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.AddGroup)
    }

    fun onClickSetting() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.Setting)
    }
}