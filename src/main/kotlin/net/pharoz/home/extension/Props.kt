package net.pharoz.home.extension

import net.pharoz.home.extension.compute.Dalle
import net.pharoz.home.extension.unit.AreaDensity
import net.pharoz.home.extension.unit.Density
import net.pharoz.home.extension.unit.Size

data class Prop(
    val poidsM3: PropPoidsM3 = PropPoidsM3(),
    val poidsM2: PropPoidsM2 = PropPoidsM2(),
    val angle: PropAngle = PropAngle(),
    val dim: PropDim = PropDim()
)

/**
 * valeurs en daN/m3
 */
data class PropPoidsM3(
    /**
     * douglas
     */
    val boisPilotis: Density = Density(420.0),

    /**
     * douglas
     */
    val boisOssature: Density = Density(420.0),

    /**
     * laine de bois
     */
    val isolant: Density = Density(55.0)
)

/**
 * valeurs en daN/m2
 */
data class PropPoidsM2(
    /**
     * OSB3 22mm, 700daN/m3
     */
    val osbPlancher: AreaDensity = AreaDensity(7 * 2.2),

    /**
     * OSB3 12mm, 700daN/m3
     */
    val osbContreventement: AreaDensity = AreaDensity(7 * 1.2),

    /**
     * parquet chêne 23mm avec lambourdes
     */
    val parementSol: AreaDensity = AreaDensity(21.0),

    /**
     * BA13
     */
    val parementInterieur: AreaDensity = AreaDensity(11.0),

    /**
     * bacacier
     */
    val couverture: AreaDensity = AreaDensity(8.0),

    val bardage: AreaDensity = AreaDensity(20.0),

val chargeExploitation: AreaDensity = AreaDensity(150.0)
)

data class PropAngle(
    val toit: Double = Math.toRadians(5.8) // 10%
)

/**
 * valeurs en mm
 */
data class PropDim(
    /**
     * au raz de l'ossature, hors bardage
     */
    val batLargeurExt: Size = Size(4.5),

    /**
     * au raz de l'ossature, hors bardage
     */
    val batProfondeurExt: Size = Size(4.5),

    /**
     * épaisseur ossature + OSB
     */
    val epaisseurOssature: Size = Size.mm(212),

    val dalleSoliveEntraxe: Size = Size.mm(600),
    val dalleLongrineSection: Section = Section(Size.mm(75), Size.mm(250)),
    val dalleSoliveSection: Section = Section(Size.mm(60), Size.mm(225)),
    val dalleIsolantExterieurEpaisseur: Size = Size.mm(200),

    val murOssatureSection: Section = Section(Size.mm(50), Size.mm(200)),
    val murOssatureEntraxe: Size = Size.mm(600),
    val murIsolantInterieurEpaisseur: Size = Size.mm(40),
    val murIsolantExterieurEpaisseur: Size = Size.mm(200),
    val murLitelageSection: Section = Section(Size.mm(40), Size.mm(14)),
    val murContreLitelageSection: Section = Section(Size.mm(40), Size.mm(27)),

    val hauteurIntMin: Size = Size(2.45),

    val toitShevronEntraxeMax: Size = Size.mm(1200),
    val toitChevronSection: Section = Section(Size.mm(70), Size.mm(50)),
    val toitCouvertureHauteur: Size = Size.mm(50),
    /**
     * épaisseur BA13 + fourrure
     */
    val toitParementHauteur: Size = Size.mm(35),
    val toitSoliveEntraxe: Size = Size.mm(600),
    val toitSoliveSection: Section = Section(Size.mm(60), Size.mm(225)),
    val toitLitelageSection: Section = Section(Size.mm(40), Size.mm(27)),
    val toitDebord: Size = Size.mm(400),
    val toitIsolantInterieurEpaisseur: Size = Size.mm(100),
    val toitIsolantExterieurEpaisseur: Size = Size.mm(200)
)