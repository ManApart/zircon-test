
import org.hexworks.zircon.api.*


fun main(args: Array<String>) {
    val tileGrid = SwingApplications.startTileGrid(
        AppConfigs.newConfig()
            .withSize(Sizes.create(20, 8))
            .withDefaultTileset(CP437TilesetResources.wanderlust16x16())
            .build()
    )

    val screen = Screens.createScreenFor(tileGrid)

    val theme = ColorThemes.adriftInDreams()

    val image = DrawSurfaces.tileGraphicsBuilder()
        .withSize(tileGrid.size)
        .build()
        .fill(
            Tiles.newBuilder()
                .withForegroundColor(theme.primaryForegroundColor)
                .withBackgroundColor(theme.primaryBackgroundColor)
                .withCharacter('~')
                .build()
        )

    screen.draw(image, Positions.zero())

    screen.display()
}