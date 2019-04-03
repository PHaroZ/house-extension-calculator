package net.pharoz.home.extension.unit

data class Size(
    /**
     * in meter
     */
    override val value: Double
) : QuantityWithUnit {
    operator fun plus(other: Size) = Size(this.value + other.value)
    operator fun minus(other: Size) = Size(this.value - other.value)
    operator fun div(other: Size) = this.value / other.value
    operator fun times(other: Size) = Area(this.value * other.value)

    operator fun times(other: Area) = other * this

    operator fun times(qu: Int) = Size(this.value * qu)
    operator fun times(qu: Double) = Size(this.value * qu)
    operator fun div(qu: Int) = Size(this.value / qu)
    operator fun div(qu: Double) = Size(this.value / qu)

    companion object {
        fun mm(value: Int) = Size(value / 1000.0)
    }

    override val unitName: String = "m"
}