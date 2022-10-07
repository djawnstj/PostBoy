package com.junseo.postboy.util.web

import android.util.Log
import com.junseo.postboy.repository.BaseRepository
import com.junseo.postboy.util.web.interceptor.AddParamsInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object PostBoy {
    private const val TAG = "PostDogClient"

    private var instance : BaseRepository? = null

    @JvmStatic
    val client: BaseRepository get() { return getInstance() }

    lateinit var dispatcher: Dispatcher

    @Synchronized
    fun getInstance(): BaseRepository {
        if (instance == null) instance = create()
        return instance as BaseRepository
    }

    /**
     * 인스턴스를 새로 생성
     */
    @Synchronized
    @JvmStatic
    fun resetInstance() { instance = create() }
    @Synchronized
    @JvmStatic
    fun resetInstance(baseUrl: String) { instance = create(baseUrl) }

    private fun create(): BaseRepository {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-Client-Id", "djawnstj")
                .addHeader("X-Client-Secret", "djawnstj")
                .addHeader("X-Client-UserId", "djawnstj")
                .removeHeader("User-Agent")
                .build()
            return@Interceptor it.proceed(request)
        }


        val clientBuilder = OkHttpClient.Builder()

        //========== SSL support START ==========//
        if (WebConfig.PROTOCOL.lowercase() == "https") {


            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            })

            // X509TrustManager
            val x509TrustManager: X509TrustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            }

            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustAllCerts, SecureRandom())
                val sslSocketFactory = sslContext.socketFactory
                clientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "msg is null", e)
            }

            clientBuilder.hostnameVerifier(RelaxedHostNameVerifier())

        }
        //========== SSL support END ==========//

        /** 모든 요청에 파라미터를 고정시키는 인터셉터 */
        val addParamsInterceptor = AddParamsInterceptor.Builder()
            .build()

        /** 응답이 왔을때 코드를 체크해서 특정 이벤트를 호출하는 인터셉터 */
        val responseInterceptor = Interceptor {
            val request = it.request()

            val response = it.proceed(request)
            /* TODO
                필요한 경우 인터셉터 ex) check (response.code != 404) throw { IllegalStateException("404 ERROR") }
            */

            response
        }

        /** 매 요청, 응답시에 호출되는 이벤트리스너 */
        val httpEventListener = object: EventListener() {
                override fun callStart(call: Call) {
                    super.callStart(call)
                }

                override fun callEnd(call: Call) {
                    super.callEnd(call)
                }
            }

        clientBuilder.addInterceptor(headerInterceptor)
        clientBuilder.addInterceptor(httpLoggingInterceptor)
        clientBuilder.addInterceptor(addParamsInterceptor)
        clientBuilder.addInterceptor(responseInterceptor)
        clientBuilder.connectTimeout(180, TimeUnit.SECONDS)  // 타임아웃 시간 설정 180초
        clientBuilder.eventListener(httpEventListener)
        val client = clientBuilder.build()

        dispatcher = client.dispatcher

        return Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(BaseRepository::class.java)
    }

    fun create(baseUrl: String): BaseRepository {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-Client-Id", "djawnstj")
                .addHeader("X-Client-Secret", "djawnstj")
                .addHeader("X-Client-UserId", "djawnstj")
                .removeHeader("User-Agent")
                .build()
            return@Interceptor it.proceed(request)
        }


        val clientBuilder = OkHttpClient.Builder()

        //========== SSL support START ==========//
        if (WebConfig.PROTOCOL.lowercase() == "https") {


            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            })

            // X509TrustManager
            val x509TrustManager: X509TrustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            }

            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustAllCerts, SecureRandom())
                val sslSocketFactory = sslContext.socketFactory
                clientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "msg is null", e)
            }

            clientBuilder.hostnameVerifier(RelaxedHostNameVerifier())

        }
        //========== SSL support END ==========//

        /** 모든 요청에 파라미터를 고정시키는 인터셉터 */
        val addParamsInterceptor = AddParamsInterceptor.Builder()
            .build()

        /** 응답이 왔을때 코드를 체크해서 특정 이벤트를 호출하는 인터셉터 */
        val responseInterceptor = Interceptor {
            val request = it.request()

            val response = it.proceed(request)
            /* TODO
                필요한 경우 인터셉터 ex) check (response.code != 404) throw { IllegalStateException("404 ERROR") }
            */

            response
        }

        /** 매 요청, 응답시에 호출되는 이벤트리스너 */
        val httpEventListener = object: EventListener() {
                override fun callStart(call: Call) {
                    super.callStart(call)
                }

                override fun callEnd(call: Call) {
                    super.callEnd(call)
                }
            }

        clientBuilder.addInterceptor(headerInterceptor)
        clientBuilder.addInterceptor(httpLoggingInterceptor)
        clientBuilder.addInterceptor(addParamsInterceptor)
        clientBuilder.addInterceptor(responseInterceptor)
        clientBuilder.connectTimeout(180, TimeUnit.SECONDS)  // 타임아웃 시간 설정 180초
        clientBuilder.eventListener(httpEventListener)
        val client = clientBuilder.build()

        dispatcher = client.dispatcher

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(BaseRepository::class.java)
    }

    class RelaxedHostNameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

}