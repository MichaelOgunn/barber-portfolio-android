package org.setu.barberportfolio.models

import java.util.Date

// id stylename notes dates
data class CutStyle (
    var id: Long = 0L,
    var styleName: String ="",
    var notes: String ="",
    var date: Date = Date()
)