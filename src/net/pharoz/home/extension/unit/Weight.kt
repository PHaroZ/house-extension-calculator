package net.pharoz.home.extension.unit

data class Weight (
    /**
     * in daN
     */
    val value : Double
) {
    operator fun plus(other: Weight) = Weight(this.value + other.value)
    operator fun minus(other: Weight) = Weight(this.value - other.value)

    operator fun times(qu: Int) = Weight(this.value * qu)
    operator fun times(qu: Double) = Weight(this.value * qu)
    operator fun div(qu: Int) = Weight(this.value / qu)
    operator fun div(qu: Double) = Weight(this.value / qu)

    override fun toString(): String {
        return "${value}daN"
    }
}