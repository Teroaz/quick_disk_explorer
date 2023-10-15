data class PathInfo(
    val path: String,
    val pathType: PathType,
    val size: Long,
    val subPaths: List<PathInfo>
) : Comparable<PathInfo> {

    enum class PathType {
        FILE, DIRECTORY, UNKNOWN
    }

    override fun toString(): String {
        return "${path}: ${size / (1024 * 1024)} MB (${pathType})"
    }

    override fun compareTo(other: PathInfo): Int {

        val sizeComparison = size.compareTo(other.size)
        if (sizeComparison != 0) return sizeComparison

        val typeComparison = pathType.compareTo(other.pathType)
        if (typeComparison != 0) return typeComparison

        return path.compareTo(other.path)
    }
}