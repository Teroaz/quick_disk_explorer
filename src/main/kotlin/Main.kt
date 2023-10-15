import kotlinx.coroutines.*
import java.io.File
import java.util.*

fun main() = runBlocking(Dispatchers.IO) {
    val roots = File.listRoots()
    val result = TreeSet<PathInfo>()

    for (root in roots) {
        val start = System.currentTimeMillis()
        val pathInfo = getDirectoryInfo(root)
        result.add(pathInfo)
        val end = System.currentTimeMillis()

        println("Finished processing ${root.path}")
        println("Temps d'exécution: ${end - start} ms, soit ${(end - start) / 1000} s")
    }

    val sortedList = result.toList();
    sortedList.forEach { println(it) }

}

fun shouldIgnore(directory: File): Boolean {
    val ignoredPaths = listOf("\$Recycle.Bin", "\$WinREAgent")
    return ignoredPaths.any { directory.absolutePath.contains(it) }
}

suspend fun getDirectoryInfo(directory: File, depth: Int = 0, maxDepth: Int = 100): PathInfo {
    if (depth > maxDepth || shouldIgnore(directory)) return PathInfo(
        directory.path, PathInfo.PathType.UNKNOWN, 0, emptyList()
    )

    val subPaths = mutableListOf<PathInfo>()

    if (directory.isFile) {
        return PathInfo(directory.path, PathInfo.PathType.FILE, directory.length(), emptyList())
    }

    val files = try {
        directory.listFiles() ?: emptyArray()
    } catch (e: Exception) {
        println("Exception while accessing: ${directory.absolutePath}, ${e.message}")
        emptyArray<File>()
    }

    val deferreds = mutableListOf<Deferred<PathInfo>>()
    for (file in files) {
        deferreds.add(CoroutineScope(Dispatchers.IO).async {
            try {
                getDirectoryInfo(file, depth + 1, maxDepth)
            } catch (e: Exception) {
                println("Exception while processing: ${file.absolutePath}, ${e.message}")
                PathInfo(file.path, PathInfo.PathType.UNKNOWN, 0, emptyList())
            }
        })
    }

    subPaths.addAll(deferreds.awaitAll())

    val totalSize = subPaths.sumOf { it.size }

    return PathInfo(directory.path, PathInfo.PathType.DIRECTORY, totalSize, subPaths)
}

