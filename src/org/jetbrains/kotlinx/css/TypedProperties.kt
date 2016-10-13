package org.jetbrains.kotlinx.css

fun Style.gradientVertical(color1: String, color2: String): Unit {
    background(color1)
    background("-moz-linear-gradient(top,  $color1 0%, $color2 100%); /* FF3.6+ */")
    background("-webkit-gradient(linear, left top, left bottom, color-stop(0%,$color1), color-stop(100%,$color2)); /* Chrome,Safari4+ */")
    background("-webkit-linear-gradient(top,  $color1 0%,$color2 100%); /* Chrome10+,Safari5.1+ */")
    background("-o-linear-gradient(top,  $color1 0%,$color2 100%); /* Opera 11.10+ */")
    background("-ms-linear-gradient(top,  $color1 0%,$color2 100%); /* IE10+ */")
    background("linear-gradient(to bottom,  $color1 0%,$color2 100%); /* W3C */")
}

fun Style.roundBorder(width: String, color: Color, radius: String): Unit {
    border("$width solid $color")
    border.radius(radius)
}

fun Style.shadow(vararg shadows: String): Unit {
    val value = shadows.joinToString(", ")
    property("box-shadow", value)
    property("-moz-box-shadow", value)
    property("-webkit-box-shadow", value)
}

@Suppress("unused")
fun Style.outset(color: Color, hshift: Int = 0, vshift: Int = 1, blur: Int = 0, strength: Int = 0): String {
    return "${hshift}px ${vshift}px ${blur}px ${strength}px $color"
}

@Suppress("unused")
fun Style.inset(color: Color, hshift: Int = 0, vshift: Int = 1, blur: Int = 0, strength: Int = 0): String {
    return "${hshift}px ${vshift}px ${blur}px ${strength}px $color inset"
}
