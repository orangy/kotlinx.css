package org.jetbrains.kotlinx.css

import kotlin.reflect.*

operator fun <T : ElementProperty> T.invoke(configure: T.() -> Unit): T = apply(configure)
operator fun <T : ElementProperty> ((String, Style) -> T).getValue(instance: ElementProperty, property: KProperty<*>): T = instance.named(property.name, this)
operator fun <T : ElementProperty> ((String, Style) -> T).getValue(instance: Style, property: KProperty<*>): T = invoke(property.name, instance)
operator fun <T : ElementProperty> T.getValue(instance: ElementProperty, property: KProperty<*>): T = this

open class ElementProperty(private val _name: String, private val _style: Style) {
    operator fun invoke(value: String) = _style.property(_name, value)
    fun <T : ElementProperty> named(name: String, constructor: (String, Style) -> T): T = constructor("$_name-$name", _style)
}

open class TextDecorationElementProperty(name: String, style: Style) : ElementProperty(name, style) {
    val line by ::ElementProperty
    val color by ::ElementProperty
    val style by ::ElementProperty
}

open class TextProperty(name: String, style: Style) : ElementProperty(name, style) {
    val decoration by ::TextDecorationElementProperty
    val shadow by ::ElementProperty
    val align by ::ElementProperty
    val overflow by ::ElementProperty
    val orientation by ::ElementProperty
}

open class BorderElementProperty(name: String, style: Style) : ElementProperty(name, style) {
    val width by ::ElementProperty
    val color by ::ElementProperty
    val style by ::ElementProperty
}

open class OutlineProperty(name: String, style: Style) : BorderElementProperty(name, style) {
    val offset by ::ElementProperty
}

class BorderProperty(name: String, style: Style) : BorderElementProperty(name, style) {
    val bottom by ::BorderElementProperty
    val left by ::BorderElementProperty
    val right by ::BorderElementProperty
    val top  by ::BorderElementProperty

    val collapse by ::ElementProperty
    val spacing by ::ElementProperty
    val image by ::BorderImageProperty
    val radius by ::RadiusProperty
}

class BorderImageProperty(name: String, style: Style) : ElementProperty(name, style) {
    val outset by ::ElementProperty
    val slice by ::ElementProperty
    val repeat by ::ElementProperty
    val source by ::ElementProperty
    val width by ::ElementProperty
}

class BoxProperty(name: String, style: Style) : ElementProperty(name, style) {
    val decorationBreak by named("decoration-break", ::ElementProperty)
    val shadow by ::ElementProperty
    val sizing by ::ElementProperty
}

class RadiusProperty(name: String, style: Style) : BorderElementProperty(name, style) {
    val bottomLeft by named("bottom-left", ::ElementProperty)
    val bottomRight by named("bottom-right", ::ElementProperty)
    val topLeft by named("top-left", ::ElementProperty)
    val topRight by named("top-right", ::ElementProperty)
}

class SidesProperty(name: String, style: Style) : ElementProperty(name, style) {
    val bottom by ::ElementProperty
    val left by ::ElementProperty
    val right by ::ElementProperty
    val top  by ::ElementProperty
}

class CaretProperty(name: String, style: Style) : ElementProperty(name, style) {
    val animation by ::ElementProperty
    val color by ::ElementProperty
    val shape by ::ElementProperty
}

open class BackgroundProperty(name: String, style: Style) : ElementProperty(name, style) {
    val attachment by ::ElementProperty
    val blendMode by named("blend-mode", ::ElementProperty)
    val clip by ::ElementProperty
    val color by ::ElementProperty
    val image by ::ElementProperty
    val origin by ::ElementProperty
    val position by ::ElementProperty
    val repeat by ::ElementProperty
    val size by ::ElementProperty
}

open class FontProperty(name: String, style: Style) : ElementProperty(name, style) {
    val family by ::ElementProperty
    val featureSettings by named("feature-settings", ::ElementProperty)
    val kerning by ::ElementProperty
    val languageOverride by named("language-override", ::ElementProperty)
    val size by ::ElementProperty
    val sizeAdjust by named("size-adjust", ::ElementProperty)
    val stretch by ::ElementProperty
    val style by ::ElementProperty
    val synthesis by ::ElementProperty
    val variant by ::ElementProperty
    val weight by ::ElementProperty
}

val Style.border by ::BorderProperty
val Style.box by ::BoxProperty
val Style.outline by ::OutlineProperty
val Style.padding by ::SidesProperty
val Style.margin by ::SidesProperty
val Style.background by ::BackgroundProperty

val Style.position by ::ElementProperty
val Style.top by ::ElementProperty
val Style.left by ::ElementProperty
val Style.right by ::ElementProperty
val Style.bottom by ::ElementProperty
val Style.clip by ::ElementProperty
val Style.clear by ::ElementProperty
val Style.display by ::ElementProperty
val Style.float by ::ElementProperty
val Style.width by ::ElementProperty
val Style.height by ::ElementProperty
val Style.overflow by ::ElementProperty
val Style.visibility by ::ElementProperty

val Style.color by ::ElementProperty
val Style.opacity by ::ElementProperty
val Style.text by ::TextProperty
val Style.font by ::FontProperty
val Style.content by ::ElementProperty
val Style.cursor by ::ElementProperty
val Style.caret by ::CaretProperty

// Unsorted

val Style.min_width: ElementProperty get() = ElementProperty("min-width", this)
val Style.max_width: ElementProperty get() = ElementProperty("max-width", this)
val Style.min_height: ElementProperty get() = ElementProperty("min-height", this)
val Style.max_height: ElementProperty get() = ElementProperty("max-height", this)

val Style.line_height: ElementProperty get() = ElementProperty("line-height", this)
val Style.vertical_align: ElementProperty get() = ElementProperty("vertical-align", this)
val Style.white_space: ElementProperty get() = ElementProperty("white-space", this)
val Style.list_style: ElementProperty get() = ElementProperty("list-style", this)
val Style.z_index: ElementProperty get() = ElementProperty("z-index", this)


