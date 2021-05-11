package org.hexworks.zircon.examples

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.SwingApplications

object CustomPanelExample {

//    class CustomPanel(title: String,
//                      componentMetadata: ComponentMetadata) : DefaultPanel(
//            componentMetadata = componentMetadata,
//            title = title,
//            renderingStrategy = DefaultComponentRenderingStrategy(
//                    decorationRenderers = listOf(BoxDecorationRenderer(
//                            title = title)),
//                    componentRenderer = DefaultPanelRenderer()))

    @JvmStatic
    fun main(args: Array<String>) {

        val tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withSize(Sizes.create(60, 30))
                .enableBetaFeatures()
                .build())

        val screen = Screens.createScreenFor(tileGrid)

//        screen.addComponent(CustomPanel(
//                title = "Whatever",
//                componentMetadata = ComponentMetadata(
//                        position = Positions.zero(),
//                        size = Sizes.create(20, 10),
//                        tileset = CP437TilesetResources.acorn8X16(),
//                        componentStyleSet = ComponentStyleSet.defaultStyleSet())))

        screen.display()
        screen.applyColorTheme(ColorThemes.cyberpunk())

    }

}
