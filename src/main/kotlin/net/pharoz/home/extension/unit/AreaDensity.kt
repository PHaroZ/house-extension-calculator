package net.pharoz.home.extension.unit

data class AreaDensity(
    /**
     * in daN/m2
     */
    override val value: Double
) : QuantityWithUnit {
    operator fun plus(other: AreaDensity) = AreaDensity(this.value + other.value)
    operator fun minus(other: AreaDensity) = AreaDensity(this.value - other.value)
    operator fun times(other: Area) = Weight(this.value * other.value)

    operator fun times(qu: Int) = AreaDensity(this.value * qu)
    operator fun times(qu: Double) = AreaDensity(this.value * qu)
    operator fun div(qu: Int) = AreaDensity(this.value / qu)
    operator fun div(qu: Double) = AreaDensity(this.value / qu)

    override val unitName: String = "kg/m2"
}