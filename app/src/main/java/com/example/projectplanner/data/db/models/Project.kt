package com.example.projectplanner.data.db.models

import android.graphics.Color
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import java.sql.Date

class Project: Model {

    @Column(name = "title")
    public var title: String? = null

    @Column(name = "text")
    public var text: String? = null

    @Column(name = "start_date")
    public var startDate: Date? = null

    @Column(name = "end_date")
    public var endDate: Date? = null

    @Column(name = "tasks")
    public var tasks: ArrayList<Task>? = null

    @Column(name = "Color")
    public var color: Color? = null

    constructor(title: String, startDate: Date, endDate: Date, tasks: ArrayList<Task>?, color: Color) {
        this.title = title
        this.startDate = startDate
        this.endDate = endDate
        this.tasks = tasks
        this.color = color
    }

    constructor()
}