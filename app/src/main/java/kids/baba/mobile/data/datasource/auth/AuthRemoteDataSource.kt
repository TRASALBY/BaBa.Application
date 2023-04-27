package kids.baba.mobile.data.datasource.auth

import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    suspend fun login(socialToken: String): Result<TokenResponse>

    fun getTerms(socialToken: String): Flow<List<TermsData>>

    fun getSignToken(signTokenRequest: SignTokenRequest): Flow<String>
}
