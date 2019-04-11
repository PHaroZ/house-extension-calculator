package net.pharoz.home.extension

import net.pharoz.home.extension.compute.Cellule
import java.io.File


fun main(args: Array<String>) {
    val prop = Prop()
    val cellule = Cellule(prop)

    with(File("/tmp/house.html").writer()) {
        htmlize("Extension", cellule)
        flush()
    }
    System.out.println("file:///tmp/house.html")
//    System.out.htmlize("Toit", toit)
}

