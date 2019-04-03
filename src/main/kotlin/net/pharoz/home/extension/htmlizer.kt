package net.pharoz.home.extension

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import net.pharoz.home.extension.compute.PropertyHolder
import net.pharoz.home.extension.unit.QuantityWithUnit
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

fun <O : Appendable> O.htmlize(title: String, ph: PropertyHolder) = this.appendHTML().html {
    body {
        div {
            propertyHolder(title, ph)
        }
    }
}


fun FlowContent.propertyHolder(title: String, ph: PropertyHolder) {
    val properties = ph::class.declaredMemberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .map { Pair(it, it.findAnnotation<Description>()) }
        .filter { it.second != null }
        .map { it as Pair<KProperty1<PropertyHolder, Any>, Description> }
    h1 { +title }
    table {
        properties
            .filter { (it.first.returnType.classifier as KClass<*>).isSubclassOf(QuantityWithUnit::class) }
            .forEach { (prop, description) ->
                tr {
                    td {
                        +description.valueOrDefault(prop)
                    }
                    td {
                        val quantityWithUnit = prop.get(ph) as QuantityWithUnit
                        +"%.2f %s".format(quantityWithUnit.value, quantityWithUnit.unitName)
                    }
                }
            }
    }
    properties
        .filter { (it.first.returnType.classifier as KClass<*>).isSubclassOf(PropertyHolder::class) }
        .forEach { (prop, description) ->
            div {
                propertyHolder(description.valueOrDefault(prop), prop.get(ph) as PropertyHolder)
            }
        }
}

/**
 * extract the value of a Description annotation or return the name of the passed property
 */
fun Description.valueOrDefault(prop: KProperty1<PropertyHolder, Any>) = value.takeIf(String::isNotBlank) ?: prop.name
