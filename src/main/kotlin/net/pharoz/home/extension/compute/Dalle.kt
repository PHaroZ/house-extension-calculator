package net.pharoz.home.extension.compute

import net.pharoz.home.extension.Description
import net.pharoz.home.extension.Prop
import net.pharoz.home.extension.unit.Area
import net.pharoz.home.extension.unit.Size
import net.pharoz.home.extension.unit.Weight

data class Dalle(
    private val prop: Prop,
    private val cellule: Cellule
) : PropertyHolder {
    @Description("Surface à l'intérieur de l'ossature")
    val surfaceInt: Area = cellule.dimLargeurInt * cellule.dimProfondeurInt

    @Description("Surface portée par une solive centrale, à l'intérieur de l'ossature")
    val surfaceIntParSolive: Area = prop.dim.dalleSoliveEntraxe * cellule.dimProfondeurInt

    val solivage = Solivage()
    val parement = Parement()
    val plancher = Plancher()
    val isolantExterieur = IsolantExterieur()
    val fondCaisson = FonCaisson()
    val chargeExploitation = ChargeExploitation()

    val chargeurs =
        hashSetOf(
            solivage,
            parement,
            plancher,
            isolantExterieur,
            fondCaisson,
            chargeExploitation
        )

    val poidsTotal: Weight = Weight(chargeurs.sumByDouble { it.poidsTotal.value })
    @Description("poids portant sur une solive centrale, hors pignon")
    val poidsParSolive: Weight = Weight(chargeurs.sumByDouble { it.poidsParSolive.value })
    @Description("poids reposant sur 1 longrine de long mur, hors pignon")
    val poidsSurLongMur: Weight = poidsParSolive * solivage.nbSoliveEntreMurs / 2
    @Description("poids reposant sur 1 longrine de pignon")
    val poidsSurPignon: Weight =
        (poidsParSolive / 2) + prop.dim.dalleLongrineSection.surface * prop.dim.batProfondeurExt * prop.poidsM3.boisOssature

    inner class Solivage : Chargeur {
        val profondeurSolive: Size = prop.dim.batProfondeurExt - prop.dim.dalleLongrineSection.largeur
        @Description("nombre de solives entres les murs -> ne reposant que sur les longrines")
        val nbSoliveEntreMurs: Int =
            Math.ceil((prop.dim.batLargeurExt - prop.dim.dalleLongrineSection.largeur) / prop.dim.dalleSoliveEntraxe).toInt() - 1
        override val poidsParSolive: Weight =
            profondeurSolive * prop.dim.dalleSoliveSection.surface * prop.poidsM3.boisOssature
        val poidsTotalSolive: Weight = poidsParSolive * nbSoliveEntreMurs
        @Description("poids total des 4 longrines")
        val poidsTotalLongrine: Weight =
            prop.dim.dalleLongrineSection.surface * (prop.dim.batLargeurExt * 2 + prop.dim.batProfondeurExt * 2) * prop.poidsM3.boisOssature
        override val poidsTotal: Weight = poidsTotalSolive + poidsTotalLongrine
        private val surfaceSolivageParSolive: Area = profondeurSolive * prop.dim.dalleSoliveSection.largeur
        private val surfaceTotalSolivage: Area = surfaceSolivageParSolive * nbSoliveEntreMurs
        val surfaceEntreSoliveTotal: Area =
            ((prop.dim.batLargeurExt - prop.dim.dalleLongrineSection.largeur * 2) * profondeurSolive) - surfaceTotalSolivage
        val surfaceEntreSoliveParSolive: Area =
            ((profondeurSolive * prop.dim.dalleSoliveEntraxe) - surfaceSolivageParSolive)
    }

    inner class Parement : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.poidsM2.parementSol
        override val poidsParSolive: Weight = surfaceIntParSolive * prop.poidsM2.parementSol


    }

    inner class Plancher : Chargeur {
        override val poidsTotal: Weight = prop.dim.batProfondeurExt * prop.dim.batLargeurExt * prop.poidsM2.osbPlancher
        override val poidsParSolive: Weight =
            prop.dim.dalleSoliveEntraxe * solivage.profondeurSolive * prop.poidsM2.osbPlancher

    }

    inner class IsolantExterieur : Chargeur {
        override val poidsTotal: Weight =
            solivage.surfaceEntreSoliveTotal * prop.dim.dalleIsolantExterieurEpaisseur * prop.poidsM3.isolant
        override val poidsParSolive: Weight =
            solivage.surfaceEntreSoliveParSolive * prop.dim.dalleIsolantExterieurEpaisseur * prop.poidsM3.isolant
    }

    inner class FonCaisson : Chargeur {
        override val poidsTotal: Weight = solivage.surfaceEntreSoliveTotal * prop.poidsM2.osbContreventement
        override val poidsParSolive: Weight = solivage.surfaceEntreSoliveParSolive * prop.poidsM2.osbContreventement
    }

    inner class ChargeExploitation : Chargeur {
        override val poidsTotal: Weight =
            cellule.dimProfondeurInt * cellule.dimLargeurInt * prop.poidsM2.chargeExploitation
        override val poidsParSolive: Weight = surfaceIntParSolive * prop.poidsM2.chargeExploitation
    }

    interface Chargeur : PropertyHolder {
        val poidsTotal: Weight
        val poidsParSolive: Weight
    }
}

