import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.runBlocking

fun main() {
    val fileWatcher = FileWatcher()


    runBlocking(Dispatchers.IO) {

        fileWatcher.start(this)

        fileWatcher.channel.consumeEach { value ->
            println(value)
        }
    }

}
