package ITI.covid19_tracker.view.instractions


import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.R
import ITI.covid19_tracker.db.getImageInstractions.InstractionsInterface
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Instractions : AppCompatActivity() {

    lateinit var context: Context
    lateinit var img: ImageView
    lateinit var next: ImageButton
    val checkNetworkConnection = newtwork()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instractions)

        supportActionBar!!.title = "Instractions"

        Toast.makeText(context, "Loading Data...", Toast.LENGTH_SHORT)
            .show();

        context = applicationContext
        img = findViewById(R.id.Instraction_img)
        next = findViewById(R.id.imageButtonId)

        getBitmapFrom()

        next.setOnClickListener {
            getBitmapFrom()
        }

    }


    fun checkInternetConnection(): Boolean {
        return checkNetworkConnection.hasInternetConnection(application)
    }


    @SuppressLint("CheckResult")
    fun getBitmapFrom() {

        if (checkInternetConnection()) {

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response? {
                    val original: Request = chain.request()
                    val request: Request = original.newBuilder()
                        .header(
                            "x-rapidapi-key",
                            "a3e81e7013mshcdca914a7a0f3f5p1173edjsn44fd70a93af2"
                        )
                        .header("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com")
                        .method(original.method(), original.body())
                        .build()
                    return chain.proceed(request)
                }
            })

            val client = httpClient.build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://coronavirus-monitor.p.rapidapi.com/coronavirus/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            val D: InstractionsInterface = retrofit.create(InstractionsInterface::class.java)
            var call = D.getImage()
            Log.i("tag", "test1")
            call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })

        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG)
                .show();
        }


    }

    private fun onFailure(t: Throwable?) {
        Log.i("tag", "Activity Failure $t")
        println("Activity Failure.")
    }

    private fun onResponse(response: ResponseBody?) {
        Log.i("tag", "test2 Instractions")

        val Response = response!!
        val bmp = BitmapFactory.decodeStream(Response.byteStream())
        img.setImageBitmap(bmp)
    }
}


