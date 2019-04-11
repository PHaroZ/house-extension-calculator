package net.pharoz.home.extension.compute

import net.pharoz.home.extension.Description
import net.pharoz.home.extension.Prop
import net.pharoz.home.extension.unit.Area
import net.pharoz.home.extension.unit.Size
import net.pharoz.home.extension.unit.Weight

data class LongMur(
    private val prop: Prop,
    private val cellule: Cellule
) : PropertyHolder {
    @Description("longueur de l'ossature du mur, entre les mignons")
    val longueurOssature: Size = prop.dim.batLargeurExt + (prop.dim.epaisseurOssature * 2)

    @Description("hauteur de l'ossature, lisses comprises")
    val hauteurOssature: Size = prop.dim.hauteurIntMin + cellule.toit.epaisseurInt

    val longueurInt: Size = longueurOssature - (prop.dim.murIsolantInterieurEpaisseur) * 2

    @Description("Surface, à l'intérieur de l'ossature")
    val surfaceInt: Area = longueurInt * prop.dim.hauteurIntMin

    val parement = Parement()
    val isolantInterieur = IsolantInterieur()
    val ossature = Ossature()
    val isolantExterieur = IsolantExterieur()
    val contreventement = Contreventement()
    val litelage = Litelage()
    val bardage = Bardage()

    val chargeurs =
        hashSetOf(
            parement,
            isolantInterieur,
            ossature,
            isolantExterieur,
            contreventement,
            litelage,
            bardage
        )

    val poidsTotal: Weight = Weight(chargeurs.sumByDouble { it.poidsTotal.value })

    inner class Parement : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.poidsM2.parementInterieur
    }

    inner class IsolantInterieur : Chargeur {
        override val poidsTotal: Weight = surfaceInt * prop.dim.murIsolantInterieurEpaisseur * prop.poidsM3.isolant
    }

    inner class Ossature : Chargeur {
        @Description("nombre de montant à l'intérieur du mur, hors contour")
        val nbMontantEntreMurs: Int = Math.ceil((longueurOssature - prop.dim.murOssatureSection.largeur) / prop.dim.murOssatureEntraxe).toInt() - 1
        @Description("nombre total de montants (y compris contour doublé)")
        val nbMontantTotal: Int = nbMontantEntreMurs + 4
        val hauteurMontant: Size = hauteurOssature - prop.dim.murOssatureSection.largeur * 3
        private val poidsMontant: Weight =
            prop.dim.murOssatureSection.surface * hauteurMontant * prop.poidsM3.boisOssature
        private val poidsMontantTotal = poidsMontant * nbMontantTotal
        private val poidLisseTotal: Weight =
            longueurOssature * prop.dim.murOssatureSection.surface * prop.poidsM3.boisOssature * 3
        override val poidsTotal: Weight = poidsMontantTotal + poidLisseTotal
    }

    inner class IsolantExterieur : Chargeur {
        val surfaceTotal: Area =
            ossature.hauteurMontant * (longueurOssature - (prop.dim.murOssatureSection.largeur * ossature.nbMontantEntreMurs))
        override val poidsTotal: Weight = surfaceTotal * prop.dim.murIsolantExterieurEpaisseur * prop.poidsM3.isolant
    }

    inner class Contreventement : Chargeur {
        override val poidsTotal: Weight = longueurOssature * hauteurOssature * prop.poidsM2.osbContreventement
    }

    inner class Litelage : Chargeur {
        val longueurTotal: Size = hauteurOssature * ossature.nbMontantTotal
        override val poidsTotal: Weight =
            longueurTotal * prop.dim.murLitelageSection.surface * prop.poidsM3.boisOssature
    }

    inner class Bardage : Chargeur {
        override val poidsTotal: Weight = longueurOssature * hauteurOssature * prop.poidsM2.bardage
    }

    interface Chargeur : PropertyHolder {
        val poidsTotal: Weight
    }

}

