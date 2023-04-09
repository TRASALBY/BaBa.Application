package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class PostOneArticleUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend fun post(id: String) = repository.addArticle(id)
}