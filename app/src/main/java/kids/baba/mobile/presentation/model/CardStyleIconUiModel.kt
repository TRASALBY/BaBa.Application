package kids.baba.mobile.presentation.model

import androidx.annotation.DrawableRes
import kids.baba.mobile.R

enum class CardStyleIconUiModel(
    @DrawableRes
    val iconRes: Int
) {
    CARD_BASIC_1(R.drawable.card_basic_1),
    CARD_SKY_1(R.drawable.card_sky_1),
    CARD_CLOUD_1(R.drawable.card_cloud_1),
    CARD_CLOUD_2(R.drawable.card_cloud_2),
    CARD_TOY_1(R.drawable.card_toy_1),
    CARD_CANDY_1(R.drawable.card_candy_1),
    CARD_SNOWFLOWER_1(R.drawable.card_snowflower_1),
    CARD_SNOWFLOWER_2(R.drawable.card_snowflower_2),
    CARD_LINE_1(R.drawable.card_line_1),
    CARD_SPRING_1(R.drawable.card_spring_1),
    CARD_CHECK_1(R.drawable.card_check_1),
    CARD_CHECK_2(R.drawable.card_check_2)
}