package com.example.projectplanner.ui.grid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import com.github.tlaabs.timetableview.*
import com.example.projectplanner.R
import java.util.*
import kotlin.collections.ArrayList

class ProjectTableView : LinearLayout {
    private var rowCount = 0
    private var columnCount = 0
    private var cellHeight = 0
    private var sideCellWidth = 0
    private lateinit var headerTitle: Array<String>
    private lateinit var stickerColors: Array<String>
    private var startTime = 0
    private var headerHighlightColor = 0
    private var stickerBox: RelativeLayout? = null
    var tableHeader: TableLayout? = null
    var tableBox: TableLayout? = null
    private var kontext: Context
    var stickers = HashMap<Int, Sticker>()
    private var stickerCount = -1
    private var stickerSelectedListener: OnStickerSelectedListener? = null
    private var highlightMode = HighlightMode.COLOR
    private var headerHighlightImageSize = 0
    private var headerHighlightImage: Drawable? = null

    constructor(context: Context) : super(context, null) {
        this.kontext = context
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        this.kontext = context
        getAttrs(attrs)
        init()
    }

    @SuppressLint("CustomViewStyleable")
    private fun getAttrs(attrs: AttributeSet?) {
        val a = kontext.obtainStyledAttributes(attrs, R.styleable.TimetableView)
        rowCount = a.getInt(
            R.styleable.TimetableView_row_count,
            DEFAULT_ROW_COUNT
        ) - 1
        columnCount = a.getInt(
            R.styleable.TimetableView_column_count,
            DEFAULT_COLUMN_COUNT
        )
        cellHeight = a.getDimensionPixelSize(
            R.styleable.TimetableView_cell_height,
            dp2Px(
                DEFAULT_CELL_HEIGHT_DP
            )
        )

        sideCellWidth = a.getDimensionPixelSize(
            R.styleable.TimetableView_side_cell_width,
            dp2Px(
                DEFAULT_SIDE_CELL_WIDTH_DP
            )
        )
        val titlesId =
            a.getResourceId(R.styleable.TimetableView_header_title, R.array.default_header_title)
        headerTitle = a.resources.getStringArray(titlesId)
        val colorsId =
            a.getResourceId(R.styleable.TimetableView_sticker_colors, R.array.default_sticker_color)
        stickerColors = a.resources.getStringArray(colorsId)
        startTime = a.getInt(
            R.styleable.TimetableView_start_time,
            DEFAULT_START_TIME
        )
        headerHighlightColor = a.getColor(
            R.styleable.TimetableView_header_highlight_color,
            resources.getColor(R.color.default_header_highlight_color)
        )
        val highlightTypeValue =
            a.getInteger(R.styleable.TimetableView_header_highlight_type, 0)
        if (highlightTypeValue == 0) highlightMode =
            HighlightMode.COLOR else if (highlightTypeValue == 1) highlightMode =
            HighlightMode.IMAGE
        headerHighlightImageSize = a.getDimensionPixelSize(
            R.styleable.TimetableView_header_highlight_image_size,
            dp2Px(
                24
            )
        )
        headerHighlightImage = a.getDrawable(R.styleable.TimetableView_header_highlight_image)
        a.recycle()
    }

