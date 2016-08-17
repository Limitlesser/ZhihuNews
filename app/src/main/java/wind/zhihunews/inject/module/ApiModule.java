package wind.zhihunews.inject.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import wind.zhihunews.net.Api;


@Module
public class ApiModule {


    @Provides
    @Singleton
    public Api provideClientApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideLogger() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }


    @Provides
    @Singleton
    public Converter.Factory provideConverter(Gson gson) {
        return GsonConverterFactory.create(gson);
    }


    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().serializeNulls().create();
    }


    @Provides
    @Singleton
    public OkHttpClient provideClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
