package net.pharoz.home.extension

data class Section(
    val largeur: Double,
    val hauteur: Double
) {
    val surface: Double = largeur * hauteur
}