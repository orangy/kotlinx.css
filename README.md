[![](https://jitpack.io/v/orangy/kotlinx.css.svg)](https://jitpack.io/#orangy/kotlinx.css)

# kotlinx.css

DSL for building CSS in Kotlin

# Example

```kotlin
style("area") {
    margin.left("auto")
    margin.right("auto")
    display("block")
    max_width("100%")

    for (size in 1..12) {
        style("inset-left-$size") { padding.left("${size * 8}px") }
        style("inset-right-$size") { padding.right("${size * 8}px") }
        style("inset-top-$size") { padding.top("${size * 8}px") }
        style("inset-bottom-$size") { padding.bottom("${size * 8}px") }
        style("inset-all-$size") { padding("${size * 8}px") }
    }

    for (size in 1..12) {
        style("width-$size") { width("${size * 32}px") }
    }
    for (size in 1..12) {
        style("height-$size") { height("${size * 32}px") }
    }

    style("display-inline") {
        display("inline-block")
    }
    style("display-cell") {
        display("table-cell")
    }

}

```

