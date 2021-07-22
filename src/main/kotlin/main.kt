import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.WatchKey
import java.nio.file.WatchService

fun main() {

    val filePath = File("files","sample.txt")
    val watchedDirPath: Path = filePath.parentFile.toPath()
    val watchService: WatchService = FileSystems.getDefault().newWatchService()

    watchedDirPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)

    while (true) {
        println("\nWatching...")

        val monitorKey: WatchKey = watchService.take()
        val dirPath = monitorKey.watchable() as? Path ?: break

        monitorKey.pollEvents().forEach { watchEvent ->
            val eventPath = dirPath.resolve(watchEvent.context() as Path)

            if(eventPath.toFile().absolutePath != filePath.absolutePath) {
                return@forEach
            }

            if (watchEvent.kind() == ENTRY_MODIFY) {
                println("Modified: ${eventPath.fileName}")
            }

            monitorKey.reset()
        }
    }
}
