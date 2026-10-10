package org.setu.barberportfolio.models

import java.util.concurrent.atomic.AtomicLong

class CutMemStore {
    //findAll, create, update, delete and findOne
    private val cuts = ArrayList<CutStyle>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<CutStyle>{
        return cuts
    }
    fun create (cut : CutStyle) {
        cut.id = lastId.incrementAndGet()
        cuts.add(cut)
    }

}