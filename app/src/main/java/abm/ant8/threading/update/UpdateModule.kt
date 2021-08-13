package abm.ant8.threading.update

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object UpdateModule {
    @Provides
    fun provideUpdateRepo(@ApplicationContext context: Context): UpdateRepo =
        UpdateRepoImpl(context)
}