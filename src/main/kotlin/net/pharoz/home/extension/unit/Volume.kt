package net.pharoz.home.extension.unit

data class Volume(
    /**
     * in meter*meter*meter
     */
    override val value: Double
) : QuantityWithUnit {
    operator fun plus(other: Volume) = Volume(this.value + other.value)
    operator fun minus(other: Volume) = Volume(this.value - other.value)

    operator fun times(other: Density) = other * this

    operator fun times(qu: Int) = Volume(this.value * qu)
    operator fun times(qu: Double) = Volume(this.value * qu)
    operator fun div(qu: Int) = Volume(this.value / qu)
    operator fun div(qu: Double) = Volume(this.value / qu)

    override val unitName: String = "m3"
}