package net.pharoz.home.extension.compute

import net.pharoz.home.extension.Prop

data class Toit(
    private val prop: Prop,
    private val cellule: Cellule
) {
    /**
     * le cosinus de l'angle du toit
     */
    val cosAngle = Math.cos(prop.angle.toit)

    /**
     * surface intérieur en m2
     */
    val surfaceInt = cellule.dimLargeurInt * cellule.dimProfondeurInt / cosAngle

    /**
     * surface extérieur au raz de l'ossature en m2
     */
    val surfaceExtRaz = prop.dim.batLargeurExt * prop.dim.batProfondeurExt / cosAngle

    /**
     * surface intérieur dont la portée correspond à une solive (en m2)
     */
    val surfaceIntParSolive = prop.dim.soliveEntraxe * cellule.dimProfondeurInt / cosAngle

    /**
     * surface au raz de l'ossature dont la portée correspond à une solive (en m2)
     */
    val surfaceExtRazParSolive = prop.dim.batLargeurExt * prop.dim.batProfondeurExt / cosAngle

    val parement = Parement()
    val isolantInterieur = IsolantInterieur()
    val solivage = Solivage()
    val isolantExterieur = IsolantExterieur()

    inner class Parement {
        val poidsTotal = surfaceInt * prop.poidsM2.parementInterieur
        val poidsParSolive = surfaceIntParSolive * prop.poidsM2.parementInterieur
    }

    inner class IsolantInterieur {
        val poidsTotal = surfaceInt * prop.dim.toitIsolantInterieurEpaisseur * prop.poidsM3.isolant
        val poidsParSolive = surfaceIntParSolive * prop.poidsM2.parementInterieur
    }

    inner class Solivage {
        /**
         * nombre de solives entres les murs -> ne reposant que sur la lisse haute et basse
         */
        val nbSoliveEntreMurs = Math.ceil(prop.dim.batLargeurExt / prop.dim.soliveEntraxe) - 1
        /**
         * nombres total de solives, y compris celles de rive
         */
        val nbSoliveTotal = nbSoliveEntreMurs + 4
        /**
         * surface totale du solivage au raz extérieur
         */
        val surfaceParSoliveExtRaz = (prop.dim.batProfondeurExt / cosAngle) * prop.dim.soliveSection.largeur
        /**
         * surface totale du solivage au raz extérieur
         */
        val surfaceTotalExtRaz = surfaceParSoliveExtRaz * nbSoliveTotal
        val longueur = (prop.dim.batProfondeurExt + prop.dim.toitDebord) / cosAngle
        val poidsParSolive = longueur * prop.dim.soliveSection.surface * prop.poidsM3.boisOssature
        val poidsTotal = poidsParSolive * nbSoliveTotal
    }

    inner class IsolantExterieur {
        val poidsTotal =
            (surfaceExtRaz - solivage.surfaceTotalExtRaz) * prop.dim.toitIsolantExterieurEpaisseur * prop.poidsM3.isolant
        val poidsParSolive =
            (surfaceExtRazParSolive - solivage.surfaceParSoliveExtRaz) * prop.dim.toitIsolantExterieurEpaisseur * prop.poidsM2.parementInterieur
    }

}