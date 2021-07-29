package abm.ant8.threading.battery

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object BatteryModule {
    @Provides
    fun provideBatteryRepository(@ApplicationContext context: Context): BatteryRepository =
        BatteryRepositoryImpl(context)
}