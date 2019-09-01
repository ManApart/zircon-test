import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyCode.*
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed
import org.slf4j.LoggerFactory
import kotlin.random.Random

object AkMapTest {

    private val WORLD_SIZE = Sizes.create(100, 100)
    private val random = Random(5643218)

    private val BLACK = TileColors.fromString("#140c1c")
    private val DARK_BROWN = TileColors.fromString("#442434")
    private val DARK_GREY = TileColors.fromString("#4e4a4e")
    private val BROWN = TileColors.fromString("#854c30")
    private val DARK_GREEN = TileColors.fromString("#346524")
    private val RED = TileColors.fromString("#d04648")
    private val GREY = TileColors.fromString("#757161")
    private val BLUE = TileColors.fromString("#597dce")
    private val LIGHT_GREEN = TileColors.fromString("#6daa2c")
    private val CREAM = TileColors.fromString("#d2aa99")
    private val BRIGHT_GREEN = TileColors.fromString("#deeed6")

    private val PLAYER_TILE = Tiles.newBuilder()
        .withBackgroundColor(ANSITileColor.BLACK)
        .withForegroundColor(ANSITileColor.WHITE)
        .withCharacter('@')
        .buildCharacterTile()

    private val EMPTY = Tiles.empty()
    private val FLOOR = Tiles.defaultTile()
        .withCharacter(Symbols.BLOCK_SPARSE)
        .withBackgroundColor(BROWN)
        .withForegroundColor(CREAM)

    private val WALL_TOP = Tiles.defaultTile()
        .withCharacter(Symbols.BLOCK_SOLID)
        .withForegroundColor(DARK_GREY)

    private val GLASS = Tiles.defaultTile()
        .withCharacter(' ')
        .withBackgroundColor(
            TileColors.create(
                red = BLUE.red,
                green = BLUE.green,
                blue = BLUE.blue,
                alpha = 125
            )
        )

    private val ROOF_TOP = Tiles.defaultTile()
        .withCharacter(Symbols.DOUBLE_LINE_HORIZONTAL)
        .withModifiers(Borders.newBuilder().withBorderColor(BROWN).build())
        .withBackgroundColor(BROWN)
        .withForegroundColor(RED)

    private val GRASS_TILES = listOf(
        Tiles.defaultTile()
            .withCharacter(',')
            .withBackgroundColor(DARK_GREEN)
            .withForegroundColor(LIGHT_GREEN),
        Tiles.defaultTile()
            .withCharacter('.')
            .withBackgroundColor(DARK_GREEN)
            .withForegroundColor(BRIGHT_GREEN),
        Tiles.defaultTile()
            .withCharacter('"')
            .withBackgroundColor(DARK_GREEN)
            .withForegroundColor(LIGHT_GREEN)
    )

    private fun grass() = (GRASS_TILES[random.nextInt(GRASS_TILES.size)].let {
        it.withForegroundColor(it.foregroundColor.darkenByPercent(random.nextDouble(.1)))
            .withBackgroundColor(it.backgroundColor.lightenByPercent(random.nextDouble(.1)))
    })


    private fun wallOutside() = Tiles.defaultTile()
        .withCharacter(Symbols.DOUBLE_LINE_HORIZONTAL_SINGLE_LINE_CROSS)
        .withBackgroundColor(BROWN.darkenByPercent(random.nextDouble(.15)))
        .withForegroundColor(CREAM.lightenByPercent(random.nextDouble(.15)))

    private fun wallInside() = Tiles.defaultTile()
        .withCharacter(Symbols.DOUBLE_LINE_VERTICAL_SINGLE_LINE_CROSS)
        .withBackgroundColor(DARK_GREY.darkenByPercent(random.nextDouble(.25)))
        .withForegroundColor(GREY.lightenByPercent(random.nextDouble(.25)))

    private fun wallFront() = Blocks.newBuilder<Tile>()
        .withLayers(EMPTY)
        .withTop(WALL_TOP)
        .withFront(wallOutside())
        .withBack(wallInside())
        .withEmptyTile(EMPTY)
        .build()


