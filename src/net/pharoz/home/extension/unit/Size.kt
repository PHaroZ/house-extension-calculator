package net.pharoz.home.extension.unit

data class Size (
    /**
     * in meter
     */
    val value : Double
) {
    operator fun plus(other: Size) = Size(this.value + other.value)
    operator fun minus(other: Size) = Size(this.value - other.value)
    operator fun times(other: Size) = Area(this.value * other.value)

    operator fun times(qu: Int) = Size(this.value * qu)
    operator fun times(qu: Double) = Size(this.value * qu)
    operator fun div(qu: Int) = Size(this.value / qu)
    operator fun div(qu: Double) = Size(this.value / qu)


    override fun toString(): String {
        return "${value}m"
    }
}