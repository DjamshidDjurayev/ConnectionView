# ConnectionView

```kotlin
connectionView = ConnectionView(this).apply {
        attachActivity(this@WelcomeScreenActivity)
        setIsAnimated(true)
        setConnectedText(getString(R.string.connected))
        setDisconnectedText(getString(R.string.disconnected))
        setTextColor(ContextCompat.getColor(this@WelcomeScreenActivity, R.color.white))
        makeTransparent()
      }
```

```java
connectionView.show(true, 3000)
connectionView.hide()
```
