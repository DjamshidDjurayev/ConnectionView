package co.djurayev.connectionview.connection

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import co.djurayev.connectionview.R
import co.djurayev.connectionview.utils.ScreenUtils
import java.lang.ref.WeakReference

class ConnectionView : RelativeLayout {
  private var connectedText: String? = null
  private var disconnectedText: String? = null
  private var connectionViewText: TextView? = null
  private var animated = false
  private var isConnected = false
  private var activity: WeakReference<Activity>? = null
  private var connectionHandler: Handler? = null
  private var autoHideRunnable: Runnable? = null

  private val connectionTextByState: String?
    get() = if (isConnected) connectedText else disconnectedText

  private fun addDecor() {
    clearDecor()

    val decor = activity!!.get()?.window?.decorView as ViewGroup
    val rootView = activity!!.get()?.window?.decorView?.rootView
    rootView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE

    rootView?.setOnSystemUiVisibilityChangeListener { _ ->
      this.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    decor.addView(this)
  }

  constructor(context: Context) : super(context) {
    initialize()
  }

  constructor(context: Context,
              attrs: AttributeSet?) : super(context, attrs) {
    initialize()
  }

  constructor(context: Context,
              attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    initialize()
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context,
              attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    initialize()
  }

  private fun initialize() {
    connectionHandler = Handler(Looper.getMainLooper())

    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight(context))
    gravity = Gravity.CENTER

    connectionViewText = TextView(context)
    connectionViewText!!.gravity = Gravity.CENTER
    connectionViewText!!.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    connectionViewText!!.setBackgroundResource(R.drawable.connection_view_selector)
    connectionViewText!!.visibility = View.GONE

    addView(connectionViewText)
  }

  fun setIsConnected(isConnected: Boolean) {
    this.isConnected = isConnected
  }

  fun show(autoHide: Boolean, autoHideDelayTime: Int) {
    if (autoHideRunnable != null) {
      connectionHandler!!.removeCallbacks(autoHideRunnable)
      autoHideRunnable = null
    }

    addDecor()

    connectionViewText!!.text = connectionTextByState
    connectionViewText!!.translationY = 0f
    connectionViewText!!.visibility = View.VISIBLE
    connectionViewText!!.isActivated = !isConnected

    if (animated) {
      val translateAnimator = ObjectAnimator.ofFloat(connectionViewText, View.TRANSLATION_Y, -ScreenUtils.getStatusBarHeight(context).toFloat(), 0f)
          .setDuration(ANIMATION_DURATION.toLong())
      translateAnimator.interpolator = AccelerateDecelerateInterpolator()
      translateAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          if (autoHide) {
            autoHideRunnable = Runnable {
              hide()
            }
            connectionHandler!!.postDelayed(autoHideRunnable, autoHideDelayTime.toLong())
          }
        }
      })
      translateAnimator.setAutoCancel(true)
      translateAnimator.start()
    }
  }

  fun hide() {
    if (animated) {
      val translationAnimator = ObjectAnimator.ofFloat(connectionViewText, View.TRANSLATION_Y, 0f, -ScreenUtils.getStatusBarHeight(context).toFloat())
          .setDuration(ANIMATION_DURATION.toLong())
      translationAnimator.interpolator = AccelerateDecelerateInterpolator()
      translationAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          connectionViewText!!.visibility = View.GONE
          connectionViewText!!.text = ""
          clearDecor()
        }
      })
      translationAnimator.setAutoCancel(true)
      translationAnimator.start()
    } else {
      connectionViewText!!.visibility = View.GONE
    }
  }

  private fun clearDecor() {
    activity!!.get()?.window?.decorView?.rootView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    val decor = activity!!.get()?.window?.decorView as ViewGroup
    decor.removeView(this@ConnectionView)
  }

  fun setTextColor(textColor: Int) {
    connectionViewText!!.setTextColor(textColor)
  }

  fun makeTransparent() {
    connectionViewText!!.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
  }

  fun setBackgroundSelector(backgroundSelector: Int) {
    connectionViewText!!.setBackgroundResource(backgroundSelector)
  }

  fun setTextSize(textSize: Float) {
    connectionViewText!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
  }

  fun setConnectedText(textValue: String) {
    this.connectedText = textValue
    connectionViewText!!.text = connectionTextByState
  }

  fun setDisconnectedText(textValue: String) {
    this.disconnectedText = textValue
    connectionViewText!!.text = connectionTextByState
  }

  fun attachActivity(activity: Activity) {
    this.activity = WeakReference(activity)
  }

  fun setIsAnimated(isAnimated: Boolean) {
    this.animated = isAnimated
  }

  override fun onDetachedFromWindow() {
    if (autoHideRunnable != null) {
      connectionHandler?.removeCallbacks(autoHideRunnable)
      autoHideRunnable = null
    }
    activity?.get()?.window?.decorView?.setOnSystemUiVisibilityChangeListener(null)
    super.onDetachedFromWindow()
  }

  companion object {
    private const val ANIMATION_DURATION = 400
  }
}
