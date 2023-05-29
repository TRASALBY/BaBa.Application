package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun getMe(accessToken: String): Result<MemberModel>

    suspend fun signUpWithBabiesInfo(signToken: String, signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo): Result<TokenResponse>

    fun signUpWithInviteCode(signToken: String, signUpRequestWithInviteCode: SignUpRequestWithInviteCode): Flow<TokenResponse>
}
