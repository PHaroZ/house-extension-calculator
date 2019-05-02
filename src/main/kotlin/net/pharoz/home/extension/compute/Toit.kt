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
    val surfaceIntParSolive: Area = prop.dim.toitSoliveEntraxe * (cellule.dimProfondeurInt / cosAngle)

    @Description("Surface sous rampants portée par une solive centrale, au raz de l'ossature extérieure")
    val surfaceExtRazParSolive: Area = prop.dim.toitSoliveEntraxe * (prop.dim.batProfondeurExt / cosAngle)

    @Description("différence d'atitude entre le bas et le haut du toit, au raz de l'ossature extérieure")
    val diffHauteurExt = prop.dim.batProfondeurExt * Math.tan(prop.angle.toit)

    @Description("épaisseur au dessus de la lisse basse, donc à partir des solives jusqu'à la couverture")
    val epaisseurExt =
        prop.dim.toitSoliveSection.hauteur + prop.dim.toitLitelageSection.hauteur + prop.dim.toitChevronSection.hauteur + prop.dim.toitCouvertureHauteur

    @Description("épaisseur intérieure, en dessous de la lisse basse")
    val epaisseurInt =
        prop.dim.toitIsolantInterieurEpaisseur + prop.dim.toitParementHauteur

    val parement = Parement()
    val isolantInterieur = IsolantInterieur()
    val solivage = Solivage()
    val isolantExterieur = IsolantExterieur()
    val contreventement = Contreventement()
    val litelage = Litelage()
    val chevronnage = Chevronnage()
    val couverture = Couverture()
    val neige = Neige()

    val chargeurs =
        hashSetOf(
            parement,
            isolantInterieur,
            solivage,
            isolantExterieur,
            contreventement,
            litelage,
            chevronnage,
            couverture,
            neige
        )

    val poidsTotal: Weight = Weight(chargeurs.sumByDouble { it.poidsTotal.value })
    @Description("poids portant sur une solive centrale, hors pignon")
    val poidsParSolive: Weight = Weight(chargeurs.sumByDouble { it.poidsParSolive.value })
    @Description("poids reposant sur 1 long mur, hors pignon")
    val poidsSurLongMur: Weight = poidsParSolive * solivage.nbSoliveEntreMurs / 2

    inner class Parement : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.poidsM2.parementInterieur
        override val poidsParSolive: Weight = surfaceIntParSolive * prop.poidsM2.parementInterieur
    }

    inner class IsolantInterieur : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.dim.toitIsolantInterieurEpaisseur * prop.poidsM3.isolant
        override val poidsParSolive: Weight = surfaceIntParSolive * prop.poidsM2.parementInterieur
    }

    inner class Solivage : Chargeur {
        @Description("nombre de solives entres les murs -> ne reposant que sur la lisse haute et basse")
        val nbSoliveEntreMurs: Int = Math.ceil((prop.dim.batLargeurExt - prop.dim.toitSoliveSection.largeur) / prop.dim.toitSoliveEntraxe).toInt() - 1
        @Description("nombres total de solives, y compris celles de rive")
        val nbSoliveTotal: Int = nbSoliveEntreMurs + 4
        val surfacePorteeParSolive = prop.dim.toitSoliveEntraxe * profondeurExt
        override val poidsParSolive: Weight = profondeurExt * prop.dim.toitSoliveSection.surface * prop.poidsM3.boisOssature
        override val poidsTotal: Weight = poidsParSolive * nbSoliveTotal
    }

    inner class IsolantExterieur : Chargeur {
        /**
         * surface d'une solive au raz extérieur
         */
        private val surfaceSolivageParSoliveExtRaz: Area =
            (prop.dim.batProfondeurExt / cosAngle) * prop.dim.toitSoliveSection.largeur
        /**
         * surface totale des solives au raz extérieur
         */
        private val surfaceSolivageTotalExtRaz: Area = surfaceSolivageParSoliveExtRaz * solivage.nbSoliveTotal
        val surfaceTotal: Area = surfaceExtRaz - surfaceSolivageTotalExtRaz
        override val poidsTotal: Weight = surfaceTotal * prop.dim.toitIsolantExterieurEpaisseur * prop.poidsM3.isolant
        override val poidsParSolive: Weight =
            (surfaceExtRazParSolive - surfaceSolivageParSoliveExtRaz) * prop.dim.toitIsolantExterieurEpaisseur * prop.poidsM3.isolant
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
        private val surface = prop.dim.toitChevronSection.surface
        val nbChevron: Int = Math.ceil(profondeurExt / prop.dim.toitChevronEntraxeMax).toInt() + 1
        val entraxe = profondeurExt / (nbChevron - 1)
        override val poidsParSolive: Weight = prop.dim.toitSoliveEntraxe * surface * prop.poidsM3.boisOssature * nbChevron
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
        val poidsTotal: Weight
        val poidsParSolive: Weight
    }
}

