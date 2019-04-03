package net.pharoz.home.extension.compute

import net.pharoz.home.extension.Description
import net.pharoz.home.extension.Prop
import net.pharoz.home.extension.unit.Area
import net.pharoz.home.extension.unit.AreaDensity
import net.pharoz.home.extension.unit.Size
import net.pharoz.home.extension.unit.Weight

data class Toit(
    private val prop: Prop,
    private val cellule: Cellule
) : PropertyHolder {
    /**
     * le cosinus de l'angle du toit
     */
    private val cosAngle: Double = Math.cos(prop.angle.toit)

    @Description("Longueur, y compris débord")
    val profondeurExt: Size = (prop.dim.batProfondeurExt / cosAngle) + prop.dim.toitDebord

    @Description("Largeur, y compris débords")
    val largeurExt: Size = prop.dim.batLargeurExt + (prop.dim.toitDebord * 2)

    @Description("Surface sous rampants, à l'intérieur de l'ossature")
    val surfaceInt: Area = cellule.dimLargeurInt * (cellule.dimProfondeurInt / cosAngle)

    @Description("Surface sous rampants, au raz de l'ossature extérieure")
    val surfaceExtRaz: Area = prop.dim.batLargeurExt * (prop.dim.batProfondeurExt / cosAngle)

    @Description("Surface sous rampants, jusqu'au débords")
    val surfaceExt: Area = profondeurExt * largeurExt

    @Description("Surface sous rampants portée par une solive centrale, à l'intérieur de l'ossature")
    val surfaceIntParSolive: Area = prop.dim.soliveEntraxe * (cellule.dimProfondeurInt / cosAngle)

    @Description("Surface sous rampants portée par une solive centrale, au raz de l'ossature extérieure")
    val surfaceExtRazParSolive: Area = prop.dim.batLargeurExt * (prop.dim.batProfondeurExt / cosAngle)

    @Description
    val parement = Parement()
    val isolantInterieur = IsolantInterieur()
    val solivage = Solivage()
    val isolantExterieur = IsolantExterieur()
    val contreventement = Contreventement()
    val litelage = Litelage()
    val couverture = Couverture()
    val neige = Neige()

    val chargeurs =
        hashSetOf(parement, isolantInterieur, solivage, isolantExterieur, contreventement, litelage, couverture, neige)

    val poidsTotal: Weight = Weight(chargeurs.sumByDouble { it.poidsTotal.value })

    inner class Parement : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.poidsM2.parementInterieur
        override val poidsParSolive: Weight = surfaceIntParSolive * prop.poidsM2.parementInterieur
    }

    inner class IsolantInterieur : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.dim.toitIsolantInterieurEpaisseur * prop.poidsM3.isolant
        override val poidsParSolive: Weight = surfaceIntParSolive * prop.poidsM2.parementInterieur
    }

    inner class Solivage : Chargeur {
        /**
         * nombre de solives entres les murs -> ne reposant que sur la lisse haute et basse
         */
        val nbSoliveEntreMurs: Int = Math.ceil(prop.dim.batLargeurExt / prop.dim.soliveEntraxe).toInt() - 1
        /**
         * nombres total de solives, y compris celles de rive
         */
        val nbSoliveTotal: Int = nbSoliveEntreMurs + 4
        /**
         * surface totale des solives au raz extérieur
         */
        val surfaceParSoliveExtRaz: Area = (prop.dim.batProfondeurExt / cosAngle) * prop.dim.soliveSection.largeur
        val surfacePorteeParSolive = prop.dim.soliveEntraxe * profondeurExt
        /**
         * surface totale des solives au raz extérieur
         */
        val surfaceTotalExtRaz: Area = surfaceParSoliveExtRaz * nbSoliveTotal
        override val poidsParSolive: Weight = profondeurExt * prop.dim.soliveSection.surface * prop.poidsM3.boisOssature
        override val poidsTotal: Weight = poidsParSolive * nbSoliveTotal
    }

    inner class IsolantExterieur : Chargeur {
        override val poidsTotal: Weight =
            (surfaceExtRaz - solivage.surfaceTotalExtRaz) * prop.dim.toitIsolantExterieurEpaisseur * prop.poidsM3.isolant
        override val poidsParSolive: Weight =
            (surfaceExtRazParSolive - solivage.surfaceParSoliveExtRaz) * prop.dim.toitIsolantExterieurEpaisseur * prop.poidsM3.isolant
    }

    inner class Contreventement : Chargeur {
        override val poidsTotal: Weight = surfaceExt * prop.poidsM2.osbContreventement
        override val poidsParSolive: Weight = solivage.surfacePorteeParSolive * prop.poidsM2.osbContreventement
    }

    inner class Litelage : Chargeur {
        val longueurTotal: Size = profondeurExt * solivage.nbSoliveTotal
        override val poidsParSolive: Weight =
            profondeurExt * prop.dim.toitLitelageSection.surface * prop.poidsM3.boisOssature
        override val poidsTotal: Weight = poidsParSolive * solivage.nbSoliveTotal
    }

    inner class Chevronnage : Chargeur {
        val surface = prop.dim.chevronSection.surface
        val nbChevron: Int = Math.ceil(profondeurExt / prop.dim.chevronEntraxeMax).toInt() + 1
        val entraxe = profondeurExt / (nbChevron - 1)
        override val poidsParSolive: Weight = prop.dim.soliveEntraxe * surface * prop.poidsM3.boisOssature * nbChevron
        override val poidsTotal: Weight = largeurExt * surface * prop.poidsM3.boisOssature
    }

    inner class Couverture : Chargeur {
        override val poidsTotal: Weight = surfaceExt * prop.poidsM2.couverture
        override val poidsParSolive: Weight = solivage.surfacePorteeParSolive * prop.poidsM2.couverture
    }

    inner class Neige : Chargeur {
        private val coefS200 = 0.45
        private val coefµ = 0.8
        /**
         * en kN/m²
         */
        private val coefS = coefS200 * coefµ * cosAngle
        val chargeParM2 = AreaDensity(coefS * 100)
        override val poidsTotal: Weight = surfaceExt * chargeParM2
        override val poidsParSolive: Weight = solivage.surfacePorteeParSolive * chargeParM2
    }

    interface Chargeur : PropertyHolder {
        @Description
        val poidsTotal: Weight
        @Description("poids portant sur une solive centrale, hors pinnon")
        val poidsParSolive: Weight
    }
}
