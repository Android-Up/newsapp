package daniellopes.io.newsappstarter.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import daniellopes.io.newsappstarter.data.local.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInsert(article: Article): Long

    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun getAll(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}