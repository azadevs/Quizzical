package android.azadev.quizzical.di

import android.azadev.quizzical.repository.ProdQuizRepository
import android.azadev.quizzical.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindQuizRepository(repository: ProdQuizRepository): QuizRepository
}