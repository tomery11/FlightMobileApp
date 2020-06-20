package com.example.flightmobileapp



import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import io.github.controlwear.virtual.joystick.android.JoystickView
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Dashboard : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {



    var rudderSeekBar: SeekBar? = null
    var throttleSeekBar: SeekBar? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //rudderSeekbar
        val seekBarRudder:SeekBar? = findViewById<SeekBar>(R.id.rudderSeekbar)
        // initiate  views
        rudderSeekBar = findViewById(R.id.rudderSeekbar) as SeekBar
        // perform seek bar change listener event used for getting the progress value
        rudderSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                progressChangedValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
        })

        //Throttle seekbar
        val seekBarThrottle:SeekBar? = findViewById<SeekBar>(R.id.throttleSeekbar)
        // initiate  views
        throttleSeekBar = findViewById(R.id.throttleSeekbar) as SeekBar
        // perform seek bar change listener event used for getting the progress value
        throttleSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                progressChangedValue = progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
        })



        //joystick
        val joystickLeft = findViewById<JoystickView>(R.id.joystickView_left) as JoystickView
        joystickLeft.setOnMoveListener { angle, strength ->

               /* String.format(
                    "x%03d:y%03d",
                    joystickLeft.normalizedX,
                    joystickLeft.normalizedY
                )*/
                print(joystickLeft.normalizedX)
                print(joystickLeft.normalizedY)
        }


        // get request for screenshot call this function every 0.5 seconds
       /* CoroutineScope(Dispatchers.IO).Launch{
            while (true){
                delay(250)
                //get screenshot
            }
        }*/


        // a thread that runs in the background and send a GET http request for the cockpit view
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(500)
                        runOnUiThread {
                            getImage()
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        thread.start()




    }


    fun getImage(){
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:5001/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.isSuccessful) {
                    //ImageView img = findViewById<>("cockpitView")
                    //setImageBitmap(body)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //senBadMessage()
                //display error
            }
        })
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }
}