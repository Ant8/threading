package abm.ant8.threading.location

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class LocationModule {
    @Provides fun bindLocationRepository(@ActivityContext context: Context): LocationRepository =
        LocationRepositoryImpl(context)
}