package net.pharoz.home.extension.unit

data class Weight(
    /**
     * in daN
     */
    override val value: Double
) : QuantityWithUnit {
    operator fun plus(other: Weight) = Weight(this.value + other.value)
    operator fun minus(other: Weight) = Weight(this.value - other.value)
    operator fun div(other: Weight) = this.value / other.value

    operator fun times(qu: Int) = Weight(this.value * qu)
    operator fun times(qu: Double) = Weight(this.value * qu)
    operator fun div(qu: Int) = Weight(this.value / qu)
    operator fun div(qu: Double) = Weight(this.value / qu)

    override val unitName: String = "kg"
}