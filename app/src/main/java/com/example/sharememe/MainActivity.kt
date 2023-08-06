package com.example.sharememe

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Picasso
import kotlin.math.abs

var currentimageurl: String? = null


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener{

    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    private lateinit var gestureDetector: GestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
        val imageView = findViewById<ImageView>(R.id.memeimageView)
        val sharebutton = findViewById<ImageView>(R.id.sharebutton)
        gestureDetector = GestureDetector(this);



        sharebutton.setOnClickListener{

            val bitmapDrawable = imageView.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val bitmappath = MediaStore.Images.Media.insertImage(contentResolver,
                bitmap,"some title",null)
            val bitmapUri = Uri.parse(bitmappath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/text"
            intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout Thia Cool Meme I got From Reddit $currentimageurl")
            intent.putExtra(Intent.EXTRA_STREAM,bitmapUri)
            startActivity(Intent.createChooser(intent,"Share Image this Meme"))
        }
    }

    private fun loadmeme() {
        val progressBar = findViewById<View>(R.id.progressbar)
        progressBar.visibility = View.VISIBLE
        val url ="https://meme-api.herokuapp.com/gimme"
        val memeimage = findViewById<ImageView>(R.id.memeimageView)



        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentimageurl = response.getString("url")

                Glide.with(this).load(currentimageurl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeimage)


            },
            { error ->
                // TODO: Handle error
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        }
        else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
        return
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {


        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        return
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if(diffX>0)
                    {
                        loadmeme()
                    }
                    else
                    {
                        loadmeme()
                    }
                }
            }
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }


}