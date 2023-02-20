package kids.baba.mobile.data.datasource.auth

import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val api: AuthApi) :
    AuthRemoteDataSource {

    override suspend fun login(socialToken: String) = flow {
        val resp = api.login(LoginRequest(socialToken))

        when (resp.code()) {
            200 -> {
                val data = resp.body() ?: throw Throwable("data is null")
                emit(data)
            }
            404 -> {
                val jsonData = JSONObject(resp.errorBody().toString())
                val tokenResponse = TokenResponse(
                    accessToken = jsonData.getString("accessToken"),
                    refreshToken = jsonData.getString("refreshToken")
                )

                throw UserNotFoundException(
                    tokenResponse = tokenResponse,
                    message = "신규 로그인"
                )
            }
            else -> throw Throwable("로그인 에러")
        }
    }
}
