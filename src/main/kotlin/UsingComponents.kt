import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.uievent.ComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventHandler
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.UIEventResponse
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level;


fun main(args: Array<String>) {
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = Level.INFO

    val tileGrid = SwingApplications.startTileGrid(
        AppConfigs.newConfig()
            .withSize(Sizes.create(34, 18))
            .withDefaultTileset(CP437TilesetResources.aduDhabi16x16())
            .build()
    )
    val screen = Screens.createScreenFor(tileGrid)


    val panel = Components.panel()
//            .wrapWithBox(true) // panels can be wrapped in a box
//            .withTitle("Panel") // if a panel is wrapped in a box a title can be displayed
//            .wrapWithShadow(true) // shadow can be added
        .withSize(Sizes.create(32, 16)) // the size must be smaller than the parent's size
        .withPosition(Positions.offset1x1())
        .build() // position is always relative to the parent

    val header = Components.header()
        // this will be 1x1 left and down from the top left
        // corner of the panel
        .withPosition(Positions.offset1x1())
        .withText("Header")
        .build()

    val checkBox = Components.checkBox()
        .withText("Check me!")
        .withPosition(
            Positions.create(0, 1)
                // the position class has some convenience methods
                // for you to specify your component's position as
                // relative to another one
                .relativeToBottomOf(header)
        )
        .build()

    val left = Components.button()
        .withPosition(
            Positions.create(0, 1) // this means 1 row below the check box
                .relativeToBottomOf(checkBox)
        )
        .withText("Left")
        .build()

    val right = Components.button()
        .withPosition(
            Positions.create(1, 0) // 1 column right relative to the left BUTTON
                .relativeToRightOf(left)
        )
        .withText("Right")
        .build()

    panel.addComponent(header)
    panel.addComponent(checkBox)
    panel.addComponent(left)
    panel.addComponent(right)

    screen.addComponent(panel)

    // we can apply color themes to a screen
    screen.applyColorTheme(ColorThemes.monokaiBlue())

    // this is how you can define interactions with a component
    left.handleComponentEvents(ComponentEventType.ACTIVATED, object : ComponentEventHandler {
        override fun handle(event: ComponentEvent): UIEventResponse {
            screen.applyColorTheme(ColorThemes.monokaiGreen())
            return UIEventResponses.processed()
        }
    })

    right.handleComponentEvents(ComponentEventType.ACTIVATED, object : ComponentEventHandler {
        override fun handle(event: ComponentEvent): UIEventResponse {
            screen.applyColorTheme(ColorThemes.monokaiViolet())
            return UIEventResponses.processed()
        }
    })


    // in order to see the changes you need to display your screen.
    screen.display()
}