    private fun init() {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.view_timetable, this, false)
        addView(view)
        stickerBox = view.findViewById(R.id.sticker_box)
        tableHeader = view.findViewById(R.id.table_header)
        tableBox = view.findViewById(R.id.table_box)
        createTable()
    }

    fun setOnStickerSelectEventListener(listener: OnStickerSelectedListener?) {
        stickerSelectedListener = listener
    }

    fun updateHeaderTitle(newHeaderTitle: ArrayList<String>) {
        val headerColumns = ArrayList<String>()
        headerColumns.add("")

        headerColumns.addAll(newHeaderTitle)

        if (headerColumns.size == 1){
            headerColumns.add("")
        }

        headerTitle = headerColumns.toTypedArray()
        columnCount = headerColumns.size

        tableHeader!!.removeAllViews()
        tableBox!!.removeAllViews()

        init()
    }

    fun updateNumDays(newNumDays: Int) {
        rowCount = newNumDays

        tableHeader!!.removeAllViews()
        tableBox!!.removeAllViews()

        init()
    }

    fun add(schedules: ArrayList<Schedule>) {
        add(schedules, -1)
    }

    @SuppressLint("SetTextI18n")
    private fun add(schedules: ArrayList<Schedule>, specIdx: Int) {
        val count = if (specIdx < 0) ++stickerCount else specIdx
        val sticker = Sticker()
        for (schedule in schedules) {
            val tv = TextView(kontext)
            val param = createStickerParam(schedule)
            tv.layoutParams = param
            tv.setPadding(10, 0, 10, 0)
            tv.text = """
                ${schedule.classTitle}
                ${schedule.classPlace}
                """.trimIndent()
            tv.setTextColor(Color.parseColor("#FFFFFF"))
            tv.setTextSize(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_STICKER_FONT_SIZE_DP.toFloat()
            )
            tv.setTypeface(null, Typeface.BOLD)
            tv.setOnClickListener {
                if (stickerSelectedListener != null) stickerSelectedListener!!.onStickerSelected(
                    count,
                    schedules
                )
            }
            sticker.addTextView(tv)
            sticker.addSchedule(schedule)
            stickers[count] = sticker
            stickerBox!!.addView(tv)
        }
        setStickerColor()
    }

    fun removeAll() {
        for (key in stickers.keys) {
            val sticker = stickers[key]
            for (tv in sticker!!.view) {
                stickerBox!!.removeView(tv)
            }
        }
        stickers.clear()
    }

    // FIXME: this doesn't check for index boundaries
    fun setHeaderOnClickListener(idx: Int, listener: OnClickListener) {
        val row = tableHeader!!.getChildAt(0) as TableRow
        row.getChildAt(idx).setOnClickListener(listener)
    }

    private fun setStickerColor() {
        val size = stickers.size
        val orders = IntArray(size)
        var i = 0
        for (key in stickers.keys) {
            orders[i++] = key
        }
        Arrays.sort(orders)
        val colorSize = stickerColors.size
        i = 0
        while (i < size) {
            for (v in stickers[orders[i]]!!.view) {
                v.setBackgroundColor(Color.parseColor(stickerColors[i % colorSize]))
            }
            i++
        }
    }

    private fun calCellWidth(): Int {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val freeWidth = size.x - paddingLeft - paddingRight - sideCellWidth
        if (freeWidth > dp2Px(
                DEFAULT_CELL_WIDTH_DP
            ) * (columnCount - 1)) {
            if (columnCount > 1){
                return freeWidth / (columnCount - 1)
            }
            return freeWidth
        }
        return dp2Px(
            DEFAULT_CELL_WIDTH_DP
        )
    }

    @SuppressLint("RtlHardcoded")
    private fun createTable() {
        createTableHeader()
        for (i in 0 until rowCount) {
            val tableRow = TableRow(kontext)
            tableRow.layoutParams = createTableLayoutParam()
            for (k in 0 until columnCount) {
                val tv = TextView(kontext)
                tv.layoutParams = createTableRowParam(cellHeight)
                if (k == 0) {
                    tv.text = getHeaderTime(i)
                    tv.setTextColor(resources.getColor(R.color.colorHeaderText))
                    tv.setTextSize(
                        TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_SIDE_HEADER_FONT_SIZE_DP.toFloat()
                    )
                    tv.setBackgroundColor(resources.getColor(R.color.colorHeader))
                    tv.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    tv.layoutParams = createTableRowParam(sideCellWidth, cellHeight)
                } else {
                    tv.text = ""
                    tv.background = resources.getDrawable(R.drawable.item_border)
                    tv.gravity = Gravity.RIGHT
                }
                tableRow.addView(tv)
            }
            tableBox!!.addView(tableRow)
        }
    }

    private fun createTableHeader() {
        val tableRow = TableRow(kontext)
        tableRow.layoutParams = createTableLayoutParam()
        for (i in 0 until columnCount) {
            val tv = TextView(kontext)
            if (i == 0) {
                tv.layoutParams = createTableRowParam(sideCellWidth, cellHeight)
            } else {
                tv.layoutParams = createTableRowParam(cellHeight)
            }
            tv.setTextColor(resources.getColor(R.color.colorHeaderText))
            tv.setTextSize(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_HEADER_FONT_SIZE_DP.toFloat()
            )
            tv.text = headerTitle[i]
            tv.gravity = Gravity.CENTER
            tableRow.addView(tv)
        }
        tableHeader!!.addView(tableRow)
    }

    private fun createStickerParam(schedule: Schedule): RelativeLayout.LayoutParams {
        val param =
            RelativeLayout.LayoutParams(calCellWidth(), calStickerHeightPx(schedule))
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        param.setMargins(
            sideCellWidth + calCellWidth() * schedule.day,
            calStickerTopPxByTime(schedule.startTime),
            0,
            0
        )
        return param
    }

    private fun calStickerHeightPx(schedule: Schedule): Int {
        val startTopPx = calStickerTopPxByTime(schedule.startTime)
        val endTopPx = calStickerTopPxByTime(schedule.endTime)
        return endTopPx - startTopPx
    }

    private fun calStickerTopPxByTime(time: Time): Int {
        return (time.hour - startTime) * cellHeight + (time.minute / 60.0f * cellHeight).toInt()
    }

    private fun createTableLayoutParam(): TableLayout.LayoutParams {
        return TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
    }

    private fun createTableRowParam(h_px: Int): TableRow.LayoutParams {
        return TableRow.LayoutParams(calCellWidth(), h_px)
    }

    private fun createTableRowParam(w_px: Int, h_px: Int): TableRow.LayoutParams {
        return TableRow.LayoutParams(w_px, h_px)
    }

    private fun getHeaderTime(i: Int): String {
        return (i + 1).toString()
    }

    interface OnStickerSelectedListener {
        fun onStickerSelected(idx: Int, schedules: ArrayList<Schedule>?)
    }

    companion object {
        private const val DEFAULT_ROW_COUNT = 12
        private const val DEFAULT_COLUMN_COUNT = 6
        private const val DEFAULT_CELL_HEIGHT_DP = 50
        private const val DEFAULT_CELL_WIDTH_DP = 100
        private const val DEFAULT_SIDE_CELL_WIDTH_DP = 30
        private const val DEFAULT_START_TIME = 1
        private const val DEFAULT_SIDE_HEADER_FONT_SIZE_DP = 13
        private const val DEFAULT_HEADER_FONT_SIZE_DP = 15
        private const val DEFAULT_STICKER_FONT_SIZE_DP = 13
        private fun dp2Px(dp: Int): Int {
            return (dp * Resources.getSystem()
                .displayMetrics.density).toInt()
        }
    }
}