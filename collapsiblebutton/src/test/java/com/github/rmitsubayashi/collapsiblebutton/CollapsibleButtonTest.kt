package com.github.rmitsubayashi.collapsiblebutton

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Xml
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.*
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class CollapsibleButtonTest {
    private lateinit var cb: CollapsibleButton
    private lateinit var context: Context

    @Before
    fun init(){
        context = ApplicationProvider.getApplicationContext<Context>()
    }

    @Test
    fun defaultState(){
        cb = CollapsibleButton(context)
        assertFalse(cb.isExpanded())
        assertEquals(
            shadowOf(cb.getToCollapseSrc()).createdFromResId,
            shadowOf(context.getDrawable(R.drawable.ic_to_collapse)).createdFromResId
        )
        assertEquals(
            shadowOf(cb.getToExpandSrc()).createdFromResId,
            shadowOf(context.getDrawable(R.drawable.ic_to_expand)).createdFromResId
        )
    }

    @Test
    fun stateWithAttributeSet(){
        val xmlParser = context.resources.getXml(R.layout.test_layout)
        xmlParser.next()
        xmlParser.nextTag()

        val attrSet = Xml.asAttributeSet(xmlParser)

        cb = CollapsibleButton(context, attrSet)
        assertTrue(cb.isExpanded())
        assertEquals(
            shadowOf(cb.getToCollapseSrc()).createdFromResId,
            shadowOf(context.getDrawable(R.drawable.ic_test)).createdFromResId
        )
        assertEquals(
            shadowOf(cb.getToExpandSrc()).createdFromResId,
            shadowOf(context.getDrawable(R.drawable.ic_test)).createdFromResId
        )
    }

    @Test
    fun setIsExpanded(){
        cb = CollapsibleButton(context)
        cb.setExpanded(true)
        assertTrue(cb.isExpanded())
        cb.setExpanded(false)
        assertFalse(cb.isExpanded())
    }

    @Test
    fun setToCollapseSrc(){
        val mockDrawable: Drawable = context.getDrawable(R.drawable.ic_test)!!
        cb = CollapsibleButton(context)
        cb.setToCollapseSrc(mockDrawable)
        assertEquals(mockDrawable, cb.getToCollapseSrc())
    }

    @Test
    fun setToExpandSrc(){
        cb = CollapsibleButton(context)
        val mockDrawable: Drawable = context.getDrawable(R.drawable.ic_test)!!
        cb.setToExpandSrc(mockDrawable)
        assertEquals(mockDrawable, cb.getToExpandSrc())
    }

    @Test
    fun click_togglesExpanded(){
        cb = CollapsibleButton(context)
        cb.performClick()
        assertTrue(cb.isExpanded())
        cb.performClick()
        assertFalse(cb.isExpanded())
    }

    @Test
    fun setOnClickListener_click_togglesExpanded(){
        cb = CollapsibleButton(context)
        cb.setOnClickListener { _ ->  }
        cb.performClick()
        assertTrue(cb.isExpanded())
        cb.performClick()
        assertFalse(cb.isExpanded())
    }

    @Test
    fun setOnClickListener_click_clickListenerFires(){
        cb = CollapsibleButton(context)
        var fired = false
        cb.setOnClickListener { _ -> fired = true }
        cb.performClick()
        assertTrue(fired)
    }
}