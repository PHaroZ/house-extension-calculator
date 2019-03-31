package net.pharoz.home.extension.unit

data class Volume (
    /**
     * in meter*meter*meter
     */
    val value : Double
) {
    operator fun plus(other: Volume) = Volume(this.value + other.value)
    operator fun minus(other: Volume) = Volume(this.value - other.value)

    operator fun times(qu: Int) = Volume(this.value * qu)
    operator fun times(qu: Double) = Volume(this.value * qu)
    operator fun div(qu: Int) = Volume(this.value / qu)
    operator fun div(qu: Double) = Volume(this.value / qu)

    override fun toString(): String {
        return "${value}m3"
    }
}