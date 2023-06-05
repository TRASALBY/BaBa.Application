package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.MemberUiModel

sealed class MyPageEvent {

    data class LoadGroups(val data: List<Group>) : MyPageEvent()

    data class LoadBabies(val data: List<BabyUiModel>) : MyPageEvent()

    data class LoadMyInfo(val data: MemberUiModel) : MyPageEvent()

    data class ShowSnackBar(@StringRes val message: Int) : MyPageEvent()

    object AddGroup : MyPageEvent()

    object Setting : MyPageEvent()
}
