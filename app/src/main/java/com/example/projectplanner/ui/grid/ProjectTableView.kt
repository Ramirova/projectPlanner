package com.example.projectplanner.ui.grid

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

    /**
     * date : 2019-02-08
     * get all schedules TimetableView has.
     */
    val allSchedulesInStickers: ArrayList<Schedule>
        get() {
            val allSchedules = ArrayList<Schedule>()
            for (key in stickers.keys) {
                for (schedule in stickers[key]!!.schedules) {
                    allSchedules.add(schedule)
                }
            }
            return allSchedules
        }

    /**
     * date : 2019-02-08
     * Used in Edit mode, To check a invalidate schedule.
     */
    fun getAllSchedulesInStickersExceptIdx(idx: Int): ArrayList<Schedule> {
        val allSchedules = ArrayList<Schedule>()
        for (key in stickers.keys) {
            if (idx == key) continue
            for (schedule in stickers[key]!!.schedules) {
                allSchedules.add(schedule)
            }
        }
        return allSchedules
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

    private fun add(schedules: ArrayList<Schedule>, specIdx: Int) {
        val count = if (specIdx < 0) ++stickerCount else specIdx
        val sticker = Sticker()
        for (schedule in schedules) {
            val tv = TextView(kontext)
            val param = createStickerParam(schedule)
            tv.layoutParams = param
            tv.setPadding(10, 0, 10, 0)
            tv.text = """
                ${schedule.getClassTitle()}
                ${schedule.getClassPlace()}
                """.trimIndent()
            tv.setTextColor(Color.parseColor("#FFFFFF"))
            tv.setTextSize(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_STICKER_FONT_SIZE_DP.toFloat()
            )
            tv.setTypeface(null, Typeface.BOLD)
            tv.setOnClickListener {
                if (stickerSelectedListener != null) stickerSelectedListener!!.OnStickerSelected(
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

    fun createSaveData(): String {
        return SaveManager.saveSticker(stickers)
    }

    fun load(data: String?) {
        removeAll()
        stickers = SaveManager.loadSticker(data)
        var maxKey = 0
        for (key in stickers.keys) {
            val schedules = stickers[key]!!.schedules
            add(schedules, key)
            if (maxKey < key) maxKey = key
        }
        stickerCount = maxKey + 1
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

    fun edit(idx: Int, schedules: ArrayList<Schedule>) {
        remove(idx)
        add(schedules, idx)
    }

    fun remove(idx: Int) {
        val sticker = stickers[idx]
        for (tv in sticker!!.view) {
            stickerBox!!.removeView(tv)
        }
        stickers.remove(idx)
        setStickerColor()
    }

    // FIXME: this doesn't check for index boundaries
    fun setHeaderOnClickListener(idx: Int, listener: OnClickListener) {
        val row = tableHeader!!.getChildAt(0) as TableRow
        row.getChildAt(idx).setOnClickListener(listener)
    }

    fun setHeaderHighlight(idx: Int) {
        if (idx < 0) return
        val row = tableHeader!!.getChildAt(0) as TableRow
        val element = row.getChildAt(idx)
        if (highlightMode == HighlightMode.COLOR) {
            val tx = element as TextView
            tx.setTextColor(Color.parseColor("#FFFFFF"))
            tx.setBackgroundColor(headerHighlightColor)
            tx.setTypeface(null, Typeface.BOLD)
            tx.setTextSize(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP.toFloat()
            )
        } else if (highlightMode == HighlightMode.IMAGE) {
            val outer = RelativeLayout(kontext)
            outer.layoutParams = createTableRowParam(cellHeight)
            val iv = ImageView(kontext)
            val params =
                RelativeLayout.LayoutParams(headerHighlightImageSize, headerHighlightImageSize)
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            iv.layoutParams = params
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            row.removeViewAt(idx)
            outer.addView(iv)
            row.addView(outer, idx)
            if (headerHighlightImage != null) {
                iv.setImageDrawable(headerHighlightImage)
            }
        }
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

    fun onCreateByBuilder(builder: Builder) {
        rowCount = builder.rowCount
        columnCount = builder.columnCount
        cellHeight = builder.cellHeight
        sideCellWidth = builder.sideCellWidth
        headerTitle = builder.headerTitle
        stickerColors = builder.stickerColors
        startTime = builder.startTime
        headerHighlightColor = builder.headerHighlightColor

        init()
    }

    interface OnStickerSelectedListener {
        fun OnStickerSelected(idx: Int, schedules: ArrayList<Schedule>?)
    }

    class Builder(private val context: Context) {
        var rowCount: Int
        var columnCount: Int
        var cellHeight: Int
        var sideCellWidth: Int
        var headerTitle: Array<String>
        var stickerColors: Array<String>
        var startTime: Int
        var headerHighlightColor: Int
        fun setRowCount(n: Int): Builder {
            rowCount = n
            return this
        }

        fun setColumnCount(n: Int): Builder {
            columnCount = n
            return this
        }

        fun setCellHeight(dp: Int): Builder {
            cellHeight =
                dp2Px(
                    dp
                )
            return this
        }

        fun setSideCellWidth(dp: Int): Builder {
            sideCellWidth =
                dp2Px(
                    dp
                )
            return this
        }

        fun setHeaderTitle(titles: Array<String>): Builder {
            headerTitle = titles
            return this.setColumnCount(headerTitle.size)
        }

        fun setStickerColors(colors: Array<String>): Builder {
            stickerColors = colors
            return this
        }

        fun setStartTime(t: Int): Builder {
            startTime = t
            return this
        }

        fun setHeaderHighlightColor(c: Int): Builder {
            headerHighlightColor = c
            return this
        }

        fun build(): ProjectTableView {
            val timetableView =
                ProjectTableView(context)
            timetableView.onCreateByBuilder(this)
            return timetableView
        }

        init {
            rowCount =
                DEFAULT_ROW_COUNT
            columnCount =
                DEFAULT_COLUMN_COUNT
            cellHeight =
                dp2Px(
                    DEFAULT_CELL_HEIGHT_DP
                )
            sideCellWidth =
                dp2Px(
                    DEFAULT_SIDE_CELL_WIDTH_DP
                )
            headerTitle = context.resources.getStringArray(R.array.default_header_title)
            stickerColors = context.resources.getStringArray(R.array.default_sticker_color)
            startTime =
                DEFAULT_START_TIME
            headerHighlightColor =
                context.resources.getColor(R.color.default_header_highlight_color)
        }
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
        private const val DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP = 15
        private const val DEFAULT_STICKER_FONT_SIZE_DP = 13
        private fun dp2Px(dp: Int): Int {
            return (dp * Resources.getSystem()
                .displayMetrics.density).toInt()
        }
    }
}