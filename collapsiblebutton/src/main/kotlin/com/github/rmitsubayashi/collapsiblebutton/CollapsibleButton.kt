package com.github.rmitsubayashi.collapsiblebutton

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton

class CollapsibleButton: ImageButton {
    private var mIsExpanded: Boolean = false
    private lateinit var mToExpandRes: Drawable
    private lateinit var mToCollapseRes: Drawable
    private var mPendingTargetViewID: Int = -1
    private var mTargetView: View? = null

    constructor(context: Context): super(context){
        mToExpandRes = context.getDrawable(R.drawable.ic_to_expand)!!
        mToCollapseRes = context.getDrawable(R.drawable.ic_to_collapse)!!
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        setCustomAttrs(attrs)
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        setCustomAttrs(attrs)
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes){
        setCustomAttrs(attrs)
        init()
    }

    private fun setCustomAttrs(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleButton)
        mIsExpanded = attributes.getBoolean(R.styleable.CollapsibleButton_expanded, false)
        mToExpandRes = attributes.getDrawable(R.styleable.CollapsibleButton_toExpandSrc) ?: context.getDrawable(R.drawable.ic_to_expand)!!
        mToCollapseRes = attributes.getDrawable(R.styleable.CollapsibleButton_toCollapseSrc) ?: context.getDrawable(R.drawable.ic_to_collapse)!!
        mPendingTargetViewID = attributes.getResourceId(R.styleable.CollapsibleButton_targetView, -1)
        attributes.recycle()
    }

    private fun init() {
        setExpanded(mIsExpanded)
        background = null
        if (!hasOnClickListeners()) {
            setOnClickListener {  }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mPendingTargetViewID != -1) {
            mTargetView = rootView.findViewById(mPendingTargetViewID)
            toggleTargetViewVisibility()
            mPendingTargetViewID = -1
        }
    }

    fun isExpanded(): Boolean = mIsExpanded
    fun setExpanded(isExpanded: Boolean) {
        mIsExpanded = isExpanded
        toggleButtonImage()
        toggleTargetViewVisibility()
    }

    private fun toggleButtonImage(){
        setImageDrawable(
            if (mIsExpanded) mToCollapseRes
            else mToExpandRes
        )
    }

    private fun toggleTargetViewVisibility(){
        mTargetView?.let {
            it.visibility = if (mIsExpanded) View.VISIBLE else View.GONE
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val newListener = OnClickListener {
            v ->
            l?.onClick(v)
            setExpanded(!mIsExpanded)
        }
        super.setOnClickListener(newListener)
    }

    fun setToCollapseSrc(drawable: Drawable){
        mToCollapseRes = drawable
    }

    fun setToCollapseSrc(resID: Int){
        context.getDrawable(resID)?.let {mToCollapseRes = it}
    }

    fun getToCollapseSrc(): Drawable = mToCollapseRes

    fun setToExpandSrc(drawable: Drawable){
        mToExpandRes = drawable
    }

    fun setToExpandSrc(resID: Int){
        context.getDrawable(resID)?.let {mToExpandRes = it}
    }

    fun getToExpandSrc(): Drawable = mToExpandRes

    fun setTargetView(view: View){
        mTargetView?.let {
            it.visibility = View.VISIBLE
        }

        mTargetView = view
        mPendingTargetViewID = -1
        if (isAttachedToWindow){
            toggleTargetViewVisibility()
        }
    }

    fun getTargetView(): View? = mTargetView
}