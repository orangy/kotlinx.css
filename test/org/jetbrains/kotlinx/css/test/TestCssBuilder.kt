package org.jetbrains.kotlinx.css.test

import org.jetbrains.kotlinx.css.*
import org.junit.*
import kotlin.test.*

class TestCssBuilder() {
    @Test fun usingTag() {
        val css = renderCSS {
            a { placeholder() }
        }
        assertEquals("a {\n}\n\n", css)
    }

    @Test fun usingTagAndId() {
        val css = renderCSS {
            a.id("top") { placeholder() }
        }
        assertEquals("a#top {\n}\n\n", css)
    }

    @Test fun usingId() {
        val css = renderCSS {
            id("top") { placeholder() }
        }
        assertEquals("#top {\n}\n\n", css)
    }

    @Test fun usingStyle() {
        val css = renderCSS {
            style("top") { placeholder() }
        }
        assertEquals(".top {\n}\n\n", css)
    }

    @Test fun usingStyleAndId() {
        val css = renderCSS {
            style("top").id("main") { placeholder() }
        }
        assertEquals(".top#main {\n}\n\n", css)
    }

    @Test fun usingTagStyleAndId() {
        val css = renderCSS {
            div.style("top").id("main") { placeholder() }
        }
        assertEquals("div.top#main {\n}\n\n", css)
    }

    @Test fun usingMedia() {
        val css = renderCSS {
            div {
                media("min-width:1200px") { placeholder() }
            }
        }
        assertEquals("@media (min-width:1200px) {\ndiv {\n}\n\n}\n\n", css)
    }

    @Test fun usingPseudoSelector() {
        val css = renderCSS {
            a.select("hover") {
                placeholder()
            }
        }
        assertEquals("a:hover {\n}\n\n", css)
    }

    @Test fun usingNotPseudoSelector() {
        val css = renderCSS {
            a.not.attribute("href") {
                placeholder()
            }
        }
        assertEquals("a:not([href]) {\n}\n\n", css)
    }
}