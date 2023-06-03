package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableNameViewData(
    val text: MutableStateFlow<String>,
    val onEditButtonClickEventListener: () -> Unit
)