import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleComponentEvents
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*
import java.awt.Dimension
import java.util.*

object MenuMockup {


    private val TILESET = CP437TilesetResources.rexPaint20x20()
    private val THEME = ColorThemes.arc()
    private var isMapScreen = true

    var mapScreen: Screen? = null
    var inventoryScreen: Screen? = null

    @JvmStatic
    fun main(args: Array<String>) {
        silenceLogs()

        val screenSize = Dimension(1000, 800)
        val columns = screenSize.getWidth() / TILESET.width
        val rows = screenSize.getHeight() / TILESET.height
        val terminalSize = Sizes.create(columns.toInt(), rows.toInt())

        val tileGrid = SwingApplications.startTileGrid(
            AppConfigs.newConfig()
                .withDefaultTileset(TILESET)
                .withSize(terminalSize)
                .build()
        )


        mapScreen = createMapScreen(tileGrid, terminalSize)
        inventoryScreen = createInventoryScreen(tileGrid, terminalSize)


        mapScreen?.display()


        tileGrid.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            when (event.code) {
                KeyCode.KEY_E -> toggleMap()
                else -> Pass
            }
            Processed
        }
    }

    private fun toggleMap() {
        println("Toggling map $isMapScreen")
        isMapScreen = !isMapScreen
        if (isMapScreen) {
            mapScreen?.display()
        } else {
            inventoryScreen?.display()
        }
    }

    private fun createMapScreen(tileGrid: TileGrid, terminalSize: Size): Screen {
        val panelWidth = 25
        val panelHeight = 10

        val screen = Screens.createScreenFor(tileGrid)
        val menuPosition = Positions.create(
            (terminalSize.width - panelWidth) / 2,
            (terminalSize.height - panelHeight) / 2
        )
        val mainLabel = Components.label()
            .withText("Map Screen")
            .withPosition(menuPosition.withRelativeY(-3).withRelativeX(4))
            .build()
        screen.addComponent(mainLabel)

        val menuPanel = Components.panel()
            .withDecorations(box(BoxType.LEFT_RIGHT_DOUBLE))
            .withPosition(menuPosition)
            .withSize(Sizes.create(panelWidth, panelHeight))
            .build()


        screen.addComponent(menuPanel)
        screen.applyColorTheme(THEME)
        return screen
    }

    private fun createInventoryScreen(tileGrid: TileGrid, terminalSize: Size): Screen {
        val panelSpacing = 2
        val screen = Screens.createScreenFor(tileGrid)

        val backButton = Components.button()
            .withText("Back")
            .withPosition(
                Positions.create(
                    panelSpacing,
                    terminalSize.height - panelSpacing * 2
                )
            )
            .build()

        backButton.handleComponentEvents(ComponentEventType.ACTIVATED) {
            toggleMap()
            UIEventResponses.processed()
        }

        screen.addComponent(backButton)


        val difficultyPanel = Components.panel()
            .withSize(Sizes.create((terminalSize.width - panelSpacing) / 3, 9))
            .withPosition(Positions.create(panelSpacing, panelSpacing))
            .withDecorations(box(BoxType.LEFT_RIGHT_DOUBLE, "List"))
            .build()

        val difficultyRadio = Components.radioButtonGroup()
            .withSize(difficultyPanel.size.minus(Sizes.create(2, 2)))
            .build()

        listOf("One", "Two", "Three").forEach { diff -> difficultyRadio.addOption(diff, diff) }

        difficultyPanel.addComponent(difficultyRadio)
        screen.addComponent(difficultyPanel)


        screen.applyColorTheme(THEME)
        return screen
    }

}
