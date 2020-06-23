package com.example.flightmobileapp



import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.cos
import kotlin.math.sin


class Dashboard : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {



    var rudderSeekBar: SeekBar? = null
    var throttleSeekBar: SeekBar? = null

    var commandToSend: Command? = null

    //var img: ImageView? = null
    var currAileron: Double? = null
    var currElevator: Double? = null
    var currRudder: Int? = null
    var currThrottle: Int? = null

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
                println(progress.toDouble() / 1000)
                if(currRudder != progress){
                    currRudder = progress
                    val rudderCommand = progress.toDouble() / 100
                    val commandToSend = Command(aileron = 0.0, throttle = 0.0, rudder = rudderCommand, elevator = 0.0 )

                    postCommand(commandToSend)
                }


                //if seekbar value changed then change send the command otherwise don't send
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
                if(currThrottle != progressChangedValue){
                    currThrottle = progress
                    val throttleCommand = progress.toDouble() / 100
                    println(throttleCommand)
                    val commandToSend = Command(aileron = 0.0, throttle = throttleCommand, rudder = 0.0, elevator = 0.0 )
                    postCommand(commandToSend)
                }


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


            val aileronCommand = cos(angle.toDouble()) * strength
            val elevatorCommand = sin(angle.toDouble()) * strength

            val commandToSend = Command(aileron = aileronCommand, throttle = 0.0, rudder = 0.0, elevator = elevatorCommand)
            postCommand(commandToSend)

        }





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

        //orientation
        fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)

            // Checks the orientation of the screen
            if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
                //retrieving resources for the variations
                setContentView(R.layout.activity_dashboard)
            } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
                //retrieving resources for the variations
                setContentView(R.layout.activity_dashboard)
            }
        }


    }

    fun postCommand(command: Command){
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:5001/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.postCommand(command).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println(response.body())

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //display failure
            }
        })
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
                    //img = findViewById(R.id.cockpitView) as ImageView
                    val I = response?.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread{
                        cockpitView.setImageBitmap(B)
                    }
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