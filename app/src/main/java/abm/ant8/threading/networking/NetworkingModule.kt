package abm.ant8.threading.networking

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object NetworkingModule {
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .build()

    @Provides
    fun provideNetworkingApi(retrofit: Retrofit): NetworkingApi =
        retrofit.create(NetworkingApi::class.java)

    @Provides
    fun provideNetworkingRepository(api: NetworkingApi): NetworkingRepository =
        NetworkingRepositoryImpl(api)
}