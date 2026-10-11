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

    fun findOne(id: Long): CutStyle? {
        return cuts.find{p-> p.id == id}
    }
    fun update(cut: CutStyle): Boolean{
        val foundCut = findOne(cut.id)
        return if(foundCut!=null){
            foundCut.styleName=cut.styleName
            foundCut.notes= cut.notes
            foundCut.date=cut.date
            true
        }else{
        false }
    }
    fun delete(id: Long): Boolean{
        val foundCut = findOne(id)
        return if (foundCut!= null){
            cuts.remove(foundCut)
            true
        }else{
            false
        }
    }

}