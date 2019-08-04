# ConnectionView

### connected
![alt text](https://github.com/DjamshidDjurayev/ConnectionView/blob/master/connected.jpeg)

### disconnected
![alt text](https://github.com/DjamshidDjurayev/ConnectionView/blob/master/disconnected.jpeg)

```kotlin
val connectionView = ConnectionView(this).apply {
            attachActivity(this@MainActivity)
            setIsAnimated(true)
            setConnectedText(getString(R.string.connected))
            setDisconnectedText(getString(R.string.disconnected))
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
            makeTransparent()
        }

        Handler().postDelayed(Runnable {
            connectionView.setIsConnected(true)
            connectionView.show(true, 4000)
        }, 2000)

        Handler().postDelayed(Runnable {
            connectionView.setIsConnected(false)
            connectionView.show(true, 4000)
        }, 8000)
```
