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
        propertyHolder(title, ph)
    }
}

fun FlowContent.propertyHolder(title: String, ph: PropertyHolder, deepLvl: Int = 1) {
    val properties = ph::class.declaredMemberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .map { it as KProperty1<PropertyHolder, Any> }
    details {
        this.attributes["style"] = "margin-left:${(deepLvl - 1) * 20}px"
        summary { +title }
        table {
            this.attributes["border"] = "1"
            this.attributes["cellspacing"] = "0"
            properties
                .filter { (it.returnType.classifier as KClass<*>).isSubclassOf(Number::class) }
                .forEach { prop ->
                    tr {
                        td {
                            +prop.description()
                        }
                        td {
                            +prop.get(ph).toString()
                        }
                    }
                }
            properties
                .filter { (it.returnType.classifier as KClass<*>).isSubclassOf(QuantityWithUnit::class) }
                .forEach { prop ->
                    tr {
                        td {
                            +prop.description()
                        }
                        td {
                            val quantityWithUnit = prop.get(ph) as QuantityWithUnit
                            +"%.2f %s".format(quantityWithUnit.value, quantityWithUnit.unitName)
                        }
                    }
                }
        }
        properties
            .filter { (it.returnType.classifier as KClass<*>).isSubclassOf(PropertyHolder::class) }
            .forEach { prop ->
                propertyHolder(prop.description(), prop.get(ph) as PropertyHolder, deepLvl + 1)
            }
    }
}

/**
 * extract the value of a Description annotation or return the name of the passed property
 */
fun KProperty1<*, Any>.description() =
    findAnnotation<Description>()?.value?.takeIf(String::isNotBlank) ?: name

@HtmlTagMarker
fun DETAILS.summary(classes: String? = null, block: SUMMARY.() -> Unit = {}): Unit =
    SUMMARY(attributesMapOf("class", classes), consumer).visit(block)

open class SUMMARY(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("summary", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag {

}
