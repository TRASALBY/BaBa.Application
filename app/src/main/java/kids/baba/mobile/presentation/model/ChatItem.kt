package kids.baba.mobile.presentation.model

sealed class ChatItem {
    data class BabaFirstChatItem(
        val message: String,
    ) : ChatItem()

    data class BabaChatItem(
        val message: String,
    ) : ChatItem()

    data class UserChatItem(
        val message: String,
        val canModify: Boolean,
        var isModifying: Boolean
    ) : ChatItem()
}