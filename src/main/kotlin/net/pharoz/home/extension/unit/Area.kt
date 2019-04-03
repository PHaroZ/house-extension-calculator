package net.pharoz.home.extension.unit

class Area(
    /**
     * in meter*meter
     */
    override val value: Double
) : QuantityWithUnit {
    operator fun plus(other: Area) = Area(this.value + other.value)
    operator fun minus(other: Area) = Area(this.value - other.value)
    operator fun times(other: Size) = Volume(this.value * other.value)

    operator fun times(other: AreaDensity) = other * this

    operator fun times(qu: Int) = Area(this.value * qu)
    operator fun times(qu: Double) = Area(this.value * qu)
    operator fun div(qu: Int) = Area(this.value / qu)
    operator fun div(qu: Double) = Area(this.value / qu)


    override val unitName: String = "m2"
}