package net.pharoz.home.extension

import java.lang.annotation.Inherited

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Description(val value:String = "")