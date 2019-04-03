package net.pharoz.home.extension

import net.pharoz.home.extension.compute.Cellule
import net.pharoz.home.extension.compute.Toit


fun main(args: Array<String>) {
    val prop = Prop();
    val cellule = Cellule(prop)
    val toit = Toit(prop, cellule)


    System.out.htmlize("Toit", toit)
}

