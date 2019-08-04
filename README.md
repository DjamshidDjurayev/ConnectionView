# ConnectionView

```kotlin
ConnectionView(this).apply {
        attachActivity(this@WelcomeScreenActivity)
        setIsAnimated(true)
        setConnectedText(getString(R.string.connected))
        setDisconnectedText(getString(R.string.disconnected))
        setTextColor(ContextCompat.getColor(this@WelcomeScreenActivity, R.color.white))
      }
```
