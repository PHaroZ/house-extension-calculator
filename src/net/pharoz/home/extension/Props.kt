package net.pharoz.home.extension

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
    val boisPilotis: Double = 420.0,

    /**
     * douglas
     */
    val boisOssature: Double = 420.0,

    /**
     * laine de bois
     */
    val isolant: Double = 55.0
)

/**
 * valeurs en daN/m2
 */
data class PropPoidsM2(
    /**
     * OSB3 22mm, 700daN/m3
     */
    val osbPlancher: Double = 7 * 2.2,

    /**
     * OSB3 12mm, 700daN/m3
     */
    val osbContreventement: Double = 7 * 1.2,

    /**
     * parquet chêne 23mm avec lambourdes
     */
    val sol: Double = 21.0,

    /**
     * BA13
     */
    val parementInterieur: Double = 9.5
)

data class PropAngle(
    val toit: Double = Math.toRadians(10.0)
)

/**
 * valeurs en mm
 */
data class PropDim(
    /**
     * au raz de l'ossature, hors bardage
     */
    val batLargeurExt: Double = 4500.mmToM(),

    /**
     * au raz de l'ossature, hors bardage
     */
    val batProfondeurExt: Double = 4500.mmToM(),

    /**
     * épaisseur ossature + OSB
     */
    val epaisseurOssature: Double = 222.mmToM(),

    val soliveEntraxe: Double = 600.mmToM(),
    val soliveSection: Section = Section(75.mmToM(), 225.mmToM()),

    val toitDebord: Double = 400.mmToM(),

    val toitIsolantInterieurEpaisseur: Double = 100.mmToM(),
    val toitIsolantExterieurEpaisseur: Double = 200.mmToM()
)