
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.hexworks.zircon.api.*
import org.slf4j.LoggerFactory


fun main(args: Array<String>) {
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = Level.INFO
    
    val tileGrid = SwingApplications.startTileGrid(
        AppConfigs.newConfig()
            .withSize(Sizes.create(20, 8))
//            .withDefaultTileset(CP437TilesetResources.wanderlust16x16())
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