    @JvmStatic
    fun main(args: Array<String>) {
        (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = Level.INFO

        val tileGrid = SwingApplications.startTileGrid()

        addGrass(tileGrid)

        tileGrid.setTileAt(
            Positions.create(30, 10), Tiles.empty().withCharacter('g')
                .withBackgroundColor(GREY)
                .withForegroundColor(BLACK)
        )

        tileGrid.setTileAt(
            Positions.create(32, 10), Tiles.empty().withCharacter('t')
                .withBackgroundColor(GREY)
                .withForegroundColor(BLACK)
        )

        tileGrid.setTileAt(
            Positions.create(32, 9), Tiles.empty().withCharacter(Symbols.DOUBLE_LINE_VERTICAL)
                .withBackgroundColor(GREY)
                .withForegroundColor(BLACK)
        )

        val player = Layers.newBuilder()
            .withSize(Sizes.one())
            .withOffset(Positions.create(33, 12))
            .build()
            .fill(PLAYER_TILE)

        tileGrid.pushLayer(player)


        addHouse(tileGrid, Positions.create(5, 5), Sizes.create(12, 8))
        addHouse(tileGrid, Positions.create(40, 0), Sizes.create(9, 8))
        addHouse(tileGrid, Positions.create(25, 20), Sizes.create(14, 6))


        tileGrid.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            println(player.position)
            //            player.position
            when (event.code) {
                UP -> player.moveUpBy(1)
                DOWN -> player.moveDownBy(1)
                LEFT -> player.moveLeftBy(1)
                RIGHT -> player.moveRightBy(1)
                else -> Pass
            }
            Processed
        }
    }

    private fun addHouse(tileGrid: TileGrid, topLeft: Position, size: Size) {
        require(size.width > 2)
        require(size.height > 2)

        val bottomLeft = topLeft.withRelativeY(size.height - 1)
        val bottomRight = bottomLeft.withRelativeX(size.width - 1)
        val topRight = topLeft.withRelativeX(size.width - 1)

        Shapes.buildFilledRectangle(topLeft, size).positions().forEach {
            tileGrid.setTileAt(it, FLOOR)
        }
            Shapes.buildLine(bottomLeft, bottomRight).positions().forEach { pos ->
                tileGrid.setTileAt(pos, WALL_TOP)
            }
            Shapes.buildLine(topLeft, topRight).positions().forEach { pos ->
                tileGrid.setTileAt(pos, WALL_TOP)
            }
            Shapes.buildLine(topLeft.withRelativeY(1), bottomLeft.withRelativeY(-1)).positions().forEach { pos ->
                tileGrid.setTileAt(pos, WALL_TOP)
            }
            Shapes.buildLine(topRight.withRelativeY(1), bottomRight.withRelativeY(-1)).positions().forEach { pos ->
                tileGrid.setTileAt(pos, WALL_TOP)
            }
        val doorPos = topLeft.withRelativeY(size.height - 1).withRelativeX((size.width - 1) / 2)
        tileGrid.setTileAt(doorPos, FLOOR)
        tileGrid.setTileAt(doorPos, EMPTY)
        if (size.width > 8) {
            val frontWindowPositions = listOf(
                doorPos.withRelativeX(-2),
                doorPos.withRelativeX(-2),
                doorPos.withRelativeX(-3),
                doorPos.withRelativeX(-3),
                doorPos.withRelativeX(2),
                doorPos.withRelativeX(2),
                doorPos.withRelativeX(3),
                doorPos.withRelativeX(3)
            )
            val backWindowPositions = frontWindowPositions.map { it.withRelativeY(-size.height + 1) }
            frontWindowPositions.forEach {
                tileGrid.setTileAt(it, GLASS)
            }
            backWindowPositions.forEach {
                tileGrid.setTileAt(it, GLASS)
            }
        }
    }

    private fun addGrass(tileGrid: TileGrid) {
        tileGrid.size.fetchPositions().forEach { pos ->
            tileGrid.setTileAt(pos, grass())
        }
    }

}