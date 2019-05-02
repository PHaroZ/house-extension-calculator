package net.pharoz.home.extension.compute

import net.pharoz.home.extension.Prop

/**
 * la cellule = la construction au dessus des poteaux
 */
data class Cellule(
    private val prop: Prop
) : PropertyHolder {
    /**
     * largeur à l'intérieur de l'ossature
     */
    val dimLargeurInt = prop.dim.batLargeurExt - prop.dim.epaisseurOssature * 2

    /**
     * profondeur à l'intérieur de l'ossature
     */
    val dimProfondeurInt = prop.dim.batProfondeurExt - prop.dim.epaisseurOssature

    val toit = Toit(prop, this)
    val dalle = Dalle(prop, this)
    val longMur = LongMur(prop, this)

    val poidsTotal = dalle.poidsTotal + longMur.poidsTotal + (longMur.poidsTotal * 2) + toit.poidsTotal
    val poidsSurLongrineNord = dalle.poidsSurLongMur
    val poidsSurLongrineSud = dalle.poidsSurLongMur + longMur.poidsTotal + toit.poidsSurLongMur
    val poidsSurLongrinePignon = dalle.poidsSurPignon + longMur.poidsTotal /*+ (toit.poidsParSolive * 1.5)*/
}