package net.pharoz.home.extension

import net.pharoz.home.extension.compute.Cellule
import net.pharoz.home.extension.compute.Toit


fun main(args: Array<String>) {
    println("Hello World!")
    val prop = Prop();
    val cellule = Cellule(prop)
    val toit = Toit(prop, cellule)

    println(toit)
}