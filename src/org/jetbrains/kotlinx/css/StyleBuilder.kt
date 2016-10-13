package org.jetbrains.kotlinx.css

import java.util.*

data class StyleProperty(val name: String, val value: String)

class StyleContainer {
    var combinator: String = ""

    val styles: MutableList<Style> = ArrayList<Style>()
    fun render(builder: StringBuilder, outerSelector: String) {
        for (style in styles) {
            if (combinator.startsWith("@media")) {
                builder.append("$combinator {\n")
                style.render(builder, outerSelector)
                builder.append("}\n\n")
            } else if (combinator == "!") {
                style.render(builder, "$outerSelector:not(", ")")
            } else {
                style.render(builder, "$outerSelector$combinator")
            }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }

    fun invoke(body: StyleContainer.() -> Unit): StyleContainer {
        body()
        return this
    }
}

class Style {
    var selector: String = "*"

    val properties: MutableList<StyleProperty> = ArrayList<StyleProperty>()
    val containers: MutableList<StyleContainer> = ArrayList<StyleContainer>()
    operator fun invoke(body: Style.() -> Unit): Style {
        body()
        return this
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }

    fun properties(): String {
        val builder = StringBuilder()
        for ((name, value) in properties) {
            builder.append("  $name: $value;\n")
        }
        return builder.toString()
    }

    fun render(builder: StringBuilder, outerSelector: String, tail: String = "") {
        if (properties.size > 0) {
            builder.append("$outerSelector$selector$tail {\n")
            for ((name, value) in properties) {
                builder.append("  $name: $value;\n")
            }
            builder.append("}\n\n")
        }
        for (container in containers) {
            container.render(builder, "$outerSelector$selector")
        }
    }
}

fun Style.property(name: String, value: String) {
    properties.add(StyleProperty(name, value))
}

val StyleContainer.any: Style get() = tag("*")

fun StyleContainer.tag(name: String, body: Style.() -> Unit): Style = tag(name).invoke(body)
fun StyleContainer.tag(name: String): Style {
    val style = Style()
    styles.add(style)
    style.selector = name
    return style
}

fun StyleContainer.select(state: String): Style {
    val style = Style()
    style.selector = ":" + state
    styles.add(style)
    return style
}

fun StyleContainer.select(state: String, body: Style.() -> Unit): Style = select(state).invoke(body)
fun Style.select(state: String, body: Style.() -> Unit): Style = select(state).invoke(body)
fun Style.select(state: String): Style {
    val container = StyleContainer()
    container.combinator = ""
    containers.add(container)
    val style = Style()
    style.selector = ":" + state
    container.styles.add(style)
    return style
}

enum class AttributeOperation {
    equals,
    contains,
    startsWith,
    endsWith
}

fun Style.attribute(name: String, value: String = "", operation: AttributeOperation = AttributeOperation.equals, body: Style.() -> Unit): Style = attribute(name, value, operation).invoke(body)
fun Style.attribute(name: String, value: String = "", operation: AttributeOperation = AttributeOperation.equals): Style {
    val container = StyleContainer()
    container.combinator = ""
    containers.add(container)
    val style = Style()
    style.selector = if (value == "") {
        "[$name]"
    } else when (operation) {
        AttributeOperation.equals -> "[$name=$value]"
        AttributeOperation.contains -> "[$name*=$value]"
        AttributeOperation.startsWith -> "[$name^=$value]"
        AttributeOperation.endsWith -> "[$name$=$value]"
    }
    container.styles.add(style)
    return style
}

fun StyleContainer.attribute(name: String, body: Style.() -> Unit): Style = attribute(name).invoke(body)
fun StyleContainer.attribute(name: String): Style {
    val style = Style()
    style.selector = "[$name]"
    styles.add(style)
    return style
}

fun Style.media(query: String, body: Style.() -> Unit): Style = media(query).invoke(body)
fun Style.media(query: String): Style {
    val container = StyleContainer()
    container.combinator = "@media ($query)"
    containers.add(container)
    val style = Style()
    style.selector = ""
    container.styles.add(style)
    return style
}

val Style.not: StyleContainer
    get() {
        val container = StyleContainer()
        container.combinator = "!"
        containers.add(container)
        return container
    }

val Style.or: StyleContainer
    get() {
        val container = StyleContainer()
        container.combinator = ","
        containers.add(container)
        return container
    }

fun StyleContainer.id(name: String, body: Style.() -> Unit): Style = id(name).invoke(body)
fun StyleContainer.id(name: String): Style {
    val style = Style().id(name)
    styles.add(style)
    return style
}

fun Style.id(name: String, body: Style.() -> Unit): Style = id(name).invoke(body)
fun Style.id(name: String): Style {
    if (selector == "*")
        selector = "#$name"
    else
        selector += "#$name"
    return this
}

fun StyleContainer.style(name: String, body: Style.() -> Unit): Style = style(name).invoke(body)
fun StyleContainer.style(name: String): Style {
    val style = Style().style(name)
    styles.add(style)
    return style
}

fun Style.style(name: String, body: Style.() -> Unit): Style = style(name).invoke(body)
fun Style.style(name: String): Style {
    val container = StyleContainer()
    container.combinator = ""
    val style = Style()
    style.selector = ".$name"
    containers.add(container)
    container.styles.add(style)
    return style
}

fun StyleContainer.immediate(body: StyleContainer.() -> Unit): StyleContainer = next.invoke(body)
val StyleContainer.immediate: StyleContainer
    get() = StyleContainer().apply container@ {
        combinator = ">"
        this@immediate.styles.add(Style().apply { containers.add(this@container) })
    }

fun Style.immediate(body: StyleContainer.() -> Unit): StyleContainer = immediate.invoke(body)
val Style.immediate: StyleContainer
    get() = StyleContainer().apply {
        combinator = ">"
        containers.add(this)
    }

fun StyleContainer.next(body: StyleContainer.() -> Unit): StyleContainer = next.invoke(body)
val StyleContainer.next: StyleContainer
    get() = StyleContainer().apply container@ {
        combinator = "+"
        this@next.styles.add(Style().apply { containers.add(this@container) })
    }

fun Style.next(body: StyleContainer.() -> Unit): StyleContainer = next.invoke(body)
val Style.next: StyleContainer
    get() = StyleContainer().apply {
        combinator = "+"
        containers.add(this)
    }

fun Style.after(body: StyleContainer.() -> Unit): StyleContainer = after.invoke(body)
val Style.after: StyleContainer
    get() = StyleContainer().apply {
        combinator = "~"
        containers.add(this)
    }

fun Style.plus(body: StyleContainer.() -> Unit): StyleContainer = nested.invoke(body)
fun Style.plus(): StyleContainer = nested

fun Style.nested(body: StyleContainer.() -> Unit): StyleContainer = nested.invoke(body)
val Style.nested: StyleContainer
    get() = StyleContainer().apply {
        combinator = " "
        containers.add(this)
    }



