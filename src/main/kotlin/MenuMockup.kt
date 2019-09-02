import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleComponentEvents
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*
import java.awt.Dimension

object MenuMockup {


    private val TILESET = CP437TilesetResources.rexPaint20x20()
    private val THEME = ColorThemes.arc()
    private val ALT_THEME = ColorThemes.war()
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


//        mapScreen?.display()
        inventoryScreen?.display()


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

        val panelSize = Sizes.create(terminalSize.width / 3 - panelSpacing, 9)
        val verticalOffset = panelSize.height + panelSpacing * 2
        val thirdOffset = terminalSize.width / 3
        val fourthOffset = terminalSize.width / 6
        val center = terminalSize.width / 2 - panelSize.width / 2


        createPanel("Helmet", Positions.create(center, panelSpacing), panelSize, screen)
        createPanel("Chest", Positions.create(center, verticalOffset), panelSize, screen)
        createPanel("Left Arm", Positions.create(center - thirdOffset, verticalOffset), panelSize, screen)
        createPanel("Right Arm", Positions.create(center + thirdOffset, verticalOffset), panelSize, screen)
        createPanel("Left Leg", Positions.create(center - fourthOffset, 2 * verticalOffset), panelSize, screen)
        createPanel("Right Leg", Positions.create(center + fourthOffset, 2 * verticalOffset), panelSize, screen)


        screen.applyColorTheme(THEME)
        return screen
    }

    private fun createPanel(name: String, position: Position, size: Size, screen: Screen) {
        val panel = Components.panel()
            .withSize(size)
            .withPosition(position)
            .withDecorations(box(BoxType.LEFT_RIGHT_DOUBLE, name))
            .build()


        val health = Components.label().withText("Health: 9/10")
            .withPosition(Position.zero())
            .build()
        panel.addComponent(health)

        val armor = Components.label()
            .withText("Armor: 2")
            .withPosition(Positions.create(0, 1))
            .build()
        panel.addComponent(armor)

        val speed = Components.label()
            .withText("Speed: 1")
            .withPosition(Positions.create(0, 2))
            .build()
        panel.addComponent(speed)

        val weight = Components.label()
            .withText("Weight: 3")
            .withPosition(Positions.create(0, 3))
            .build()
        panel.addComponent(weight)

        val handler = MousePanelHandler(panel, name)
        panel.handleMouseEvents(MouseEventType.MOUSE_CLICKED, handler)
        panel.handleMouseEvents(MouseEventType.MOUSE_ENTERED, handler)
        panel.handleMouseEvents(MouseEventType.MOUSE_EXITED, handler)

        screen.addComponent(panel)
    }

    private class MousePanelHandler(val panel: Panel, val name: String) : MouseEventHandler {
        override fun handle(event: MouseEvent, phase: UIEventPhase): UIEventResponse {
            when (event.type) {
                MouseEventType.MOUSE_ENTERED -> panel.applyColorTheme(ALT_THEME)
                MouseEventType.MOUSE_EXITED-> panel.applyColorTheme(THEME)
                MouseEventType.MOUSE_CLICKED-> println("Clicked panel for $name")
                else -> Pass
            }
            return UIEventResponses.processed()
        }

    }

}
