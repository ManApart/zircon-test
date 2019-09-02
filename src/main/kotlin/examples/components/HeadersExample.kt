package org.hexworks.zircon.examples.components

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.positionalAlignment
import org.hexworks.zircon.api.extensions.shadow
import org.hexworks.zircon.api.graphics.BoxType

object HeadersExample {

    private val theme = ColorThemes.headache()
    private val tileset = CP437TilesetResources.runeset24x24()

    @JvmStatic
    fun main(args: Array<String>) {

        val tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withDefaultTileset(tileset)
                .withSize(Sizes.create(60, 30))
                .build())

        val screen = Screens.createScreenFor(tileGrid)

        val panel = Components.panel()
                .withDecorations(box())
                .withSize(28, 28)
                .withAlignment(positionalAlignment(31, 1))
                .build()
        screen.addComponent(panel)

        val simpleHeader = Components.header()
                .withText("Some header")
                .withAlignment(positionalAlignment(2, 2))

        screen.addComponent(simpleHeader)
        panel.addComponent(simpleHeader)

        val decoratedLabel = Components.label()
                .withText("Some label")
                .withDecorations(box(boxType = BoxType.DOUBLE), shadow())
                .withAlignment(positionalAlignment(2, 4))

        screen.addComponent(decoratedLabel)
        panel.addComponent(decoratedLabel)

        val shadowedHeader = Components.header()
                .withText("Some header")
                .withDecorations(shadow())
                .withAlignment(positionalAlignment(2, 9))

        screen.addComponent(shadowedHeader)
        panel.addComponent(shadowedHeader)

        val tooLongHeader = Components.header()
                .withText("Too long header")
                .withSize(Sizes.create(10, 1))
                .withAlignment(positionalAlignment(2, 13))

        screen.addComponent(tooLongHeader)
        panel.addComponent(tooLongHeader)

        val overTheTopHeader = Components.header()
                .withText("WTF header")
                .withDecorations(
                        box(boxType = BoxType.DOUBLE),
                        box(boxType = BoxType.SINGLE),
                        box(boxType = BoxType.LEFT_RIGHT_DOUBLE),
                        shadow())
                .withAlignment(positionalAlignment(2, 16))

        screen.addComponent(overTheTopHeader)
        panel.addComponent(overTheTopHeader)


        screen.display()
        screen.applyColorTheme(theme)
    }

}
