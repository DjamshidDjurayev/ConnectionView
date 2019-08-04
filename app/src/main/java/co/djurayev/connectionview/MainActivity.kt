package co.djurayev.connectionview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import co.djurayev.connectionview.connection.ConnectionView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectionView = ConnectionView(this).apply {
            attachActivity(this@MainActivity)
            setIsAnimated(true)
            setConnectedText(getString(R.string.connected))
            setDisconnectedText(getString(R.string.disconnected))
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
        }

        Handler().postDelayed(Runnable {
            connectionView.setIsConnected(true)
            connectionView.show(true, 4000)
        }, 2000)

        Handler().postDelayed(Runnable {
            connectionView.setIsConnected(false)
            connectionView.show(true, 4000)
        }, 8000)
    }
}
