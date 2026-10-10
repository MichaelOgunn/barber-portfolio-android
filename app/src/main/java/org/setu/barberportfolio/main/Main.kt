package org.setu.barberportfolio.main

import org.setu.barberportfolio.models.CutMemStore
import org.setu.barberportfolio.models.CutStyle

val store = CutMemStore()

fun main(){
    println("==Barber app console=====")
    var input: Int
    do {
        input = menu()
        when (input){
            1 -> addCut()
            2 -> listCuts()
        }
    }while (input !=0)
}
fun menu(): Int{
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Cut")
    println(" 2. List All Cuts")
    print("\nEnter option: ")
    return readlnOrNull()?.toIntOrNull() ?: -1
}
fun addCut(){
    println("\n--- Add Cut ---")
    print("Enter Cut: ")
    val styleName = readlnOrNull()?.trim().orEmpty()
    print("enter Notes")
    val notes = readlnOrNull()?.trim().orEmpty()
    if (styleName.isNotEmpty()){
        val cut = CutStyle(styleName=styleName, notes = notes)
        store.create(cut)
        println("Cut Style added with Id: ${cut.id}")
    }else{
        println("Style Name can not be empty. Creation canceled ")
    }
}
fun listCuts() {
    println("\n--- All Placemarks ---")
    val cutStyles = store.findAll()
    if (cutStyles.isEmpty()) {
        println("No placemarks stored yet.")
    } else {
        cutStyles.forEach { println("ID: ${it.id} | Title: ${it.styleName} | Description: ${it.notes}") }
    }
}
