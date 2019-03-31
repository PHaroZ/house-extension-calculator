package net.pharoz.home.extension.compute

import net.pharoz.home.extension.Prop

/**
 * la cellule = la construction au dessus des poteaux
 */
data class Cellule(
    private val prop: Prop
) {
    /**
     * largeur à l'intérieur de l'ossature
     */
    val dimLargeurInt  = prop.dim.batLargeurExt - 2 * prop.dim.epaisseurOssature

    /**
     * profondeur à l'intérieur de l'ossature
     */
    val dimProfondeurInt = prop.dim.batProfondeurExt - 1 * prop.dim.epaisseurOssature
}