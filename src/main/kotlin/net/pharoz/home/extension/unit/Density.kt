package net.pharoz.home.extension.unit

data class Density(
    /**
     * in daN/m3
     */
    override val value: Double
) : QuantityWithUnit {
    operator fun plus(other: Density) = Density(this.value + other.value)
    operator fun minus(other: Density) = Density(this.value - other.value)
    operator fun times(other: Volume) = Weight(this.value * other.value)

    operator fun times(qu: Int) = Density(this.value * qu)
    operator fun times(qu: Double) = Density(this.value * qu)
    operator fun div(qu: Int) = Density(this.value / qu)
    operator fun div(qu: Double) = Density(this.value / qu)

    override val unitName: String = "kg/m3"
}