
import org.hexworks.zircon.api.CharacterTileStrings
import org.hexworks.zircon.api.ImageDictionaryTilesetResources
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.Tiles

object ImageTileExample {

    private val imageDictionary = ImageDictionaryTilesetResources.loadTilesetFromFilesystem("./src/main/resources/image_dictionary")

    @JvmStatic
    fun main(args: Array<String>) {

        val tileGrid = SwingApplications.startTileGrid()

        tileGrid.draw(CharacterTileStrings.newBuilder()
            .withText("You can see an image tile below...")
            .build())


        val imageTile = Tiles.newBuilder()
            .withTileset(imageDictionary)
            .withName("")
            .buildImageTile()

        // TODO: fix positioning

        tileGrid.setTileAt(Positions.create(30, 20), imageTile)

    }

}