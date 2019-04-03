package net.pharoz.home.extension

import net.pharoz.home.extension.unit.Area
import net.pharoz.home.extension.unit.Size

data class Section(
    val largeur: Size,
    val hauteur: Size
) {
    val surface: Area = largeur * hauteur
}