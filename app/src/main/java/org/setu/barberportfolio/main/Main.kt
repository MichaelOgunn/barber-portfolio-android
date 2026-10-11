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
            3 -> updateCut()
//            4 -> deleteCut()
//            5 -> searchCut()
            0 -> println("\nExiting portfolio application. Goodbye!")

        }
    }while (input !=0)
}
fun menu(): Int{
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Cut")
    println(" 2. List All Cuts")
    println(" 3. Update a Cut")
    println(" 4. Delete a Cut")
    println(" 5. Search Cut by ID")
    println(" 0. Exit")
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
    println("\n--- All Cut ---")
    val cutStyles = store.findAll()
    if (cutStyles.isEmpty()) {
        println("No Cut stored yet.")
    } else {
        cutStyles.forEach { println("ID: ${it.id} | Title: ${it.styleName} | Description: ${it.notes}") }
    }
}
fun updateCut(){
    println("\n--- Update Cut ---")
    listCuts()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Cut to update: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null && store.findOne(id) != null) {
        print("Enter New Style Name: ")
        val styleName = readlnOrNull()?.trim().orEmpty()
        print("Enter New Notes: ")
        val notes = readlnOrNull()?.trim().orEmpty()

        if (styleName.isNotEmpty()) {
            val updated = store.update(CutStyle(id = id, styleName = styleName,notes = notes))
            if (updated) println("Placemark updated successfully.")
        } else {
            println("Title cannot be empty. Update cancelled.")
        }
    } else {
        println("Placemark with ID $id not found.")
    }
}

