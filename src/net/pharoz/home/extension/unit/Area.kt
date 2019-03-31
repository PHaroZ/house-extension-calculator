package net.pharoz.home.extension.unit

data class Area (
    /**
     * in meter*meter
     */
    val value : Double
) {
    operator fun plus(other: Area) = Area(this.value + other.value)
    operator fun minus(other: Area) = Area(this.value - other.value)
    operator fun times(other: Size) = Volume(this.value * other.value)

    operator fun times(qu: Int) = Area(this.value * qu)
    operator fun times(qu: Double) = Area(this.value * qu)
    operator fun div(qu: Int) = Area(this.value / qu)
    operator fun div(qu: Double) = Area(this.value / qu)


    override fun toString(): String {
        return "${value}m2"
    }
}