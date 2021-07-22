import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.*

class FileWatcher {

    val channel = Channel<String>(BUFFERED)

    fun start(scope: CoroutineScope) {

        val filePath = File("files","sample.txt")
        val watchedDirPath: Path = filePath.parentFile.toPath()
        val watchService: WatchService = FileSystems.getDefault().newWatchService()

        watchedDirPath.register(watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_DELETE
        )
        scope.launch {
            while (true) {
                channel.send("\nWatching...")

                val monitorKey: WatchKey = watchService.take()

                val dirPath = monitorKey.watchable() as? Path ?: break

                monitorKey.pollEvents().forEach { watchEvent ->
                    val eventPath = dirPath.resolve(watchEvent.context() as Path)

                    if (eventPath.toFile().absolutePath != filePath.absolutePath) {
                        return@forEach
                    }

                    if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        channel.send("Modified: ${eventPath.fileName}")
                    }

                    monitorKey.reset()
                }
            }
        }
        println("daleew")

    }
}